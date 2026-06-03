package fiap.hydrata.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("IOT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FonteExternaIot extends FonteExterna {
}
