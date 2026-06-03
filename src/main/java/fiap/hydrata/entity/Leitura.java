package fiap.hydrata.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "LEITURA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Leitura {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "leitura_seq")
    @SequenceGenerator(name = "leitura_seq", sequenceName = "SQ_LEITURA", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SENSOR_ID", nullable = false)
    private Sensor sensor;

    @Column(name = "UMIDADE_AR", nullable = false)
    private BigDecimal umidadeAr;

    @Column(name = "TEMPERATURA")
    private BigDecimal temperatura;

    @Column(name = "LUMINOSIDADE")
    private BigDecimal luminosidade;

    @Column(name = "DATA_LEITURA")
    private LocalDateTime dataLeitura;
}
