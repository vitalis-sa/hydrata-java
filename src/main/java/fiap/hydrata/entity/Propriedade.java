package fiap.hydrata.entity;

import fiap.hydrata.enums.StatusGeral;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "PROPRIEDADE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Propriedade {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "propriedade_seq")
    @SequenceGenerator(name = "propriedade_seq", sequenceName = "SQ_PROPRIEDADE", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUTOR_ID", nullable = false)
    private Produtor produtor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLANO_ID", nullable = false)
    private Plano plano;

    @Column(name = "NOME", nullable = false, length = 150)
    private String nome;

    @Column(name = "AREA_HECTARES")
    private Double areaHectares;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 30)
    private StatusGeral status;

    @Column(name = "DATA_CADASTRO")
    private LocalDateTime dataCadastro;

    @Embedded
    private Coordenadas coordenadas;
}
