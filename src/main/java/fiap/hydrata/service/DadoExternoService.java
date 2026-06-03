package fiap.hydrata.service;

import fiap.hydrata.dto.request.DadoExternoRequest;
import fiap.hydrata.dto.response.DadoExternoResponse;
import fiap.hydrata.dto.response.DeleteResponse;
import fiap.hydrata.entity.DadoExterno;
import fiap.hydrata.exception.ResourceNotFoundException;
import fiap.hydrata.mapper.DadoExternoMapper;
import fiap.hydrata.repository.DadoExternoRepository;
import fiap.hydrata.repository.FonteExternaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DadoExternoService {

    private final DadoExternoRepository repository;
    private final DadoExternoMapper mapper;
    private final FonteExternaRepository fonteExternaRepository;

    public List<DadoExternoResponse> findAll() {
        return mapper.toResponseList(repository.findAll());
    }

    public DadoExternoResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Dado externo não encontrado com id: " + id));
    }

    @Transactional
    public DadoExternoResponse create(DadoExternoRequest request) {
        DadoExterno entity = mapper.toEntity(request);
        entity.setFonteExterna(fonteExternaRepository.findById(request.getFonteExternaId())
                .orElseThrow(() -> new ResourceNotFoundException("Fonte externa não encontrada com id: " + request.getFonteExternaId())));
        entity.setDataColeta(LocalDateTime.now());
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public DadoExternoResponse update(Long id, DadoExternoRequest request) {
        DadoExterno entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dado externo não encontrado com id: " + id));
        mapper.updateEntity(request, entity);
        entity.setFonteExterna(fonteExternaRepository.findById(request.getFonteExternaId())
                .orElseThrow(() -> new ResourceNotFoundException("Fonte externa não encontrada com id: " + request.getFonteExternaId())));
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public DeleteResponse delete(Long id) {
        repository.findById(id)
                .ifPresentOrElse(
                        repository::delete,
                        () -> { throw new ResourceNotFoundException("Dado externo não encontrado com id: " + id); }
                );
        return DeleteResponse.of("DadoExterno", id);
    }
}
