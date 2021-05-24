package bd.ac.buet.paxosevalutaion.service;

import bd.ac.buet.paxosevalutaion.dto.ObjectStore;
import bd.ac.buet.paxosevalutaion.dto.UserData;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

@Service
public class TestDataService {
    private final Gson gson;

    public TestDataService(Gson gson) {
        this.gson = gson;
    }

    public Object createUserData(){
        UserData userData =  UserData
                .builder()
                .firstName("Monjur")
                .lastName("Morshed")
                .email("monjurmorshed793@gmail.com")
                .build();

        String userDataStr = gson.toJson(userData);
        return ObjectStore.builder()
                .id(userData.getObjectId())
                .customObject(userDataStr)
                .build();
    }

    public UserData objectStoreToUserData(ObjectStore objectStore){
        UserData userData = gson.fromJson(objectStore.getCustomObject(), UserData.class);
        userData.setObjectId(objectStore.getId());
        return userData;
    }
}
