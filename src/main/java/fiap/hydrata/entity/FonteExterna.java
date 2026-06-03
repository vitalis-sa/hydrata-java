package fiap.hydrata.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "FONTE_EXTERNA")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public abstract class FonteExterna {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fonte_externa_seq")
    @SequenceGenerator(name = "fonte_externa_seq", sequenceName = "SQ_FONTE_EXTERNA", allocationSize = 1)
    private Long id;

    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @Column(name = "STATUS", length = 30)
    private String status;
}
