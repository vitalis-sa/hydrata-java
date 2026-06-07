package fiap.hydrata.controller;

import fiap.hydrata.dto.request.AlertaRequest;
import fiap.hydrata.dto.response.AlertaResponse;
import fiap.hydrata.dto.response.DeleteResponse;
import fiap.hydrata.service.AlertaService;
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
@RequestMapping("/api/alertas")
@RequiredArgsConstructor
@Tag(name = "Alertas", description = "Alertas gerados pelo rule engine de irrigação")
public class AlertasController {

    private final AlertaService service;

    @Operation(summary = "Listar todos os alertas")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso") })
    @GetMapping
    public ResponseEntity<List<AlertaResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Listar alertas de uma propriedade (com filtro opcional por tipo)")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso") })
    @GetMapping("/propriedade/{propriedadeId}")
    public ResponseEntity<List<AlertaResponse>> findByPropriedade(
            @PathVariable Long propriedadeId, 
            @RequestParam(required = false) String tipo) {
        return ResponseEntity.ok(service.findByPropriedade(propriedadeId, tipo));
    }

    @Operation(summary = "Buscar alerta por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Alerta encontrado"),
        @ApiResponse(responseCode = "404", description = "Alerta não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<AlertaResponse>> findById(@PathVariable Long id) {
        AlertaResponse response = service.findById(id);
        EntityModel<AlertaResponse> model = EntityModel.of(response,
                linkTo(methodOn(AlertasController.class).findById(id)).withSelfRel(),
                linkTo(methodOn(AlertasController.class).findAll()).withRel("alertas")
        );
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Criar alerta manualmente")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Alerta criado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Propriedade não encontrada")
    })
    @PostMapping
    public ResponseEntity<AlertaResponse> create(@Valid @RequestBody AlertaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @Operation(summary = "Atualizar alerta")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Alerta não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AlertaResponse> update(@PathVariable Long id, @Valid @RequestBody AlertaRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Remover alerta")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Alerta não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> delete(@PathVariable Long id) {
        return ResponseEntity.ok(service.delete(id));
    }
}
