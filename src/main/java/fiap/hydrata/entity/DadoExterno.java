package fiap.hydrata.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "DADO_EXTERNO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DadoExterno {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dado_externo_seq")
    @SequenceGenerator(name = "dado_externo_seq", sequenceName = "SQ_DADO_EXTERNO", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FONTE_EXTERNA_ID", nullable = false)
    private FonteExterna fonteExterna;

    @Column(name = "TIPO", nullable = false, length = 50)
    private String tipo;

    @Column(name = "VALOR")
    private BigDecimal valor;

    @Column(name = "OBSERVACAO", length = 500)
    private String observacao;

    @Column(name = "DATA_COLETA")
    private LocalDateTime dataColeta;
}
