package fiap.hydrata.controller;

import fiap.hydrata.dto.request.LeituraRequest;
import fiap.hydrata.dto.response.DeleteResponse;
import fiap.hydrata.dto.response.LeituraResponse;
import fiap.hydrata.service.LeituraService;
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
@RequestMapping("/api/leituras")
@RequiredArgsConstructor
@Tag(name = "Leituras", description = "Leituras dos sensores IoT (DHT22 e LDR)")
public class LeiturasController {

    private final LeituraService service;

    @Operation(summary = "Listar todas as leituras")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso") })
    @GetMapping
    public ResponseEntity<List<LeituraResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Buscar leitura por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Leitura encontrada"),
        @ApiResponse(responseCode = "404", description = "Leitura não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<LeituraResponse>> findById(@PathVariable Long id) {
        LeituraResponse response = service.findById(id);
        EntityModel<LeituraResponse> model = EntityModel.of(response,
                linkTo(methodOn(LeiturasController.class).findById(id)).withSelfRel(),
                linkTo(methodOn(LeiturasController.class).findAll()).withRel("leituras")
        );
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Registrar leitura")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Leitura registrada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Sensor não encontrado")
    })
    @PostMapping
    public ResponseEntity<LeituraResponse> create(@Valid @RequestBody LeituraRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @Operation(summary = "Atualizar leitura")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Leitura não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<LeituraResponse> update(@PathVariable Long id, @Valid @RequestBody LeituraRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Remover leitura")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Leitura não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> delete(@PathVariable Long id) {
        return ResponseEntity.ok(service.delete(id));
    }
}
