package fiap.hydrata;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class HydrataApplicationTests {

    @Value("${mqtt.broker.url}")
    private String brokerUrl;

    @Value("${mqtt.username}")
    private String username;

    @Value("${mqtt.password}")
    private String password;

    @Test
    void testMqttConnection() {
        System.out.println("====== INICIANDO TESTE DE CONEXÃO MQTT ======");
        System.out.println("Broker URL: " + brokerUrl);
        System.out.println("Username: " + username);
        
        try {
            String clientId = "test-client-" + System.currentTimeMillis();
            MqttClient client = new MqttClient(brokerUrl, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            
            if (username != null && !username.trim().isEmpty()) {
                options.setUserName(username);
                options.setPassword(password.toCharArray());
            }
            
            System.out.println("Tentando conectar...");
            client.connect(options);
            System.out.println("Conectado com sucesso!");
            assertTrue(client.isConnected());
            
            client.disconnect();
            System.out.println("Desconectado.");
        } catch (Exception e) {
            System.err.println("ERRO DE CONEXÃO MQTT:");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
