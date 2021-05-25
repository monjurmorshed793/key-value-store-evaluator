package bd.ac.buet.paxosevalutaion.dto.response;

import bd.ac.buet.paxosevalutaion.dto.TempData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TempDataListResponse {
    List<TempData> tempData;
    String server;
}
