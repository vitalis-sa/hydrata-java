package fiap.hydrata.service;

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

@Service
@RequiredArgsConstructor
@Slf4j
public class LeituraService {

    private final LeituraClimaRepository climaRepository;
    private final LeituraLuzRepository luzRepository;
    private final DispositivoIotRepository dispositivoIotRepository;

    @Transactional
    public void salvarDeClima(ClimaPayload clima, String macAddress) {
        log.info("[DEBUG-MQTT] Tentando salvar dados de CLIMA para MAC: {}", macAddress);
        var dispositivoOpt = dispositivoIotRepository.findByMacAddress(macAddress);
        
        if (dispositivoOpt.isEmpty()) {
            log.warn("[DEBUG-MQTT] Dispositivo com MAC {} NÃO ENCONTRADO! Ignorando a leitura de clima.", macAddress);
            return;
        }
        
        var dispositivo = dispositivoOpt.get();
        log.info("[DEBUG-MQTT] Associando leitura de clima ao dispositivo ID: {}", dispositivo.getId());
        
        LeituraClima leitura = LeituraClima.builder()
                .dispositivoIot(dispositivo)
                .temperatura(clima.temperatura() != null ? BigDecimal.valueOf(clima.temperatura()) : BigDecimal.ZERO)
                .umidadeAr(clima.umidadeAr() != null ? BigDecimal.valueOf(clima.umidadeAr()) : BigDecimal.ZERO)
                .dataLeitura(LocalDateTime.now())
                .build();
                
        climaRepository.save(leitura);
        log.info("[DEBUG-MQTT] Leitura de clima persistida com SUCESSO no banco!");
    }

    @Transactional
    public void salvarDeLuz(LuzPayload luz, String macAddress) {
        log.info("[DEBUG-MQTT] Tentando salvar dados de LUZ para MAC: {}", macAddress);
        var dispositivoOpt = dispositivoIotRepository.findByMacAddress(macAddress);
        
        if (dispositivoOpt.isEmpty()) {
            log.warn("[DEBUG-MQTT] Dispositivo com MAC {} NÃO ENCONTRADO! Ignorando a leitura de luz.", macAddress);
            return;
        }
        
        var dispositivo = dispositivoOpt.get();
        log.info("[DEBUG-MQTT] Associando leitura de luz ao dispositivo ID: {}", dispositivo.getId());
        
        LeituraLuz leitura = LeituraLuz.builder()
                .dispositivoIot(dispositivo)
                .luminosidade(luz.luminosidade() != null ? BigDecimal.valueOf(luz.luminosidade()) : BigDecimal.ZERO)
                .dataLeitura(LocalDateTime.now())
                .build();
                
        luzRepository.save(leitura);
        log.info("[DEBUG-MQTT] Leitura de luz persistida com SUCESSO no banco!");
    }
}
