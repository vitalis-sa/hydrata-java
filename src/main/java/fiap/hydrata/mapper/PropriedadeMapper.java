package fiap.hydrata.mapper;

import fiap.hydrata.dto.request.PropriedadeRequest;
import fiap.hydrata.dto.response.PropriedadeResponse;
import fiap.hydrata.entity.Propriedade;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PropriedadeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produtor", ignore = true)
    @Mapping(target = "plano", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "coordenadas.latitude", source = "latitude")
    @Mapping(target = "coordenadas.longitude", source = "longitude")
    @Mapping(target = "coordenadas.cidade", source = "cidade")
    @Mapping(target = "coordenadas.estado", source = "estado")
    Propriedade toEntity(PropriedadeRequest request);

    @Mapping(source = "produtor.nome", target = "nomeProdutor")
    @Mapping(source = "produtor.id", target = "produtorId")
    @Mapping(source = "plano.nome", target = "nomePlano")
    @Mapping(source = "coordenadas.latitude", target = "latitude")
    @Mapping(source = "coordenadas.longitude", target = "longitude")
    @Mapping(source = "coordenadas.cidade", target = "cidade")
    @Mapping(source = "coordenadas.estado", target = "estado")
    @Mapping(source = "status", target = "status")
    PropriedadeResponse toResponse(Propriedade propriedade);

    List<PropriedadeResponse> toResponseList(List<Propriedade> list);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produtor", ignore = true)
    @Mapping(target = "plano", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "coordenadas.latitude", source = "latitude")
    @Mapping(target = "coordenadas.longitude", source = "longitude")
    @Mapping(target = "coordenadas.cidade", source = "cidade")
    @Mapping(target = "coordenadas.estado", source = "estado")
    void updateEntity(PropriedadeRequest request, @MappingTarget Propriedade propriedade);
}
