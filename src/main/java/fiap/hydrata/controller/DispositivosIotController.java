package fiap.hydrata.controller;

import fiap.hydrata.dto.request.DispositivoIotRequest;
import fiap.hydrata.dto.response.DeleteResponse;
import fiap.hydrata.dto.response.DispositivoIotResponse;
import fiap.hydrata.service.DispositivoIotService;
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
@RequestMapping("/api/dispositivos-iot")
@RequiredArgsConstructor
@Tag(name = "Dispositivos IoT", description = "Gerenciamento de Dispositivos IoT das propriedades")
public class DispositivosIotController {

    private final DispositivoIotService service;

    @Operation(summary = "Listar todos os dispositivos")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso") })
    @GetMapping
    public ResponseEntity<List<DispositivoIotResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Buscar dispositivo por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Dispositivo encontrado"),
        @ApiResponse(responseCode = "404", description = "Dispositivo não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<DispositivoIotResponse>> findById(@PathVariable Long id) {
        DispositivoIotResponse response = service.findById(id);
        EntityModel<DispositivoIotResponse> model = EntityModel.of(response,
                linkTo(methodOn(DispositivosIotController.class).findById(id)).withSelfRel(),
                linkTo(methodOn(DispositivosIotController.class).findAll()).withRel("dispositivosIot")
        );
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Cadastrar dispositivo")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Dispositivo criado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Propriedade não encontrada")
    })
    @PostMapping
    public ResponseEntity<DispositivoIotResponse> create(@Valid @RequestBody DispositivoIotRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @Operation(summary = "Atualizar dispositivo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Dispositivo não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DispositivoIotResponse> update(@PathVariable Long id, @Valid @RequestBody DispositivoIotRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Remover dispositivo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Dispositivo não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> delete(@PathVariable Long id) {
        return ResponseEntity.ok(service.delete(id));
    }
}
