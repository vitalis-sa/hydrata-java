package fiap.hydrata.scheduler;

import fiap.hydrata.service.AlertaService;
import fiap.hydrata.service.SensorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class IrrigacaoScheduler {

    private final SensorService sensorService;
    private final AlertaService alertaService;

    @Scheduled(fixedRateString = "${scheduler.irrigacao.intervalo:300000}")
    public void analisarCondicoes() {
        log.info("Iniciando análise de condições para irrigação...");
        sensorService.findSensoresAtivos().forEach(alertaService::avaliarEGerar);
        log.info("Análise de condições concluída.");
    }
}
