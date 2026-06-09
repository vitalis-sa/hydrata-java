package fiap.hydrata.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FonteExternaRequest {

    @Schema(description = "Nome da fonte externa de dados", example = "INPE BDQueimadas")
    @NotBlank(message = "Nome da fonte externa é obrigatório")
    private String nome;

    @Schema(description = "Tipo de integração da fonte", example = "SATELITE")
    @NotBlank(message = "Tipo da fonte externa é obrigatório")
    private String tipo;
}
