package fiap.hydrata.service;

import fiap.hydrata.dto.request.SensorRequest;
import fiap.hydrata.dto.response.DeleteResponse;
import fiap.hydrata.dto.response.SensorResponse;
import fiap.hydrata.entity.Sensor;
import fiap.hydrata.enums.StatusSensor;
import fiap.hydrata.exception.ResourceNotFoundException;
import fiap.hydrata.mapper.SensorMapper;
import fiap.hydrata.repository.PropriedadeRepository;
import fiap.hydrata.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorService {

    private final SensorRepository repository;
    private final SensorMapper mapper;
    private final PropriedadeRepository propriedadeRepository;

    public List<SensorResponse> findAll() {
        return mapper.toResponseList(repository.findAll());
    }

    public SensorResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Sensor não encontrado com id: " + id));
    }

    public List<Sensor> findSensoresAtivos() {
        return repository.findByStatus(StatusSensor.ATIVO);
    }

    @Transactional
    public SensorResponse create(SensorRequest request) {
        Sensor entity = mapper.toEntity(request);
        entity.setPropriedade(propriedadeRepository.findById(request.getPropriedadeId())
                .orElseThrow(() -> new ResourceNotFoundException("Propriedade não encontrada com id: " + request.getPropriedadeId())));
        entity.setStatus(StatusSensor.ATIVO);
        entity.setDataCadastro(LocalDateTime.now());
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public SensorResponse update(Long id, SensorRequest request) {
        Sensor entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sensor não encontrado com id: " + id));
        mapper.updateEntity(request, entity);
        entity.setPropriedade(propriedadeRepository.findById(request.getPropriedadeId())
                .orElseThrow(() -> new ResourceNotFoundException("Propriedade não encontrada com id: " + request.getPropriedadeId())));
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public DeleteResponse delete(Long id) {
        repository.findById(id)
                .ifPresentOrElse(
                        repository::delete,
                        () -> { throw new ResourceNotFoundException("Sensor não encontrado com id: " + id); }
                );
        return DeleteResponse.of("Sensor", id);
    }
}
