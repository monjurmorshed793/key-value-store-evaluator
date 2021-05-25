package bd.ac.buet.paxosevalutaion.dto;

import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObjectStore implements Serializable {
    private String id;
    private String customObject;
    private Instant createdOn;
    private Instant updatedOn;
}
