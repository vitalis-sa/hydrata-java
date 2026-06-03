package fiap.hydrata.dto.request;

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

    @NotNull(message = "ID da fonte externa é obrigatório")
    @Positive(message = "ID da fonte externa deve ser positivo")
    private Long fonteExternaId;

    @NotBlank(message = "Tipo do dado externo é obrigatório")
    private String tipo;

    private BigDecimal valor;

    private String observacao;
}
