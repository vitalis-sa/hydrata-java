package fiap.hydrata.mapper;

import fiap.hydrata.dto.request.AlertaRequest;
import fiap.hydrata.dto.response.AlertaResponse;
import fiap.hydrata.entity.Alerta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AlertaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "propriedade", ignore = true)
    @Mapping(target = "leitura", ignore = true)
    @Mapping(target = "dadoExterno", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "dataGeracao", ignore = true)
    @Mapping(target = "dataResolucao", ignore = true)
    Alerta toEntity(AlertaRequest request);

    @Mapping(source = "propriedade.nome", target = "nomePropriedade")
    @Mapping(source = "leitura.id", target = "leituraId")
    @Mapping(source = "dadoExterno.id", target = "dadoExternoId")
    AlertaResponse toResponse(Alerta alerta);

    List<AlertaResponse> toResponseList(List<Alerta> list);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "propriedade", ignore = true)
    @Mapping(target = "leitura", ignore = true)
    @Mapping(target = "dadoExterno", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "dataGeracao", ignore = true)
    @Mapping(target = "dataResolucao", ignore = true)
    void updateEntity(AlertaRequest request, @MappingTarget Alerta alerta);
}
