package fiap.hydrata.mapper;

import fiap.hydrata.dto.request.FonteExternaRequest;
import fiap.hydrata.dto.response.FonteExternaResponse;
import fiap.hydrata.entity.FonteExterna;
import fiap.hydrata.entity.FonteExternaApi;
import fiap.hydrata.entity.FonteExternaSatelital;
import fiap.hydrata.entity.FonteExternaIot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FonteExternaMapper {

    FonteExternaResponse toResponse(FonteExterna fonte);

    List<FonteExternaResponse> toResponseList(List<FonteExterna> list);
}
