package fiap.hydrata.service;

import fiap.hydrata.dto.request.LeituraRequest;
import fiap.hydrata.dto.response.DeleteResponse;
import fiap.hydrata.dto.response.LeituraResponse;
import fiap.hydrata.entity.Leitura;
import fiap.hydrata.exception.ResourceNotFoundException;
import fiap.hydrata.mapper.LeituraMapper;
import fiap.hydrata.mqtt.payload.ClimaPayload;
import fiap.hydrata.mqtt.payload.LuzPayload;
import fiap.hydrata.repository.LeituraRepository;
import fiap.hydrata.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class LeituraService {

    private final LeituraRepository repository;
    private final LeituraMapper mapper;
    private final SensorRepository sensorRepository;

    public List<LeituraResponse> findAll() {
        return mapper.toResponseList(repository.findAll());
    }

    public LeituraResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Leitura não encontrada com id: " + id));
    }

    @Transactional
    public LeituraResponse create(LeituraRequest request) {
        Leitura entity = mapper.toEntity(request);
        entity.setSensor(sensorRepository.findById(request.getSensorId())
                .orElseThrow(() -> new ResourceNotFoundException("Sensor não encontrado com id: " + request.getSensorId())));
        entity.setDataLeitura(LocalDateTime.now());
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public LeituraResponse update(Long id, LeituraRequest request) {
        Leitura entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leitura não encontrada com id: " + id));
        mapper.updateEntity(request, entity);
        entity.setSensor(sensorRepository.findById(request.getSensorId())
                .orElseThrow(() -> new ResourceNotFoundException("Sensor não encontrado com id: " + request.getSensorId())));
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public DeleteResponse delete(Long id) {
        repository.findById(id)
                .ifPresentOrElse(
                        repository::delete,
                        () -> { throw new ResourceNotFoundException("Leitura não encontrada com id: " + id); }
                );
        return DeleteResponse.of("Leitura", id);
    }

    @Transactional
    public void salvarDeClima(ClimaPayload clima) {
        log.info("[DEBUG-MQTT] Tentando salvar dados de CLIMA: {}", clima);
        var sensores = sensorRepository.findAll();
        if (sensores.isEmpty()) {
            log.warn("[DEBUG-MQTT] NENHUM SENSOR CADASTRADO NO BANCO! Ignorando a leitura de clima.");
            return;
        }
        
        var sensor = sensores.get(0);
        log.info("[DEBUG-MQTT] Associando leitura de clima ao sensor ID: {}", sensor.getId());
        
        Leitura leitura = Leitura.builder()
                .sensor(sensor)
                .temperatura(clima.temperatura() != null ? BigDecimal.valueOf(clima.temperatura()) : null)
                .umidadeAr(clima.umidadeAr() != null ? BigDecimal.valueOf(clima.umidadeAr()) : BigDecimal.ZERO)
                .dataLeitura(LocalDateTime.now())
                .build();
        repository.save(leitura);
        log.info("[DEBUG-MQTT] Leitura de clima persistida com SUCESSO no banco!");
    }

    @Transactional
    public void salvarDeLuz(LuzPayload luz) {
        log.info("[DEBUG-MQTT] Tentando salvar dados de LUZ: {}", luz);
        var sensores = sensorRepository.findAll();
        if (sensores.isEmpty()) {
            log.warn("[DEBUG-MQTT] NENHUM SENSOR CADASTRADO NO BANCO! Ignorando a leitura de luz.");
            return;
        }
        
        var sensor = sensores.get(0);
        log.info("[DEBUG-MQTT] Associando leitura de luz ao sensor ID: {}", sensor.getId());
        
        Leitura leitura = Leitura.builder()
                .sensor(sensor)
                .umidadeAr(BigDecimal.ZERO)
                .luminosidade(luz.luminosidade() != null ? BigDecimal.valueOf(luz.luminosidade()) : null)
                .dataLeitura(LocalDateTime.now())
                .build();
        repository.save(leitura);
        log.info("[DEBUG-MQTT] Leitura de luz persistida com SUCESSO no banco!");
    }
}
