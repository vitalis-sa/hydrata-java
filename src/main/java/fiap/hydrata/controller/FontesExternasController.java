package fiap.hydrata.controller;

import fiap.hydrata.dto.request.FonteExternaRequest;
import fiap.hydrata.dto.response.DeleteResponse;
import fiap.hydrata.dto.response.FonteExternaResponse;
import fiap.hydrata.service.FonteExternaService;
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
@RequestMapping("/api/fontes-externas")
@RequiredArgsConstructor
@Tag(name = "Fontes Externas", description = "Gerenciamento de fontes externas de dados (APIs, satélites, IoT)")
public class FontesExternasController {

    private final FonteExternaService service;

    @Operation(summary = "Listar todas as fontes externas")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso") })
    @GetMapping
    public ResponseEntity<List<FonteExternaResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Buscar fonte externa por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Fonte encontrada"),
        @ApiResponse(responseCode = "404", description = "Fonte não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<FonteExternaResponse>> findById(@PathVariable Long id) {
        FonteExternaResponse response = service.findById(id);
        EntityModel<FonteExternaResponse> model = EntityModel.of(response,
                linkTo(methodOn(FontesExternasController.class).findById(id)).withSelfRel(),
                linkTo(methodOn(FontesExternasController.class).findAll()).withRel("fontes-externas")
        );
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Cadastrar fonte externa")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Fonte criada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<FonteExternaResponse> create(@Valid @RequestBody FonteExternaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @Operation(summary = "Atualizar fonte externa")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Fonte não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<FonteExternaResponse> update(@PathVariable Long id, @Valid @RequestBody FonteExternaRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Remover fonte externa")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Fonte não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> delete(@PathVariable Long id) {
        return ResponseEntity.ok(service.delete(id));
    }
}
