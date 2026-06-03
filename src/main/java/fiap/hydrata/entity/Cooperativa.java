package fiap.hydrata.entity;

import fiap.hydrata.enums.StatusGeral;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "COOPERATIVA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cooperativa {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cooperativa_seq")
    @SequenceGenerator(name = "cooperativa_seq", sequenceName = "SQ_COOPERATIVA", allocationSize = 1)
    private Long id;

    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @Column(name = "EMAIL", length = 150)
    private String email;

    @Column(name = "TELEFONE", length = 20)
    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 30)
    private StatusGeral status;

    @Column(name = "DATA_CADASTRO")
    private LocalDateTime dataCadastro;
}
