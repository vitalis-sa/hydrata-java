package fiap.hydrata.mqtt.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LuzPayload(
    @JsonProperty("luminosidade") Integer luminosidade
) {}
