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
public class DispositivoIotRequest {

    @Schema(description = "Modelo do dispositivo IoT", example = "ESP32-DHT22-LDR")
    private String modelo;

    @Schema(description = "Endereço MAC físico do dispositivo", example = "00:1B:44:11:3A:B7")
    @NotBlank(message = "MAC address é obrigatório")
    private String macAddress;

    @Schema(description = "ID da propriedade relacionada", example = "1")
    @NotNull(message = "ID da propriedade é obrigatório")
    @Positive(message = "ID da propriedade deve ser positivo")
    private Long propriedadeId;
}
