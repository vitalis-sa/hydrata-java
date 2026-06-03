package fiap.hydrata.mqtt.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StatusPayload(
    @JsonProperty("bomba_ativa") Boolean bombaAtiva,
    @JsonProperty("alerta_critico") Boolean alertaCritico
) {}
