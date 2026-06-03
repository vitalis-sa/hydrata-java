package fiap.hydrata.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertaResponse {

    @Schema(description = "ID do alerta", example = "1")
    private Long id;

    @Schema(description = "Nome da propriedade", example = "Fazenda Boa Vista")
    private String nomePropriedade;

    @Schema(description = "ID da leitura relacionada", example = "5")
    private Long leituraId;

    @Schema(description = "ID do dado externo relacionado", example = "3")
    private Long dadoExternoId;

    @Schema(description = "Tipo do alerta", example = "IRRIGAR")
    private String tipo;

    @Schema(description = "Nível de risco", example = "ALTO")
    private String nivelRisco;

    @Schema(description = "Mensagem do alerta", example = "Umidade do ar abaixo de 40%")
    private String mensagem;

    @Schema(description = "Recomendação", example = "Iniciar irrigação imediatamente")
    private String recomendacao;

    @Schema(description = "Status do alerta", example = "ATIVO")
    private String status;

    @Schema(description = "Data de geração")
    private LocalDateTime dataGeracao;

    @Schema(description = "Data de resolução")
    private LocalDateTime dataResolucao;
}
