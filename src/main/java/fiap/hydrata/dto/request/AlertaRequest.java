package fiap.hydrata.dto.request;

import fiap.hydrata.enums.NivelRisco;
import fiap.hydrata.enums.TipoAlerta;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertaRequest {

    @NotNull(message = "ID da propriedade é obrigatório")
    @Positive(message = "ID da propriedade deve ser positivo")
    private Long propriedadeId;

    private Long leituraId;

    private Long dadoExternoId;

    @NotNull(message = "Tipo do alerta é obrigatório")
    private TipoAlerta tipo;

    @NotNull(message = "Nível de risco é obrigatório")
    private NivelRisco nivelRisco;

    private String mensagem;

    private String recomendacao;
}
