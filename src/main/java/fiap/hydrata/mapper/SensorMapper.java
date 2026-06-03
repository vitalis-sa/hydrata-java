package fiap.hydrata.mapper;

import fiap.hydrata.dto.request.SensorRequest;
import fiap.hydrata.dto.response.SensorResponse;
import fiap.hydrata.entity.Sensor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SensorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "propriedade", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "status", ignore = true)
    Sensor toEntity(SensorRequest request);

    @Mapping(source = "propriedade.nome", target = "nomePropriedade")
    SensorResponse toResponse(Sensor sensor);

    List<SensorResponse> toResponseList(List<Sensor> list);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "propriedade", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateEntity(SensorRequest request, @MappingTarget Sensor sensor);
}
