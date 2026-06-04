package fiap.hydrata.service;

import fiap.hydrata.client.AnaHidroClient;
import fiap.hydrata.client.InpeBdqueimadasClient;
import fiap.hydrata.dto.request.AlertaRequest;
import fiap.hydrata.dto.response.AlertaResponse;
import fiap.hydrata.dto.response.DeleteResponse;
import fiap.hydrata.entity.*;
import fiap.hydrata.enums.NivelRisco;
import fiap.hydrata.enums.StatusAlerta;
import fiap.hydrata.enums.TipoAlerta;
import fiap.hydrata.exception.ResourceNotFoundException;
import fiap.hydrata.mapper.AlertaMapper;
import fiap.hydrata.mqtt.payload.StatusPayload;
import fiap.hydrata.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertaService {

    private final AlertaRepository repository;
    private final AlertaMapper mapper;
    private final PropriedadeRepository propriedadeRepository;
    private final LeituraClimaRepository leituraClimaRepository;
    private final LeituraLuzRepository leituraLuzRepository;

    private final AnaHidroClient anaClient;
    private final InpeBdqueimadasClient inpeClient;
    private final DadoExternoRepository dadoExternoRepository;
    private final FonteExternaRepository fonteExternaRepository;

    public List<AlertaResponse> findAll() {
        return mapper.toResponseList(repository.findAll());
    }

    public AlertaResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta não encontrado com id: " + id));
    }

    @Transactional
    public AlertaResponse create(AlertaRequest request) {
        Alerta entity = mapper.toEntity(request);
        entity.setPropriedade(propriedadeRepository.findById(request.getPropriedadeId())
                .orElseThrow(() -> new ResourceNotFoundException("Propriedade não encontrada com id: " + request.getPropriedadeId())));
        entity.setStatus(StatusAlerta.ATIVO);
        entity.setDataGeracao(LocalDateTime.now());
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public AlertaResponse update(Long id, AlertaRequest request) {
        Alerta entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta não encontrado com id: " + id));
        mapper.updateEntity(request, entity);
        entity.setPropriedade(propriedadeRepository.findById(request.getPropriedadeId())
                .orElseThrow(() -> new ResourceNotFoundException("Propriedade não encontrada com id: " + request.getPropriedadeId())));
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public DeleteResponse delete(Long id) {
        repository.findById(id)
                .ifPresentOrElse(
                        repository::delete,
                        () -> { throw new ResourceNotFoundException("Alerta não encontrado com id: " + id); }
                );
        return DeleteResponse.of("Alerta", id);
    }

    @Transactional
    public void avaliarEGerar(DispositivoIot dispositivo) {
        LeituraClima ultimaClima = leituraClimaRepository.findFirstByDispositivoIotIdOrderByIdDesc(dispositivo.getId())
                .orElse(null);
        LeituraLuz ultimaLuz = leituraLuzRepository.findFirstByDispositivoIotIdOrderByIdDesc(dispositivo.getId())
                .orElse(null);
        
        if (ultimaClima == null) return;

        Propriedade propriedade = propriedadeRepository.findById(dispositivo.getPropriedade().getId()).orElse(null);
        if (propriedade == null) return;

        double umidade = ultimaClima.getUmidadeAr() != null ? ultimaClima.getUmidadeAr().doubleValue() : 100.0;
        
        // 1. DADOS EXTERNOS DO INPE
        double lat = propriedade.getCoordenadas() != null && propriedade.getCoordenadas().getLatitude() != null ? propriedade.getCoordenadas().getLatitude() : 0.0;
        double lon = propriedade.getCoordenadas() != null && propriedade.getCoordenadas().getLongitude() != null ? propriedade.getCoordenadas().getLongitude() : 0.0;
        
        var respInpe = inpeClient.buscarFocosQueimada(lat, lon);
        FonteExterna fonteInpe = obterOuCriarFonte("INPE BDQueimadas", new FonteExternaSatelital());
        DadoExterno dadoInpe = DadoExterno.builder()
                .fonteExterna(fonteInpe)
                .tipo("FOCOS_QUEIMADA")
                .valor(BigDecimal.valueOf(respInpe.getQuantidadeFocos()))
                .dataColeta(LocalDateTime.now())
                .observacao("Busca de queimadas na latitude " + lat + " e longitude " + lon)
                .build();
        dadoExternoRepository.save(dadoInpe);

        // 2. DADOS EXTERNOS DA ANA
        var respAna = anaClient.buscarNivelRio("EST-HIDRO-01");
        FonteExterna fonteAna = obterOuCriarFonte("ANA Nível Rio", new FonteExternaApi());
        DadoExterno dadoAna = DadoExterno.builder()
                .fonteExterna(fonteAna)
                .tipo("NIVEL_RIO")
                .valor(BigDecimal.valueOf(respAna.getNivelMetros()))
                .dataColeta(LocalDateTime.now())
                .observacao("Medição do nível do rio próximo (" + respAna.getCodigoEstacao() + ")")
                .build();
        dadoExternoRepository.save(dadoAna);

        // 3. MOTOR DE REGRAS DE NEGÓCIO COMBINADAS
        int focos = respInpe.getQuantidadeFocos() != null ? respInpe.getQuantidadeFocos() : 0;
        double nivelRio = respAna.getNivelMetros() != null ? respAna.getNivelMetros() : 5.0;

        String luzStr = (ultimaLuz != null && ultimaLuz.getLuminosidade() != null) ? 
                ultimaLuz.getLuminosidade() + "%" : "Desconhecida";

        // Regra 1: Risco Crítico de Incêndio (Umidade Baixa + Focos do INPE)
        if (umidade < 30.0 && focos > 0) {
            gerarAlerta(propriedade, ultimaClima, ultimaLuz, dadoInpe, TipoAlerta.QUEIMADA, NivelRisco.CRITICO,
                    "Risco de Incêndio! Umidade: " + umidade + "%, Focos (INPE): " + focos,
                    "Acionar imediatamente os aspersores para umedecer o perímetro da fazenda.");
            return; // Impede sobreposição
        }

        // Regra 2: Escassez Hídrica da ANA
        if (nivelRio < 3.0) {
            gerarAlerta(propriedade, ultimaClima, ultimaLuz, dadoAna, TipoAlerta.SECA, NivelRisco.ALTO,
                    "Rio com nível baixo (ANA): " + String.format("%.2f", nivelRio) + "m. Umidade: " + umidade + "%",
                    "Racionamento de água. Não ligar bombas pesadas para não esgotar as reservas hídricas regionais.");
            return;
        }

        // Regra 3: Irrigação Padrão Cruzada
        if (umidade < 40.0) {
            gerarAlerta(propriedade, ultimaClima, ultimaLuz, dadoAna, TipoAlerta.IRRIGAR, NivelRisco.MEDIO,
                    "Umidade abaixo do ideal: " + umidade + "%. Rio (ANA): " + String.format("%.2f", nivelRio) + "m",
                    "Iniciar irrigação controlada. Condições hídricas externas favoráveis.");
        }
    }

    private void gerarAlerta(Propriedade propriedade, LeituraClima clima, LeituraLuz luz, DadoExterno externo,
                             TipoAlerta tipo, NivelRisco risco, String mensagem, String recomendacao) {
        Alerta alerta = Alerta.builder()
                .propriedade(propriedade)
                .leituraClima(clima)
                .leituraLuz(luz)
                .dadoExterno(externo)
                .tipo(tipo)
                .nivelRisco(risco)
                .mensagem(mensagem)
                .recomendacao(recomendacao)
                .status(StatusAlerta.ATIVO)
                .dataGeracao(LocalDateTime.now())
                .build();
        repository.save(alerta);
        log.info("🚨 [ALERTA GERADO] Tipo: {} | Risco: {} | Prop. ID: {}", tipo, risco, propriedade.getId());
    }

    private FonteExterna obterOuCriarFonte(String nome, FonteExterna defaultFonte) {
        return fonteExternaRepository.findByNome(nome).orElseGet(() -> {
            defaultFonte.setNome(nome);
            defaultFonte.setStatus("ATIVO");
            return fonteExternaRepository.save(defaultFonte);
        });
    }

    public void processarStatusIot(StatusPayload status, String macAddress) {
        if (Boolean.TRUE.equals(status.alertaCritico())) {
            log.warn("🔥 [HW ALERT] ESP32 (MAC: {}) reportou superaquecimento local!", macAddress);
        } else if (Boolean.TRUE.equals(status.bombaAtiva())) {
            log.info("💧 [HW STATUS] Bomba D'água ATIVADA no MAC: {}", macAddress);
        }
    }
}
