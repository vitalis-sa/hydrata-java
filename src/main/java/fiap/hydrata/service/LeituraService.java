package fiap.hydrata.service;

import fiap.hydrata.dto.response.LeituraClimaResponse;
import fiap.hydrata.entity.DispositivoIot;
import fiap.hydrata.entity.LeituraClima;
import fiap.hydrata.entity.LeituraLuz;
import fiap.hydrata.mqtt.payload.ClimaPayload;
import fiap.hydrata.mqtt.payload.LuzPayload;
import fiap.hydrata.repository.DispositivoIotRepository;
import fiap.hydrata.repository.LeituraClimaRepository;
import fiap.hydrata.repository.LeituraLuzRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeituraService {

    private final LeituraClimaRepository climaRepository;
    private final LeituraLuzRepository luzRepository;
    private final DispositivoIotRepository dispositivoIotRepository;

    @Transactional
    public void salvarDeClima(ClimaPayload clima, String macAddress) {
        var dispositivoOpt = dispositivoIotRepository.findByMacAddress(macAddress);
        
        if (dispositivoOpt.isEmpty()) {
            log.warn("❌ [DB REJECT] Leitura de CLIMA ignorada! MAC '{}' não está cadastrado.", macAddress);
            return;
        }
        
        var dispositivo = dispositivoOpt.get();
        LeituraClima leitura = LeituraClima.builder()
                .dispositivoIot(dispositivo)
                .temperatura(clima.temperatura() != null ? BigDecimal.valueOf(clima.temperatura()) : BigDecimal.ZERO)
                .umidadeAr(clima.umidadeAr() != null ? BigDecimal.valueOf(clima.umidadeAr()) : BigDecimal.ZERO)
                .dataLeitura(LocalDateTime.now())
                .build();
                
        climaRepository.save(leitura);
        log.info("☁️  [DB PERSIST] Leitura CLIMA salva com SUCESSO - Temp: {}, Umidade: {} (Disp ID: {})",
                leitura.getTemperatura(), leitura.getUmidadeAr(), dispositivo.getId());
    }

    @Transactional
    public void salvarDeLuz(LuzPayload luz, String macAddress) {
        var dispositivoOpt = dispositivoIotRepository.findByMacAddress(macAddress);
        
        if (dispositivoOpt.isEmpty()) {
            log.warn("❌ [DB REJECT] Leitura de LUZ ignorada! MAC '{}' não está cadastrado.", macAddress);
            return;
        }
        
        var dispositivo = dispositivoOpt.get();
        LeituraLuz leitura = LeituraLuz.builder()
                .dispositivoIot(dispositivo)
                .luminosidade(luz.luminosidade() != null ? BigDecimal.valueOf(luz.luminosidade()) : BigDecimal.ZERO)
                .dataLeitura(LocalDateTime.now())
                .build();
                
        luzRepository.save(leitura);
        log.info("☀️  [DB PERSIST] Leitura LUZ salva com SUCESSO - Luminosidade: {} (Disp ID: {})",
                leitura.getLuminosidade(), dispositivo.getId());
    }

    public List<LeituraClimaResponse> getHistorico(Long propriedadeId, int dias) {
        Optional<DispositivoIot> dispositivoOpt = dispositivoIotRepository.findByPropriedadeId(propriedadeId);
        if (dispositivoOpt.isEmpty()) {
            return List.of();
        }

        LocalDateTime dataCorte = LocalDateTime.now().minusDays(dias);
        var leituras = climaRepository.findByDispositivoIotIdAndDataLeituraAfterOrderByDataLeituraDesc(dispositivoOpt.get().getId(), dataCorte);

        return leituras.stream()
                .map(l -> LeituraClimaResponse.builder()
                        .id(l.getId())
                        .dispositivoIotId(l.getDispositivoIot().getId())
                        .umidadeAr(l.getUmidadeAr())
                        .temperatura(l.getTemperatura())
                        .dataLeitura(l.getDataLeitura())
                        .build())
                .collect(Collectors.toList());
    }
}
