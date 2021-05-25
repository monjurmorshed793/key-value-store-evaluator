package bd.ac.buet.paxosevalutaion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProposerStore {
    private String id;
    private TempData tempData;
    private State state;
    private Status status;
    private Instant createdOn;
    private Instant updatedOn;
}
