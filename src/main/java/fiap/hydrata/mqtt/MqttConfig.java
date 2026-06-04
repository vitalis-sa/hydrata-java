package fiap.hydrata.mqtt;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.config.EnableIntegration;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableIntegration
@Slf4j
public class MqttConfig {

    @Value("${mqtt.broker.url:tcp://broker.mqtt.cool:1883}")
    private String brokerUrl;

    @Value("${mqtt.client.id:hydrata-java-api}")
    private String clientId;

    @Value("${mqtt.username:}")
    private String username;

    @Value("${mqtt.password:}")
    private String password;

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        log.info("[DEBUG-MQTT] Criando MqttPahoClientFactory...");
        log.info("[DEBUG-MQTT] URL do Broker: {}", brokerUrl);
        log.info("[DEBUG-MQTT] Client ID Base: {}", clientId);
        log.info("[DEBUG-MQTT] Username: {}", username);
        
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{ brokerUrl });
        options.setCleanSession(true);
        options.setAutomaticReconnect(true);
        
        if (username != null && !username.trim().isEmpty()) {
            options.setUserName(username);
        }
        if (password != null && !password.trim().isEmpty()) {
            options.setPassword(password.toCharArray());
        }
        
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new PublishSubscribeChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler mqttMessageHandler(MqttMessageListener listener) {
        log.info("[DEBUG-MQTT] Registrando MqttMessageListener como MessageHandler no mqttInputChannel");
        return listener::handleMessage;
    }

    @Bean
    public MqttPahoMessageDrivenChannelAdapter mqttInbound(MqttPahoClientFactory factory, MessageChannel mqttInputChannel) {
        String uniqueClientId = clientId + "-inbound-" + java.util.UUID.randomUUID().toString().substring(0, 8);
        log.info("[DEBUG-MQTT] Criando adaptador MqttPahoMessageDrivenChannelAdapter...");
        log.info("[DEBUG-MQTT] Client ID Único: {}", uniqueClientId);
        log.info("[DEBUG-MQTT] Tópicos assinados: FIAP/HYDRATA/CLIMA, FIAP/HYDRATA/LUZ, FIAP/HYDRATA/STATUS");
        
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                uniqueClientId, factory,
                "FIAP/HYDRATA/CLIMA",
                "FIAP/HYDRATA/LUZ",
                "FIAP/HYDRATA/STATUS"
        );
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel);
        return adapter;
    }
}
