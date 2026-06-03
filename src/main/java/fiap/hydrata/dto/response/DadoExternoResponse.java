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
public class DadoExternoResponse {

    @Schema(description = "ID do dado externo", example = "1")
    private Long id;

    @Schema(description = "ID da fonte externa", example = "1")
    private Long fonteExternaId;

    @Schema(description = "Nome da fonte externa", example = "Open-Meteo")
    private String nomeFonteExterna;

    @Schema(description = "Tipo do dado", example = "PRECIPITACAO")
    private String tipo;

    @Schema(description = "Valor coletado", example = "12.5")
    private BigDecimal valor;

    @Schema(description = "Observação", example = "Chuva moderada")
    private String observacao;

    @Schema(description = "Data e hora da coleta")
    private LocalDateTime dataColeta;
}
