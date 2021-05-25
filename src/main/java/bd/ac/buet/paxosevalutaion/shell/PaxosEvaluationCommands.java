package bd.ac.buet.paxosevalutaion.shell;

import bd.ac.buet.paxosevalutaion.controller.PaxosController;
import bd.ac.buet.paxosevalutaion.dto.ObjectStore;
import bd.ac.buet.paxosevalutaion.dto.Status;
import bd.ac.buet.paxosevalutaion.dto.TempData;
import bd.ac.buet.paxosevalutaion.dto.TestObjectData;
import bd.ac.buet.paxosevalutaion.dto.response.ObjectStoreResponse;
import bd.ac.buet.paxosevalutaion.service.TestDataService;
import com.google.gson.Gson;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import reactor.core.publisher.Mono;

import java.time.temporal.ChronoField;
import java.util.List;

import static java.time.temporal.ChronoField.INSTANT_SECONDS;
import static java.time.temporal.ChronoField.NANO_OF_SECOND;

@ShellComponent
public class PaxosEvaluationCommands {
    private final PaxosController paxosController;
    private final Gson gson;
    private final TestDataService testDataService;

    public PaxosEvaluationCommands(PaxosController paxosController, Gson gson, TestDataService testDataService) {
        this.paxosController = paxosController;
        this.gson = gson;
        this.testDataService = testDataService;
    }

    @ShellMethod("Send a test data without Id")
    public void sendTestData(){
        System.out.println("Generated test data");
        TestObjectData testObjectData = testDataService.createTestData();
        String testObjectDataStr = gson.toJson(testObjectData);
        System.out.println("#######################");
        System.out.println(testObjectDataStr);
        System.out.println("#######################");

        System.out.println("Sending data to server");
        ObjectStore objectStore = testDataService.convertToObjectStore(testObjectData);
        Mono<ObjectStoreResponse> objectStoreResponseMono = paxosController.saveData(objectStore);
        objectStoreResponseMono.subscribe((o)->{
            System.out.println("Response from server: "+ o.getServer());
            System.out.println("Object Response:");
            o.getObjectStore().setId(o.getObjectStore().getId());
            System.out.println(gson.toJson(o.getObjectStore()));
        });
    }

    @ShellMethod("Delete all data")
    public void deleteAllData(){
        System.out.println("Deleting all data");
        paxosController.deleteAllTempData()
                .subscribe((o)->{
                    System.out.println("Deletion complete!");
                });
    }

    @ShellMethod("Delete all object data")
    public void deleteAllObjectData(){
        paxosController.deleteAllObjectData()
                .subscribe((o)->{
                    System.out.println("Deletion complete!");
                });
    }
    @ShellMethod("Get data by object id")
    public void getByDataObjectId(String objectId){
        paxosController.getData(objectId)
                .subscribe((o)->{
                    System.out.println(o.getObjectStore().getCustomObject());
                });
    }

    @ShellMethod("Get all object store data")
    public void getAllObjectStoreData(){
        paxosController.getAllObjectStore()
                .collectList()
                .subscribe(o->{
                    o.forEach((obj->{
                        System.out.println(gson.toJson(obj));
                    }));
                });
    }

    @ShellMethod("Get temp storage data by object id")
    public void getTempStorageData(String objectId){
        paxosController.getTempDataByObjectId(objectId)
                .subscribe((o)->{
                    List<TempData> tempDataList = o.getTempData();
                    System.out.println("Response from "+o.getServer());
                    tempDataList.forEach(t->{
                        System.out.println(gson.toJson(t));
                        System.out.println(t.getUpdatedOn().getLong(NANO_OF_SECOND)- t.getCreatedOn().getLong(NANO_OF_SECOND));
                    });
                });
    }

    @ShellMethod("Get temp storage data by object id and status(IN_PROCESS, ACCEPTED,REJECTED)")
    public void getTempStorageDataByStatus(String objectId, Status status){
        paxosController.getTempDataByObjectIdAndStatus(objectId, status)
                .subscribe((o)->{
                    List<TempData> tempDataList = o.getTempData();
                    tempDataList.forEach(t->{
                        System.out.println(gson.toJson(t));
                    });
                });
    }

    @ShellMethod("Get all server information")
    public void getAllServerInformation(){
        paxosController.getAllServerInfo()
                .collectList()
                .subscribe((s)->{
                    s.forEach(t->{
                        System.out.println(gson.toJson(t));
                    });
                });
    }

    @ShellMethod("Load all temp data")
    public void loadAllTempData(){
        paxosController.loadAllTempData()
                .collectList()
                .subscribe((s)->{
                    s.forEach(t->{
                        System.out.println(gson.toJson(t));
                    });
                });
    }

    @ShellMethod("Load all proposer stores")
    public void loadAllProposerStore(){
        paxosController.loadAllProposerStores()
                .collectList()
                .subscribe((s)->{
                    s.forEach(t->{
                        System.out.println(gson.toJson(t));
                    });
                });
    }

    @ShellMethod("Load all detailed proposer store")
    public void loadDetailedProposerStore(){
        paxosController.loadAllDetailedProposerStores()
                .collectList()
                .subscribe((s)->{
                    s.forEach(t->{
                        System.out.println(gson.toJson(t));
                    });
                });
    }
}
