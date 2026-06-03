package fiap.hydrata.service;

import fiap.hydrata.dto.request.PropriedadeRequest;
import fiap.hydrata.dto.response.DeleteResponse;
import fiap.hydrata.dto.response.PropriedadeResponse;
import fiap.hydrata.entity.Propriedade;
import fiap.hydrata.enums.StatusGeral;
import fiap.hydrata.exception.ResourceNotFoundException;
import fiap.hydrata.mapper.PropriedadeMapper;
import fiap.hydrata.repository.PlanoRepository;
import fiap.hydrata.repository.PropriedadeRepository;
import fiap.hydrata.repository.ProdutorRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PropriedadeService {

    private final PropriedadeRepository repository;
    private final PropriedadeMapper mapper;
    private final ProdutorRepository produtorRepository;
    private final PlanoRepository planoRepository;

    public List<PropriedadeResponse> findAll() {
        return mapper.toResponseList(repository.findAll());
    }

    public PropriedadeResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Propriedade não encontrada com id: " + id));
    }

    @Transactional
    public PropriedadeResponse create(PropriedadeRequest request) {
        Propriedade entity = mapper.toEntity(request);
        entity.setProdutor(produtorRepository.findById(request.getProdutorId())
                .orElseThrow(() -> new ResourceNotFoundException("Produtor não encontrado com id: " + request.getProdutorId())));
        entity.setPlano(planoRepository.findById(request.getPlanoId())
                .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado com id: " + request.getPlanoId())));
        entity.setStatus(StatusGeral.ATIVO);
        entity.setDataCadastro(LocalDateTime.now());
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public PropriedadeResponse update(Long id, PropriedadeRequest request) {
        Propriedade entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Propriedade não encontrada com id: " + id));
        mapper.updateEntity(request, entity);
        entity.setProdutor(produtorRepository.findById(request.getProdutorId())
                .orElseThrow(() -> new ResourceNotFoundException("Produtor não encontrado com id: " + request.getProdutorId())));
        entity.setPlano(planoRepository.findById(request.getPlanoId())
                .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado com id: " + request.getPlanoId())));
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public DeleteResponse delete(Long id) {
        repository.findById(id)
                .ifPresentOrElse(
                        repository::delete,
                        () -> { throw new ResourceNotFoundException("Propriedade não encontrada com id: " + id); }
                );
        return DeleteResponse.of("Propriedade", id);
    }
}
