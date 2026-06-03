package fiap.hydrata.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropriedadeRequest {

    @NotBlank(message = "Nome da propriedade é obrigatório")
    private String nome;

    private Double areaHectares;

    private Double latitude;

    private Double longitude;

    private String cidade;

    private String estado;

    @NotNull(message = "ID do produtor é obrigatório")
    @Positive(message = "ID do produtor deve ser positivo")
    private Long produtorId;

    @NotNull(message = "ID do plano é obrigatório")
    @Positive(message = "ID do plano deve ser positivo")
    private Long planoId;
}
