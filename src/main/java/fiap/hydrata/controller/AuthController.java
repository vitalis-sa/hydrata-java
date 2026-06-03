package fiap.hydrata.controller;

import fiap.hydrata.config.JwtUtil;
import fiap.hydrata.dto.request.LoginRequest;
import fiap.hydrata.dto.response.LoginResponse;
import fiap.hydrata.exception.BusinessException;
import fiap.hydrata.repository.ProdutorRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Login e geração de token JWT")
public class AuthController {

    private final ProdutorRepository produtorRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "Realizar login e obter token JWT")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
        @ApiResponse(responseCode = "422", description = "Credenciais inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return produtorRepository.findByEmail(request.getEmail())
                .filter(p -> passwordEncoder.matches(request.getSenha(), p.getSenha()))
                .map(p -> ResponseEntity.ok(LoginResponse.builder()
                        .token(jwtUtil.generateToken(p.getEmail()))
                        .email(p.getEmail())
                        .build()))
                .orElseThrow(() -> new BusinessException("Email ou senha inválidos"));
    }
}
