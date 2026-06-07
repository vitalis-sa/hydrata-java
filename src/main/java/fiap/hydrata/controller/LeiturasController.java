package fiap.hydrata.controller;

import fiap.hydrata.dto.response.LeituraClimaResponse;
import fiap.hydrata.entity.DispositivoIot;
import fiap.hydrata.repository.DispositivoIotRepository;
import fiap.hydrata.repository.LeituraClimaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/leituras")
@RequiredArgsConstructor
@Tag(name = "Leituras", description = "Histórico de telemetria dos sensores")
public class LeiturasController {

    private final LeituraClimaRepository leituraClimaRepository;
    private final DispositivoIotRepository dispositivoIotRepository;

    @Operation(summary = "Listar histórico de clima de uma propriedade")
    @GetMapping("/historico/{propriedadeId}")
    public ResponseEntity<List<LeituraClimaResponse>> getHistorico(
            @PathVariable Long propriedadeId,
            @RequestParam(defaultValue = "7") int dias) {
        
        Optional<DispositivoIot> dispositivoOpt = dispositivoIotRepository.findByPropriedadeId(propriedadeId);
        if (dispositivoOpt.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        LocalDateTime dataCorte = LocalDateTime.now().minusDays(dias);
        var leituras = leituraClimaRepository.findByDispositivoIotIdAndDataLeituraAfterOrderByDataLeituraDesc(dispositivoOpt.get().getId(), dataCorte);

        var responseList = leituras.stream()
                .map(l -> LeituraClimaResponse.builder()
                        .id(l.getId())
                        .dispositivoIotId(l.getDispositivoIot().getId())
                        .umidadeAr(l.getUmidadeAr())
                        .temperatura(l.getTemperatura())
                        .dataLeitura(l.getDataLeitura())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }
}
