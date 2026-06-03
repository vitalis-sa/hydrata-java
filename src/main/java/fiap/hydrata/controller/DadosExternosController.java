package fiap.hydrata.controller;

import fiap.hydrata.dto.request.DadoExternoRequest;
import fiap.hydrata.dto.response.DadoExternoResponse;
import fiap.hydrata.dto.response.DeleteResponse;
import fiap.hydrata.service.DadoExternoService;
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
@RequestMapping("/api/dados-externos")
@RequiredArgsConstructor
@Tag(name = "Dados Externos", description = "Dados coletados de fontes externas")
public class DadosExternosController {

    private final DadoExternoService service;

    @Operation(summary = "Listar todos os dados externos")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso") })
    @GetMapping
    public ResponseEntity<List<DadoExternoResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Buscar dado externo por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Dado encontrado"),
        @ApiResponse(responseCode = "404", description = "Dado não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<DadoExternoResponse>> findById(@PathVariable Long id) {
        DadoExternoResponse response = service.findById(id);
        EntityModel<DadoExternoResponse> model = EntityModel.of(response,
                linkTo(methodOn(DadosExternosController.class).findById(id)).withSelfRel(),
                linkTo(methodOn(DadosExternosController.class).findAll()).withRel("dados-externos")
        );
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Registrar dado externo")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Dado registrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Fonte externa não encontrada")
    })
    @PostMapping
    public ResponseEntity<DadoExternoResponse> create(@Valid @RequestBody DadoExternoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @Operation(summary = "Atualizar dado externo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Dado não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DadoExternoResponse> update(@PathVariable Long id, @Valid @RequestBody DadoExternoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Remover dado externo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Dado não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> delete(@PathVariable Long id) {
        return ResponseEntity.ok(service.delete(id));
    }
}
