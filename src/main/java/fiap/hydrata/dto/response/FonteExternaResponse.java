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
public class FonteExternaResponse {

    @Schema(description = "ID da fonte externa", example = "1")
    private Long id;

    @Schema(description = "Nome da fonte", example = "Open-Meteo")
    private String nome;

    @Schema(description = "Tipo da fonte", example = "API_CLIMA")
    private String tipo;

    @Schema(description = "Status da fonte", example = "ATIVO")
    private String status;
}
