package fiap.hydrata.entity;

import fiap.hydrata.enums.StatusGeral;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "PLANO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plano {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "plano_seq")
    @SequenceGenerator(name = "plano_seq", sequenceName = "SQ_PLANO", allocationSize = 1)
    private Long id;

    @Column(name = "NOME", nullable = false, length = 50)
    private String nome;

    @Column(name = "VALOR_MENSALIDADE", nullable = false)
    private java.math.BigDecimal valorMensalidade;

    @Column(name = "DESCRICAO", length = 300)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 30)
    private StatusGeral status;
}
