package bd.ac.buet.paxosevalutaion.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class ObjectStore {
    private String id;
    private String customObject;
    private Instant createdOn;
    private Instant updatedOn;
}
