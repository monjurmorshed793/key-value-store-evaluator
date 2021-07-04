package bd.ac.buet.paxosevalutaion.shell;

import bd.ac.buet.paxosevalutaion.controller.PaxosController;
import bd.ac.buet.paxosevalutaion.dto.ObjectStore;
import bd.ac.buet.paxosevalutaion.dto.Status;
import bd.ac.buet.paxosevalutaion.dto.TempData;
import bd.ac.buet.paxosevalutaion.dto.TestObjectData;
import bd.ac.buet.paxosevalutaion.dto.response.ObjectStoreResponse;
import bd.ac.buet.paxosevalutaion.service.TestDataService;
import com.google.gson.Gson;
import org.springframework.scheduling.annotation.Async;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @ShellMethod("Create test-1 ")
    public void createEvaluationDataForCreation(Integer numberOfServers) throws InterruptedException{
        Map<Integer, Long> serverAvgResponse = new HashMap<>();
        for(int i=10; i<=100; i=i+10){
            System.out.println("Data size: "+ i);
            long averageResponseTime = createTestDataForEvaluation(i, numberOfServers);
            serverAvgResponse.put(i, averageResponseTime);
            //Thread.sleep(20000);
        }

        String x="";
        String y="";
        for(int i=10; i<=100; i=i+10){
            x = x+i+",";
            y= y+serverAvgResponse.get(i)+",";
        }
        for(Map.Entry<Integer, Long> entry: serverAvgResponse.entrySet()){
            System.out.println(entry.getKey()+","+entry.getValue());
        }

        System.out.println(x);
        System.out.println(y);
    }


    @ShellMethod("Create test-2 ")
    public void createEvaluationDataForDeletion(Integer numberOfServers) throws InterruptedException{
        Map<Integer, Long> serverAvgResponse = new HashMap<>();
        for(int i=5; i<=50; i=i+5){
            System.out.println("Data size: "+ i);
            long averageResponseTime = deleteAllDataAndFetchResponse(i, numberOfServers);
            serverAvgResponse.put(i, averageResponseTime);
            //Thread.sleep(20000);
        }

        String x="";
        String y="";
        for(int i=5; i<=50; i=i+5){
            x = x+i+",";
            y= y+serverAvgResponse.get(i)+",";
        }
        for(Map.Entry<Integer, Long> entry: serverAvgResponse.entrySet()){
            System.out.println(entry.getKey()+","+entry.getValue());
        }

        System.out.println(x);
        System.out.println(y);
    }

    @ShellMethod("Create and fetch avg response ")
    public long createTestDataForEvaluation(Integer numberOfTestData, Integer numberOfServers) throws InterruptedException {
        for(int i=0; i<(numberOfServers); i++){
            deleteAllData();
            Thread.sleep(1000);
        }

        for(int i=0; i<numberOfTestData; i++){
            sendTestData();
            //Thread.sleep(1000);
        }
        Integer waitTime = numberOfTestData*2;
        System.out.println("Waiting for seconds "+ waitTime);
        Thread.sleep((numberOfTestData*2*1000+10000)>120000? 120000:(numberOfTestData*2*1000+10000));
        boolean allItemReturned = true;
        List<Long> secondList = new ArrayList<>();
        int totalItems = 0;

        for(int i=0; i<numberOfServers; i++){
            List<TempData> tempDataList = paxosController.loadAllTempData().collectList().block();
            System.out.println("Total data found: "+tempDataList.size());
            totalItems = totalItems+ tempDataList.size();
            for(TempData tempData: tempDataList){
                if (tempData.getStatus().equals(Status.ACCEPTED)){
                    Duration duration = Duration.between(tempData.getUpdatedOn(), tempData.getCreatedOn());
                    long seconds = duration.getNano();
                    secondList.add(seconds);
                }
            }
        }

        totalItems = totalItems/numberOfServers;
        long totalSeconds = secondList.stream()
                .reduce(Long::sum)
                .get();
        long averageSeconds = (totalSeconds/numberOfServers)/numberOfTestData;
        if(totalItems==numberOfTestData)
            System.out.println("All "+numberOfTestData+ " items created");
        else
            System.out.println("All items was not created");
        System.out.println("Avg Seconds: " + averageSeconds);

        return averageSeconds;
    }

    @ShellMethod("Delete and fetch avg response ")
    public long deleteAllDataAndFetchResponse(Integer numberOfTestData, Integer numberOfServers) throws InterruptedException {
        for(int i=0; i<numberOfServers; i++){
            deleteAllData();
            deleteAllObjectData();
        }
        Thread.sleep(10000);

        for(int i=0; i<numberOfTestData; i++){
            sendTestData();
        }

        System.out.println("Waiting for 10 seconds");
        Thread.sleep(10000);
        boolean allItemReturned = true;
        List<Long> secondList = new ArrayList<>();

        List<TempData> tempDataList = paxosController.loadAllTempData().collectList().block();

        for(TempData tempData: tempDataList){
            deleteDataById(tempData.getId());
        }

        System.out.println("Waiting for another 10 seconds for delete operatin");
        Thread.sleep(10000);



        for(int i=0; i<numberOfServers; i++){
            tempDataList = paxosController.loadAllDeleteTempData().collectList().block();
            if(tempDataList.size()<numberOfTestData && allItemReturned)
                allItemReturned = false;
            for(TempData tempData: tempDataList){
                if (tempData.getStatus().equals(Status.ACCEPTED)){
                    Duration duration = Duration.between(tempData.getUpdatedOn(), tempData.getCreatedOn());
                    long seconds = duration.getNano();
                    secondList.add(seconds);
                }
            }
        }

        long totalSeconds = secondList.stream()
                .reduce(Long::sum)
                .get();
        long averageSeconds = totalSeconds/numberOfTestData;
        if(allItemReturned)
            System.out.println("All "+numberOfTestData+ " items deleted");
        else
            System.out.println("All items was not created");
        System.out.println("Avg Seconds: " + averageSeconds);
        return averageSeconds;
    }

    @ShellMethod("Delete data by id")
    @Async
    public void deleteDataById(String id){
        paxosController.deleteData(id).block();
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
            o.getObjectStore().setId(o.getObjectStore().getId());
        });
    }

    @ShellMethod("Update by ID")
    public void updateData(String objectId){
        System.out.println("Modifying data");
        TestObjectData testObjectData = testDataService.createTestData();
        testObjectData.setId(objectId);
        System.out.println("Modified body data:");
        String testObjectDataStr = gson.toJson(testObjectData);
        System.out.println(testObjectDataStr);
        ObjectStore objectStore = testDataService.convertToObjectStore(testObjectData);
        objectStore.setId(objectId);
        System.out.println("Sending updated data to server");
        Mono<ObjectStoreResponse> objectStoreResponseMono = paxosController.saveData(objectStore);
        objectStoreResponseMono.subscribe((o)->{
            o.getObjectStore().setId(o.getObjectStore().getId());
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

    @ShellMethod("Delete all data by number of servers")
    public void deleteAllDataByServerNumber(int numberOfServers){
        System.out.println("Deleting all data");
        for(int i=0; i<numberOfServers; i++){
            paxosController.deleteAllTempData()
                    .subscribe((o)->{
                        System.out.println("Deletion complete!");
                    });
            deleteAllObjectData();
        }

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
