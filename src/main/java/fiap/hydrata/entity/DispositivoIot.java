package fiap.hydrata.entity;

import fiap.hydrata.enums.StatusSensor;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "DISPOSITIVO_IOT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DispositivoIot {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dispositivo_iot_seq")
    @SequenceGenerator(name = "dispositivo_iot_seq", sequenceName = "SQ_DISPOSITIVO_IOT", allocationSize = 1)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROPRIEDADE_ID", nullable = false, unique = true)
    private Propriedade propriedade;

    @Column(name = "MODELO", length = 50)
    private String modelo;

    @Column(name = "MAC_ADDRESS", length = 30, unique = true)
    private String macAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 20)
    private StatusSensor status;

    @Column(name = "DATA_CADASTRO")
    private LocalDateTime dataCadastro;
}
