package fiap.hydrata.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeituraRequest {

    @NotNull(message = "ID do sensor é obrigatório")
    @Positive(message = "ID do sensor deve ser positivo")
    private Long sensorId;

    @NotNull(message = "Umidade do ar é obrigatória")
    @DecimalMin(value = "0.0", message = "Umidade do ar deve ser >= 0")
    @DecimalMax(value = "100.0", message = "Umidade do ar deve ser <= 100")
    private BigDecimal umidadeAr;

    private BigDecimal temperatura;

    @DecimalMin(value = "0.0", message = "Luminosidade deve ser >= 0")
    @DecimalMax(value = "100.0", message = "Luminosidade deve ser <= 100")
    private BigDecimal luminosidade;
}
