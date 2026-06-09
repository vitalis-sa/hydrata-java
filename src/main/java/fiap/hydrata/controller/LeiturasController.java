package fiap.hydrata.controller;

import fiap.hydrata.dto.response.LeituraClimaResponse;
import fiap.hydrata.service.LeituraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leituras")
@RequiredArgsConstructor
@Tag(name = "Leituras", description = "Histórico de telemetria dos sensores")
public class LeiturasController {

    private final LeituraService leituraService;

    @Operation(summary = "Listar histórico de clima de uma propriedade")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Histórico retornado com sucesso")
    })
    @GetMapping("/historico/{propriedadeId}")
    public ResponseEntity<List<LeituraClimaResponse>> getHistorico(
            @PathVariable Long propriedadeId,
            @RequestParam(defaultValue = "7") int dias) {
        
        return ResponseEntity.ok(leituraService.getHistorico(propriedadeId, dias));
    }
}
