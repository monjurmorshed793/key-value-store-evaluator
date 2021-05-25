package bd.ac.buet.paxosevalutaion.dto.response;

import bd.ac.buet.paxosevalutaion.dto.ObjectStore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ObjectStoreResponse {
    private ObjectStore objectStore;
    private String server;
}
