package fiap.hydrata.service;

import fiap.hydrata.client.OpenMeteoClient;
import fiap.hydrata.dto.response.DashboardResponse;
import fiap.hydrata.entity.Propriedade;
import fiap.hydrata.exception.ResourceNotFoundException;
import fiap.hydrata.mapper.AlertaMapper;
import fiap.hydrata.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final PropriedadeRepository propriedadeRepository;
    private final DispositivoIotRepository dispositivoIotRepository;
    private final AlertaRepository alertaRepository;
    private final LeituraClimaRepository leituraClimaRepository;
    private final DadoExternoRepository dadoExternoRepository;
    private final OpenMeteoClient openMeteoClient;
    private final AlertaMapper alertaMapper;

    public DashboardResponse getDashboard(Long propriedadeId) {
        Propriedade propriedade = propriedadeRepository.findById(propriedadeId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Propriedade não encontrada com id: " + propriedadeId));

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
                    builder.nivelRioStatus(d.getValor().doubleValue() < 3.0 ? "BAIXO"
                            : (d.getValor().doubleValue() > 5.0 ? "ALTO" : "NORMAL"));
                });

        if (propriedade.getCoordenadas() != null) {
            try {
                var meteo = openMeteoClient.buscarClima(
                        propriedade.getCoordenadas().getLatitude(),
                        propriedade.getCoordenadas().getLongitude());
                if (meteo != null && meteo.getCurrent() != null) {
                    builder.temperaturaMax(BigDecimal.valueOf(meteo.getCurrent().getTemperatura()));
                    builder.previsaoChuva(
                            meteo.getCurrent().getPrecipitacao() != null && meteo.getCurrent().getPrecipitacao() > 0);
                }
            } catch (Exception e) {
                // Falha na API externa de clima não deve quebrar o dashboard
            }
        }

        return builder.build();
    }
}
