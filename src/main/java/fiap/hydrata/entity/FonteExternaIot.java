package fiap.hydrata.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("IOT")
@Data
@NoArgsConstructor
public class FonteExternaIot extends FonteExterna {
}
