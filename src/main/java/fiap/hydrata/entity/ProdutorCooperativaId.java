package fiap.hydrata.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutorCooperativaId implements Serializable {

    @Column(name = "PRODUTOR_ID")
    private Long produtorId;

    @Column(name = "COOPERATIVA_ID")
    private Long cooperativaId;
}
