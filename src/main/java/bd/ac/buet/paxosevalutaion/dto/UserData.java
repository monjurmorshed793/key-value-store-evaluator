package bd.ac.buet.paxosevalutaion.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserData {
    private String objectId;
    private String firstName;
    private String lastName;
    private String email;
}
