package fiap.hydrata.dto.request;

import fiap.hydrata.enums.NivelRisco;
import fiap.hydrata.enums.TipoAlerta;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertaRequest {

    @Schema(description = "ID da propriedade relacionada", example = "1")
    @NotNull(message = "ID da propriedade é obrigatório")
    @Positive(message = "ID da propriedade deve ser positivo")
    private Long propriedadeId;

    @Schema(description = "ID do dado externo relacionado (opcional)", example = "3")
    private Long dadoExternoId;

    @Schema(description = "Tipo do alerta", example = "IRRIGAR")
    @NotNull(message = "Tipo do alerta é obrigatório")
    private TipoAlerta tipo;

    @Schema(description = "Nível de risco do alerta", example = "MEDIO")
    @NotNull(message = "Nível de risco é obrigatório")
    private NivelRisco nivelRisco;

    @Schema(description = "Mensagem descritiva do alerta", example = "Solo seco sem previsão de chuva")
    private String mensagem;

    @Schema(description = "Recomendação de ação", example = "Ligar as bombas de irrigação")
    private String recomendacao;
}
