package fiap.hydrata.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DadoExternoRequest {

    @Schema(description = "ID da fonte externa relacionada", example = "2")
    @NotNull(message = "ID da fonte externa é obrigatório")
    @Positive(message = "ID da fonte externa deve ser positivo")
    private Long fonteExternaId;

    @Schema(description = "Tipo do dado externo coletado", example = "NIVEL_RIO")
    @NotBlank(message = "Tipo do dado externo é obrigatório")
    private String tipo;

    @Schema(description = "Valor da medição", example = "2.8")
    private BigDecimal valor;

    @Schema(description = "Observações adicionais", example = "Rio monitorado na estação principal")
    private String observacao;
}
