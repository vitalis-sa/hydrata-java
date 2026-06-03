package fiap.hydrata.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NivelRioResponse {
    private String codigoEstacao;
    private Double nivelMetros;
    private LocalDateTime dataLeitura;
}
