package fiap.hydrata.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.integration.mqtt.event.MqttIntegrationEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MqttEventListener {

    @EventListener
    public void handleMqttEvent(MqttIntegrationEvent event) {
        log.info("[DEBUG-MQTT-EVENT] Evento MQTT recebido: {}", event.toString());
        if (event.getCause() != null) {
            log.error("[DEBUG-MQTT-EVENT] Causa do evento/erro:", event.getCause());
        }
    }
}
