package fiap.hydrata.service;

import fiap.hydrata.dto.request.DispositivoIotRequest;
import fiap.hydrata.dto.response.DeleteResponse;
import fiap.hydrata.dto.response.DispositivoIotResponse;
import fiap.hydrata.entity.DispositivoIot;
import fiap.hydrata.enums.StatusSensor;
import fiap.hydrata.exception.ResourceNotFoundException;
import fiap.hydrata.mapper.DispositivoIotMapper;
import fiap.hydrata.repository.PropriedadeRepository;
import fiap.hydrata.repository.DispositivoIotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DispositivoIotService {

    private final DispositivoIotRepository repository;
    private final DispositivoIotMapper mapper;
    private final PropriedadeRepository propriedadeRepository;

    public List<DispositivoIotResponse> findAll() {
        return mapper.toResponseList(repository.findAll());
    }

    public DispositivoIotResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo IoT não encontrado com id: " + id));
    }

    public List<DispositivoIot> findDispositivosIotAtivos() {
        return repository.findByStatus(StatusSensor.ATIVO);
    }

    @Transactional
    public DispositivoIotResponse create(DispositivoIotRequest request) {
        DispositivoIot entity = mapper.toEntity(request);
        entity.setPropriedade(propriedadeRepository.findById(request.getPropriedadeId())
                .orElseThrow(() -> new ResourceNotFoundException("Propriedade não encontrada com id: " + request.getPropriedadeId())));
        entity.setStatus(StatusSensor.ATIVO);
        entity.setDataCadastro(LocalDateTime.now());
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public DispositivoIotResponse update(Long id, DispositivoIotRequest request) {
        DispositivoIot entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dispositivo IoT não encontrado com id: " + id));
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
                        () -> { throw new ResourceNotFoundException("Dispositivo IoT não encontrado com id: " + id); }
                );
        return DeleteResponse.of("DispositivoIot", id);
    }
}
