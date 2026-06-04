package fiap.hydrata.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "LEITURA_CLIMA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeituraClima {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "leitura_clima_seq")
    @SequenceGenerator(name = "leitura_clima_seq", sequenceName = "SQ_LEITURA_CLIMA", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DISPOSITIVO_IOT_ID", nullable = false)
    private DispositivoIot dispositivoIot;

    @Column(name = "UMIDADE_AR", nullable = false)
    private BigDecimal umidadeAr;

    @Column(name = "TEMPERATURA", nullable = false)
    private BigDecimal temperatura;

    @Column(name = "DATA_LEITURA")
    private LocalDateTime dataLeitura;
}
