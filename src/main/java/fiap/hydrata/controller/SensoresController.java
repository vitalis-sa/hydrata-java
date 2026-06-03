package fiap.hydrata.controller;

import fiap.hydrata.dto.request.SensorRequest;
import fiap.hydrata.dto.response.DeleteResponse;
import fiap.hydrata.dto.response.SensorResponse;
import fiap.hydrata.service.SensorService;
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
@RequestMapping("/api/sensores")
@RequiredArgsConstructor
@Tag(name = "Sensores", description = "Gerenciamento de sensores IoT das propriedades")
public class SensoresController {

    private final SensorService service;

    @Operation(summary = "Listar todos os sensores")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso") })
    @GetMapping
    public ResponseEntity<List<SensorResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Buscar sensor por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sensor encontrado"),
        @ApiResponse(responseCode = "404", description = "Sensor não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<SensorResponse>> findById(@PathVariable Long id) {
        SensorResponse response = service.findById(id);
        EntityModel<SensorResponse> model = EntityModel.of(response,
                linkTo(methodOn(SensoresController.class).findById(id)).withSelfRel(),
                linkTo(methodOn(SensoresController.class).findAll()).withRel("sensores")
        );
        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Cadastrar sensor")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Sensor criado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Propriedade não encontrada")
    })
    @PostMapping
    public ResponseEntity<SensorResponse> create(@Valid @RequestBody SensorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @Operation(summary = "Atualizar sensor")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Sensor não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SensorResponse> update(@PathVariable Long id, @Valid @RequestBody SensorRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Remover sensor")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Sensor não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> delete(@PathVariable Long id) {
        return ResponseEntity.ok(service.delete(id));
    }
}
