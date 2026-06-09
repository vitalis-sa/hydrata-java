package fiap.hydrata.controller;

import fiap.hydrata.dto.response.DashboardResponse;
import fiap.hydrata.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "BFF para a tela principal do aplicativo")
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "Obter dados agregados para o dashboard")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Dados agregados retornados com sucesso"),
        @ApiResponse(responseCode = "404", description = "Propriedade não encontrada")
    })
    @GetMapping("/{propriedadeId}")
    public ResponseEntity<DashboardResponse> getDashboard(@PathVariable Long propriedadeId) {
        return ResponseEntity.ok(dashboardService.getDashboard(propriedadeId));
    }
}

