package bd.ac.buet.paxosevalutaion.dto.response;

import bd.ac.buet.paxosevalutaion.dto.TempData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TempDataResponse {
    private TempData tempData;
    private String server;
}
