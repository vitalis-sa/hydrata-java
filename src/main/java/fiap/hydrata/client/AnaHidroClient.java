package fiap.hydrata.client;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AnaHidroClient {

    public NivelRioResponse buscarNivelRio(String codigoEstacao) {
        // MOCK: retorna dados simulados para fins de demonstração
        return NivelRioResponse.builder()
                .codigoEstacao(codigoEstacao)
                .nivelMetros(2.5 + Math.random() * 3.0)
                .dataLeitura(LocalDateTime.now())
                .build();
    }
}
