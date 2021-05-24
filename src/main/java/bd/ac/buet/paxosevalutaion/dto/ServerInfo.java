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
public class ServerInfo {
    private String id;
    private String name;
    private Instant createdOn;
    private Instant updatedOn;
}
