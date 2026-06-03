package fiap.hydrata.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "PRODUTOR_COOPERATIVA")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutorCooperativa {

    @EmbeddedId
    private ProdutorCooperativaId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("produtorId")
    @JoinColumn(name = "PRODUTOR_ID")
    private Produtor produtor;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("cooperativaId")
    @JoinColumn(name = "COOPERATIVA_ID")
    private Cooperativa cooperativa;

    @Column(name = "DATA_ASSOCIACAO")
    private LocalDateTime dataAssociacao;
}
