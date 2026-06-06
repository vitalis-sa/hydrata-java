package fiap.hydrata.scheduler;

import fiap.hydrata.entity.DispositivoIot;
import fiap.hydrata.entity.LeituraClima;
import fiap.hydrata.repository.LeituraClimaRepository;
import fiap.hydrata.service.AlertaService;
import fiap.hydrata.service.DispositivoIotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SensorHealthCheckScheduler {

    private final DispositivoIotService dispositivoIotService;
    private final LeituraClimaRepository leituraClimaRepository;
    private final AlertaService alertaService;

    // Roda a cada 5 minutos
    @Scheduled(fixedRateString = "300000")
    public void verificarSensoresOffline() {
        log.info("🔍 Iniciando verificação de Health Check dos Sensores...");
        
        LocalDateTime agora = LocalDateTime.now();
        
        for (DispositivoIot dispositivo : dispositivoIotService.findDispositivosIotAtivos()) {
            Optional<LeituraClima> ultimaLeitura = leituraClimaRepository.findFirstByDispositivoIotIdOrderByIdDesc(dispositivo.getId());
            
            boolean offline = false;
            
            if (ultimaLeitura.isEmpty()) {
                offline = true; // Nunca enviou leitura
            } else {
                // Se a data de coleta for anterior a 10 minutos atrás
                if (ultimaLeitura.get().getDataLeitura() != null && ultimaLeitura.get().getDataLeitura().isBefore(agora.minusMinutes(10))) {
                    offline = true;
                }
            }
            
            if (offline) {
                alertaService.registrarAlertaOffline(dispositivo);
            }
        }
        log.info("✅ Health Check de Sensores concluído.");
    }
}
