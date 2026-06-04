package fiap.hydrata.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OpenMeteoClient {

    private final RestClient restClient;

    @Value("${openmeteo.base-url:https://api.open-meteo.com/v1}")
    private String baseUrl;

    public OpenMeteoClient() {
        this.restClient = RestClient.create();
    }

    public DadosClimaResponse buscarClima(double latitude, double longitude) {
        return restClient.get()
                .uri(baseUrl + "/forecast?latitude={lat}&longitude={lon}&current=temperature_2m,precipitation",
                        latitude, longitude)
                .retrieve()
                .body(DadosClimaResponse.class);
    }
}
