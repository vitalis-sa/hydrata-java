package fiap.hydrata.mapper;

import fiap.hydrata.dto.request.DispositivoIotRequest;
import fiap.hydrata.dto.response.DispositivoIotResponse;
import fiap.hydrata.entity.DispositivoIot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DispositivoIotMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "propriedade", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "status", ignore = true)
    DispositivoIot toEntity(DispositivoIotRequest request);

    @Mapping(source = "propriedade.nome", target = "nomePropriedade")
    DispositivoIotResponse toResponse(DispositivoIot dispositivoIot);

    List<DispositivoIotResponse> toResponseList(List<DispositivoIot> list);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "propriedade", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateEntity(DispositivoIotRequest request, @MappingTarget DispositivoIot dispositivoIot);
}
