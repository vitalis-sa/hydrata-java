package fiap.hydrata.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "LEITURA_LUZ")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeituraLuz {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "leitura_luz_seq")
    @SequenceGenerator(name = "leitura_luz_seq", sequenceName = "SQ_LEITURA_LUZ", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DISPOSITIVO_IOT_ID", nullable = false)
    private DispositivoIot dispositivoIot;

    @Column(name = "LUMINOSIDADE", nullable = false)
    private BigDecimal luminosidade;

    @Column(name = "DATA_LEITURA")
    private LocalDateTime dataLeitura;
}
