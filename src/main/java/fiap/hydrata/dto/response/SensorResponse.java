package fiap.hydrata.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorResponse {

    @Schema(description = "ID do sensor", example = "1")
    private Long id;

    @Schema(description = "Modelo do sensor", example = "ESP32")
    private String modelo;

    @Schema(description = "MAC address do sensor", example = "AA:BB:CC:DD:EE:FF")
    private String macAddress;

    @Schema(description = "Status do sensor", example = "ATIVO")
    private String status;

    @Schema(description = "Nome da propriedade vinculada", example = "Fazenda Boa Vista")
    private String nomePropriedade;

    @Schema(description = "Data de cadastro")
    private LocalDateTime dataCadastro;
}
