package fiap.hydrata.mqtt.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ClimaPayload(
    @JsonProperty("temperatura") Double temperatura,
    @JsonProperty("umidade_ar") Double umidadeAr
) {}
