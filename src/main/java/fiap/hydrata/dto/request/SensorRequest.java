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
public class SensorRequest {

    private String modelo;

    @NotBlank(message = "MAC address é obrigatório")
    private String macAddress;

    @NotNull(message = "ID da propriedade é obrigatório")
    @Positive(message = "ID da propriedade deve ser positivo")
    private Long propriedadeId;
}
