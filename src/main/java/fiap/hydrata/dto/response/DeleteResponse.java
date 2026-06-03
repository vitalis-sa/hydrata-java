package fiap.hydrata.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteResponse {

    @Schema(description = "Mensagem de confirmação", example = "Sensor com id 1 removido com sucesso")
    private String message;

    public static DeleteResponse of(String entity, Long id) {
        return DeleteResponse.builder()
                .message(entity + " com id " + id + " removido com sucesso")
                .build();
    }
}
