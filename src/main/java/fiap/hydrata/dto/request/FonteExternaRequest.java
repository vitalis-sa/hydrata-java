package fiap.hydrata.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FonteExternaRequest {

    @NotBlank(message = "Nome da fonte externa é obrigatório")
    private String nome;

    @NotBlank(message = "Tipo da fonte externa é obrigatório")
    private String tipo;
}
