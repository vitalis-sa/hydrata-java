package fiap.hydrata.mapper;

import fiap.hydrata.dto.request.LeituraRequest;
import fiap.hydrata.dto.response.LeituraResponse;
import fiap.hydrata.entity.Leitura;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LeituraMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sensor", ignore = true)
    @Mapping(target = "dataLeitura", ignore = true)
    Leitura toEntity(LeituraRequest request);

    @Mapping(source = "sensor.id", target = "sensorId")
    @Mapping(source = "sensor.macAddress", target = "macAddressSensor")
    LeituraResponse toResponse(Leitura leitura);

    List<LeituraResponse> toResponseList(List<Leitura> list);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sensor", ignore = true)
    @Mapping(target = "dataLeitura", ignore = true)
    void updateEntity(LeituraRequest request, @MappingTarget Leitura leitura);
}
