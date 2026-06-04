package fiap.hydrata.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("API_CLIMA")
@Data
@NoArgsConstructor
public class FonteExternaApi extends FonteExterna {
}
