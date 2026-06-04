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
import jakarta.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
@Slf4j
public class MqttMessageListener {

    private final LeituraService leituraService;
    private final AlertaService alertaService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        log.info("[DEBUG-MQTT] MqttMessageListener inicializado e aguardando mensagens em 'mqttInputChannel'!");
    }

    public void handleMessage(Message<?> message) {
        String topic = (String) message.getHeaders().get("mqtt_receivedTopic");
        String payload = message.getPayload().toString();
        log.info("[DEBUG-MQTT] MENSAGEM RECEBIDA DO BROKER - Tópico: {}, Payload: {}", topic, payload);

        try {
            if (topic == null) {
                log.warn("Tópico MQTT nulo, mensagem ignorada");
                return;
            }

            String[] parts = topic.split("/");
            if (parts.length < 4) {
                log.warn("Formato de tópico inválido: {}", topic);
                return;
            }
            
            String macAddress = parts[2];
            String type = parts[3];

            switch (type) {
                case "CLIMA" -> {
                    ClimaPayload clima = objectMapper.readValue(payload, ClimaPayload.class);
                    leituraService.salvarDeClima(clima, macAddress);
                }
                case "LUZ" -> {
                    LuzPayload luz = objectMapper.readValue(payload, LuzPayload.class);
                    leituraService.salvarDeLuz(luz, macAddress);
                }
                case "STATUS" -> {
                    StatusPayload status = objectMapper.readValue(payload, StatusPayload.class);
                    alertaService.processarStatusIot(status, macAddress);
                }
                default -> log.warn("Tipo desconhecido no tópico: {}", type);
            }
        } catch (Exception e) {
            log.error("Erro ao processar mensagem MQTT do tópico {}: {}", topic, e.getMessage(), e);
        }
    }
}
