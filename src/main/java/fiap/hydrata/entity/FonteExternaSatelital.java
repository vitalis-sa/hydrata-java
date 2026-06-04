package fiap.hydrata.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("SATELITE")
@Data
@NoArgsConstructor
public class FonteExternaSatelital extends FonteExterna {
}
