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
public class DetailedProposerStore {
    private String id;
    private ProposerStore proposerStore;
    private ServerInfo serverInfo;
    private TempData respondedTempData;
    private State state;
    private Status status;
    private Instant createdOn;
    private Instant updatedOn;
}
