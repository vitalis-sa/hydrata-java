package fiap.hydrata.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import fiap.hydrata.mqtt.payload.ClimaPayload;
import fiap.hydrata.mqtt.payload.LuzPayload;
import fiap.hydrata.mqtt.payload.StatusPayload;
import fiap.hydrata.service.AlertaService;
import fiap.hydrata.service.LeituraService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MqttMessageListener {

    private final LeituraService leituraService;
    private final AlertaService alertaService;
    private final ObjectMapper objectMapper;

    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handleMessage(Message<?> message) {
        String topic = (String) message.getHeaders().get("mqtt_receivedTopic");
        String payload = message.getPayload().toString();
        log.debug("Mensagem MQTT recebida — tópico: {}, payload: {}", topic, payload);

        try {
            if (topic == null) {
                log.warn("Tópico MQTT nulo, mensagem ignorada");
                return;
            }

            switch (topic) {
                case "FIAP/HYDRATA/CLIMA" -> {
                    ClimaPayload clima = objectMapper.readValue(payload, ClimaPayload.class);
                    leituraService.salvarDeClima(clima);
                }
                case "FIAP/HYDRATA/LUZ" -> {
                    LuzPayload luz = objectMapper.readValue(payload, LuzPayload.class);
                    leituraService.salvarDeLuz(luz);
                }
                case "FIAP/HYDRATA/STATUS" -> {
                    StatusPayload status = objectMapper.readValue(payload, StatusPayload.class);
                    alertaService.processarStatusIot(status);
                }
                default -> log.warn("Tópico desconhecido: {}", topic);
            }
        } catch (Exception e) {
            log.error("Erro ao processar mensagem MQTT do tópico {}: {}", topic, e.getMessage(), e);
        }
    }
}
