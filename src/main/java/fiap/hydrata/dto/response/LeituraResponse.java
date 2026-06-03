package fiap.hydrata.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeituraResponse {

    @Schema(description = "ID da leitura", example = "1")
    private Long id;

    @Schema(description = "ID do sensor", example = "1")
    private Long sensorId;

    @Schema(description = "MAC address do sensor", example = "AA:BB:CC:DD:EE:FF")
    private String macAddressSensor;

    @Schema(description = "Umidade do ar (0-100%)", example = "65.5")
    private BigDecimal umidadeAr;

    @Schema(description = "Temperatura em °C", example = "25.3")
    private BigDecimal temperatura;

    @Schema(description = "Luminosidade (0-100%)", example = "80.0")
    private BigDecimal luminosidade;

    @Schema(description = "Data e hora da leitura")
    private LocalDateTime dataLeitura;
}
