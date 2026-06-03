package fiap.hydrata.entity;

import fiap.hydrata.enums.StatusGeral;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "PRODUTOR")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produtor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "produtor_seq")
    @SequenceGenerator(name = "produtor_seq", sequenceName = "SQ_PRODUTOR", allocationSize = 1)
    private Long id;

    @Column(name = "NOME", nullable = false, length = 150)
    private String nome;

    @Column(name = "CPF", unique = true, nullable = false, length = 14)
    private String cpf;

    @Column(name = "EMAIL", unique = true, nullable = false, length = 150)
    private String email;

    @Column(name = "TELEFONE", length = 20)
    private String telefone;

    @Column(name = "SENHA", nullable = false, length = 255)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 30)
    private StatusGeral status;

    @Column(name = "DATA_CADASTRO")
    private LocalDateTime dataCadastro;
}
