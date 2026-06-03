package fiap.hydrata.mapper;

import fiap.hydrata.dto.request.DadoExternoRequest;
import fiap.hydrata.dto.response.DadoExternoResponse;
import fiap.hydrata.entity.DadoExterno;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DadoExternoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fonteExterna", ignore = true)
    @Mapping(target = "dataColeta", ignore = true)
    DadoExterno toEntity(DadoExternoRequest request);

    @Mapping(source = "fonteExterna.id", target = "fonteExternaId")
    @Mapping(source = "fonteExterna.nome", target = "nomeFonteExterna")
    DadoExternoResponse toResponse(DadoExterno dadoExterno);

    List<DadoExternoResponse> toResponseList(List<DadoExterno> list);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fonteExterna", ignore = true)
    @Mapping(target = "dataColeta", ignore = true)
    void updateEntity(DadoExternoRequest request, @MappingTarget DadoExterno dadoExterno);
}
