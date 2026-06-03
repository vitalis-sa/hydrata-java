package fiap.hydrata.controller;

import fiap.hydrata.dto.request.PropriedadeRequest;
import fiap.hydrata.dto.response.DeleteResponse;
import fiap.hydrata.dto.response.PropriedadeResponse;
import fiap.hydrata.service.PropriedadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/propriedades")
@RequiredArgsConstructor
@Tag(name = "Propriedades", description = "Gerenciamento de propriedades rurais")
public class PropriedadesController {

    private final PropriedadeService service;

    @Operation(summary = "Listar todas as propriedades")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso") })
    @GetMapping
    public ResponseEntity<List<PropriedadeResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Buscar propriedade por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Propriedade encontrada"),
        @ApiResponse(responseCode = "404", description = "Propriedade não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PropriedadeResponse>> findById(@PathVariable Long id) {
        PropriedadeResponse response = service.findById(id);
        EntityModel<PropriedadeResponse> model = EntityModel.of(response,
                linkTo(methodOn(PropriedadesController.class).findById(id)).withSelfRel(),
                linkTo(methodOn(PropriedadesController.class).findAll()).withRel("propriedades")
        );
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Cadastrar propriedade")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Propriedade criada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Produtor ou plano não encontrado")
    })
    @PostMapping
    public ResponseEntity<PropriedadeResponse> create(@Valid @RequestBody PropriedadeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @Operation(summary = "Atualizar propriedade")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Propriedade não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PropriedadeResponse> update(@PathVariable Long id, @Valid @RequestBody PropriedadeRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Remover propriedade")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Propriedade não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> delete(@PathVariable Long id) {
        return ResponseEntity.ok(service.delete(id));
    }
}
