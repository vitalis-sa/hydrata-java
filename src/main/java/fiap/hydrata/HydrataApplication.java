package fiap.hydrata;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@OpenAPIDefinition(
    info = @Info(
        title = "HyDrata API — Java Backend",
        version = "1.0.0",
        description = "API de monitoramento hídrico e otimização de irrigação. " +
                      "Gerencia Propriedades, Dispositivos IoT, Leituras, Alertas, Fontes Externas e Dados Externos.",
        contact = @Contact(
            name = "Vitalis",
            url = "https://github.com/vitalis-sa/hydrata-java"
        )
    ),
    security = @SecurityRequirement(name = "Bearer Authentication")
)
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
@SpringBootApplication(exclude = { org.springdoc.core.configuration.SpringDocHateoasConfiguration.class })
@EnableScheduling
public class HydrataApplication {

    public static void main(String[] args) {
        SpringApplication.run(HydrataApplication.class, args);
    }
}
