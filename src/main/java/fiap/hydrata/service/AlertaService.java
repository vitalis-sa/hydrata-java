package fiap.hydrata.service;

import fiap.hydrata.dto.request.AlertaRequest;
import fiap.hydrata.dto.response.AlertaResponse;
import fiap.hydrata.dto.response.DeleteResponse;
import fiap.hydrata.entity.Alerta;
import fiap.hydrata.entity.Sensor;
import fiap.hydrata.enums.NivelRisco;
import fiap.hydrata.enums.StatusAlerta;
import fiap.hydrata.enums.TipoAlerta;
import fiap.hydrata.exception.ResourceNotFoundException;
import fiap.hydrata.mapper.AlertaMapper;
import fiap.hydrata.mqtt.payload.StatusPayload;
import fiap.hydrata.repository.AlertaRepository;
import fiap.hydrata.repository.LeituraRepository;
import fiap.hydrata.repository.PropriedadeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertaService {

    private final AlertaRepository repository;
    private final AlertaMapper mapper;
    private final PropriedadeRepository propriedadeRepository;
    private final LeituraRepository leituraRepository;

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
        if (request.getLeituraId() != null) {
            entity.setLeitura(leituraRepository.findById(request.getLeituraId())
                    .orElseThrow(() -> new ResourceNotFoundException("Leitura não encontrada com id: " + request.getLeituraId())));
        }
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
    public void avaliarEGerar(Sensor sensor) {
        fiap.hydrata.entity.Leitura ultima = leituraRepository.findFirstBySensorIdOrderByIdDesc(sensor.getId())
                .orElse(null);
        if (ultima == null) return;

        if (ultima.getUmidadeAr() != null && ultima.getUmidadeAr().doubleValue() < 40.0) {
            Alerta alerta = Alerta.builder()
                    .propriedade(sensor.getPropriedade())
                    .leitura(ultima)
                    .tipo(TipoAlerta.IRRIGAR)
                    .nivelRisco(ultima.getUmidadeAr().doubleValue() < 20.0 ? NivelRisco.CRITICO : NivelRisco.ALTO)
                    .mensagem("Umidade do ar abaixo de 40%: " + ultima.getUmidadeAr() + "%")
                    .recomendacao("Iniciar irrigação imediatamente")
                    .status(StatusAlerta.ATIVO)
                    .dataGeracao(LocalDateTime.now())
                    .build();
            repository.save(alerta);
            log.info("Alerta IRRIGAR gerado para propriedade id={}", sensor.getPropriedade().getId());
        }
    }

    public void processarStatusIot(StatusPayload status) {
        log.info("[DEBUG-MQTT] Recebido status IoT - Bomba Ativa: {}, Alerta Crítico: {}", 
                status.bombaAtiva(), status.alertaCritico());
        if (Boolean.TRUE.equals(status.alertaCritico())) {
            log.warn("[DEBUG-MQTT] Status IoT indicou alerta crítico — aguardando avaliação do scheduler");
        }
    }
}
