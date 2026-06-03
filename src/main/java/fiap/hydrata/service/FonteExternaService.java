package fiap.hydrata.service;

import fiap.hydrata.dto.request.FonteExternaRequest;
import fiap.hydrata.dto.response.DeleteResponse;
import fiap.hydrata.dto.response.FonteExternaResponse;
import fiap.hydrata.entity.FonteExterna;
import fiap.hydrata.entity.FonteExternaApi;
import fiap.hydrata.entity.FonteExternaIot;
import fiap.hydrata.entity.FonteExternaSatelital;
import fiap.hydrata.exception.BusinessException;
import fiap.hydrata.exception.ResourceNotFoundException;
import fiap.hydrata.mapper.FonteExternaMapper;
import fiap.hydrata.repository.FonteExternaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FonteExternaService {

    private final FonteExternaRepository repository;
    private final FonteExternaMapper mapper;

    public List<FonteExternaResponse> findAll() {
        return mapper.toResponseList(repository.findAll());
    }

    public FonteExternaResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Fonte externa não encontrada com id: " + id));
    }

    @Transactional
    public FonteExternaResponse create(FonteExternaRequest request) {
        FonteExterna entity = buildByTipo(request.getTipo());
        entity.setNome(request.getNome());
        entity.setStatus("ATIVO");
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public FonteExternaResponse update(Long id, FonteExternaRequest request) {
        FonteExterna entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fonte externa não encontrada com id: " + id));
        entity.setNome(request.getNome());
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public DeleteResponse delete(Long id) {
        repository.findById(id)
                .ifPresentOrElse(
                        repository::delete,
                        () -> { throw new ResourceNotFoundException("Fonte externa não encontrada com id: " + id); }
                );
        return DeleteResponse.of("FonteExterna", id);
    }

    private FonteExterna buildByTipo(String tipo) {
        return switch (tipo.toUpperCase()) {
            case "SATELITE" -> new FonteExternaSatelital();
            case "API_CLIMA" -> new FonteExternaApi();
            case "IOT" -> new FonteExternaIot();
            default -> throw new BusinessException("Tipo de fonte externa inválido: " + tipo);
        };
    }
}
