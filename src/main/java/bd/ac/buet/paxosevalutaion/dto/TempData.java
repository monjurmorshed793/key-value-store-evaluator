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
public class TempData {
    private String id;
    private String objectId;
    private String object;
    private ServerInfo serverInfo;
    private Status status;
    private Instant createdOn;
    private Instant updatedOn;
}
