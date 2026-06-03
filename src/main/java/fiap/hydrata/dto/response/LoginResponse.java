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
public class LoginResponse {

    @Schema(description = "Token JWT", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;

    @Schema(description = "Email do produtor autenticado", example = "joao@fazenda.com")
    private String email;
}
