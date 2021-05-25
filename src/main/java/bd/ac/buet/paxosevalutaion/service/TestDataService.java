package bd.ac.buet.paxosevalutaion.service;

import bd.ac.buet.paxosevalutaion.dto.ObjectStore;
import bd.ac.buet.paxosevalutaion.dto.TestObjectData;
import bd.ac.buet.paxosevalutaion.dto.UserData;
import com.google.gson.Gson;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;

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

    public TestObjectData createTestData(){
        return TestObjectData
                .builder()
                .field1(RandomStringUtils.random(10,"ABCDEFGHIJKLMNOPQRSTWXYZ"))
                .field2(RandomStringUtils.random(10,true, true))
                .field3(RandomStringUtils.randomAlphanumeric(10))
                .createdOn(Instant.now())
                .updatedOn(Instant.now())
                .build();
    }

    public ObjectStore convertToObjectStore(TestObjectData testObjectData){
        return ObjectStore
                .builder()
                .customObject(gson.toJson(testObjectData))
                .createdOn(Instant.now())
                .updatedOn(Instant.now())
                .build();
    }
}
