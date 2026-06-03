package fiap.hydrata.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class InpeBdqueimadasClient {

    public FocoQueimadaResponse buscarFocosQueimada(double latitude, double longitude) {
        // MOCK: retorna dados simulados para fins de demonstração
        return FocoQueimadaResponse.builder()
                .latitude(latitude)
                .longitude(longitude)
                .quantidadeFocos((int) (Math.random() * 5))
                .dataConsulta(LocalDateTime.now())
                .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FocoQueimadaResponse {
        private Double latitude;
        private Double longitude;
        private Integer quantidadeFocos;
        private LocalDateTime dataConsulta;
    }
}
