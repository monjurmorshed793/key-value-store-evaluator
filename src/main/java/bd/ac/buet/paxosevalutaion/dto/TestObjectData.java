package bd.ac.buet.paxosevalutaion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestObjectData {
    private String id;
    private String field1;
    private String field2;
    private String field3;
    private Instant createdOn;
    private Instant updatedOn;
}
