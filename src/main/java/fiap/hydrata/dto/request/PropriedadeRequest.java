package fiap.hydrata.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "Nome da propriedade rural", example = "Fazenda Santa Maria")
    @NotBlank(message = "Nome da propriedade é obrigatório")
    private String nome;

    @Schema(description = "Área total em hectares", example = "150.8")
    private Double areaHectares;

    @Schema(description = "Latitude geográfica", example = "-22.9068")
    private Double latitude;

    @Schema(description = "Longitude geográfica", example = "-47.0616")
    private Double longitude;

    @Schema(description = "Nome da cidade", example = "Campinas")
    private String cidade;

    @Schema(description = "Sigla do estado", example = "SP")
    private String estado;

    @Schema(description = "ID do produtor dono da propriedade", example = "1")
    @NotNull(message = "ID do produtor é obrigatório")
    @Positive(message = "ID do produtor deve ser positivo")
    private Long produtorId;

    @Schema(description = "ID do plano de assinatura da propriedade", example = "2")
    @NotNull(message = "ID do plano é obrigatório")
    @Positive(message = "ID do plano deve ser positivo")
    private Long planoId;
}
