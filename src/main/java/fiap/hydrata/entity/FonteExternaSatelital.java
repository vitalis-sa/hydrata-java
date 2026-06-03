package fiap.hydrata.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("SATELITE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FonteExternaSatelital extends FonteExterna {
}
