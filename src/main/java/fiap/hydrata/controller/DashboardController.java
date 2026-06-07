package fiap.hydrata.controller;

import fiap.hydrata.client.OpenMeteoClient;
import fiap.hydrata.dto.response.DashboardResponse;
import fiap.hydrata.entity.DispositivoIot;
import fiap.hydrata.entity.Propriedade;
import fiap.hydrata.mapper.AlertaMapper;
import fiap.hydrata.repository.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "BFF para a tela principal do aplicativo")
public class DashboardController {

    private final PropriedadeRepository propriedadeRepository;
    private final DispositivoIotRepository dispositivoIotRepository;
    private final AlertaRepository alertaRepository;
    private final LeituraClimaRepository leituraClimaRepository;
    private final DadoExternoRepository dadoExternoRepository;
    private final OpenMeteoClient openMeteoClient;
    private final AlertaMapper alertaMapper;

    @Operation(summary = "Obter dados agregados para o dashboard")
    @GetMapping("/{propriedadeId}")
    public ResponseEntity<DashboardResponse> getDashboard(@PathVariable Long propriedadeId) {
        Propriedade propriedade = propriedadeRepository.findById(propriedadeId).orElse(null);
        if (propriedade == null) return ResponseEntity.notFound().build();

        var builder = DashboardResponse.builder();

        alertaRepository.findFirstByPropriedadeIdOrderByIdDesc(propriedadeId)
                .ifPresent(a -> builder.ultimaDecisao(alertaMapper.toResponse(a)));

        dispositivoIotRepository.findByPropriedadeId(propriedadeId).ifPresent(disp -> {
            leituraClimaRepository.findFirstByDispositivoIotIdOrderByIdDesc(disp.getId())
                    .ifPresent(clima -> {
                        builder.umidadeAr(clima.getUmidadeAr());
                        builder.ultimaLeitura(clima.getDataLeitura());
                    });
        });

        dadoExternoRepository.findFirstByTipoOrderByIdDesc("FOCOS_QUEIMADA")
                .ifPresent(d -> builder.focosQueimada(d.getValor().intValue()));

        dadoExternoRepository.findFirstByTipoOrderByIdDesc("NIVEL_RIO")
                .ifPresent(d -> {
                    builder.nomeRio("Rio Monitorado (ANA)");
                    builder.nivelRioStatus(d.getValor().doubleValue() < 3.0 ? "BAIXO" : 
                                         (d.getValor().doubleValue() > 5.0 ? "ALTO" : "NORMAL"));
                });

        if (propriedade.getCoordenadas() != null) {
            try {
                var meteo = openMeteoClient.buscarClima(
                        propriedade.getCoordenadas().getLatitude(), 
                        propriedade.getCoordenadas().getLongitude());
                if (meteo != null && meteo.getCurrent() != null) {
                    builder.temperaturaMax(java.math.BigDecimal.valueOf(meteo.getCurrent().getTemperatura()));
                    builder.previsaoChuva(meteo.getCurrent().getPrecipitacao() != null && meteo.getCurrent().getPrecipitacao() > 0);
                }
            } catch (Exception e) {
                // Falha na API externa de clima não deve quebrar o dashboard
            }
        }

        return ResponseEntity.ok(builder.build());
    }
}
