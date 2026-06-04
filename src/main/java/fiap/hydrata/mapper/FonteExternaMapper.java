package fiap.hydrata.mapper;

import fiap.hydrata.dto.request.FonteExternaRequest;
import fiap.hydrata.dto.response.FonteExternaResponse;
import fiap.hydrata.entity.FonteExterna;
import fiap.hydrata.entity.FonteExternaApi;
import fiap.hydrata.entity.FonteExternaSatelital;
import fiap.hydrata.entity.FonteExternaIot;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FonteExternaMapper {

    @Mapping(target = "tipo", ignore = true)
    FonteExternaResponse toResponse(FonteExterna fonte);

    List<FonteExternaResponse> toResponseList(List<FonteExterna> list);

    @AfterMapping
    default void setTipo(FonteExterna fonte, @MappingTarget FonteExternaResponse.FonteExternaResponseBuilder response) {
        if (fonte instanceof FonteExternaApi) {
            response.tipo("API_CLIMA");
        } else if (fonte instanceof FonteExternaSatelital) {
            response.tipo("SATELITE");
        } else if (fonte instanceof FonteExternaIot) {
            response.tipo("IOT");
        }
    }
}
