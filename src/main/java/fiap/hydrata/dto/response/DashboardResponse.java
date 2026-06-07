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
public class DashboardResponse {
    private AlertaResponse ultimaDecisao;
    private BigDecimal umidadeAr;
    private Boolean previsaoChuva;
    private BigDecimal temperaturaMax;
    private String nivelRioStatus;
    private String nomeRio;
    private Integer focosQueimada;
    private LocalDateTime ultimaLeitura;
}
