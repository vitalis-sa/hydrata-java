package fiap.hydrata.dto.response;

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
public class LeituraClimaResponse {
    private Long id;
    private Long dispositivoIotId;
    private BigDecimal umidadeAr;
    private BigDecimal temperatura;
    private LocalDateTime dataLeitura;
}
