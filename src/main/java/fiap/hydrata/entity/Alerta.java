package fiap.hydrata.entity;

import fiap.hydrata.enums.NivelRisco;
import fiap.hydrata.enums.StatusAlerta;
import fiap.hydrata.enums.TipoAlerta;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ALERTA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "alerta_seq")
    @SequenceGenerator(name = "alerta_seq", sequenceName = "SQ_ALERTA", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROPRIEDADE_ID", nullable = false)
    private Propriedade propriedade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LEITURA_ID")
    private Leitura leitura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DADO_EXTERNO_ID")
    private DadoExterno dadoExterno;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO", nullable = false, length = 20)
    private TipoAlerta tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "NIVEL_RISCO", nullable = false, length = 20)
    private NivelRisco nivelRisco;

    @Column(name = "MENSAGEM", length = 500)
    private String mensagem;

    @Column(name = "RECOMENDACAO", length = 500)
    private String recomendacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 20)
    private StatusAlerta status;

    @Column(name = "DATA_GERACAO")
    private LocalDateTime dataGeracao;

    @Column(name = "DATA_RESOLUCAO")
    private LocalDateTime dataResolucao;
}
