package fiap.hydrata.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropriedadeResponse {

    @Schema(description = "ID da propriedade", example = "1")
    private Long id;

    @Schema(description = "Nome da propriedade", example = "Fazenda Boa Vista")
    private String nome;

    @Schema(description = "Área em hectares", example = "150.5")
    private Double areaHectares;

    @Schema(description = "Latitude", example = "-23.550520")
    private Double latitude;

    @Schema(description = "Longitude", example = "-46.633308")
    private Double longitude;

    @Schema(description = "Cidade", example = "São Paulo")
    private String cidade;

    @Schema(description = "Estado (UF)", example = "SP")
    private String estado;

    @Schema(description = "Status da propriedade", example = "ATIVO")
    private String status;

    @Schema(description = "Nome do produtor", example = "João da Silva")
    private String nomeProdutor;

    @Schema(description = "ID do produtor", example = "1")
    private Long produtorId;

    @Schema(description = "Nome do plano", example = "Plano Premium")
    private String nomePlano;
}
