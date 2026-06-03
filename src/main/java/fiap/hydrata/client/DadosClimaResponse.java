package fiap.hydrata.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DadosClimaResponse {

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("longitude")
    private Double longitude;

    @JsonProperty("current")
    private DadosAtuais current;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DadosAtuais {

        @JsonProperty("temperature_2m")
        private Double temperatura;

        @JsonProperty("precipitation")
        private Double precipitacao;
    }
}
