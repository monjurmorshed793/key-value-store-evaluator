package bd.ac.buet.paxosevalutaion.controller;

import bd.ac.buet.paxosevalutaion.dto.*;
import bd.ac.buet.paxosevalutaion.dto.response.ObjectStoreResponse;
import bd.ac.buet.paxosevalutaion.dto.response.TempDataListResponse;
import bd.ac.buet.paxosevalutaion.service.TestDataService;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class PaxosController {

    private final WebClient.Builder loadBalancedWebClientBuilder;
    private final DiscoveryClient discoveryClient;
    private final ReactorLoadBalancerExchangeFilterFunction lbFunction;
    private final TestDataService testDataService;

    public PaxosController(WebClient.Builder loadBalancedWebClientBuilder, DiscoveryClient discoveryClient, ReactorLoadBalancerExchangeFilterFunction lbFunction, TestDataService testDataService) {
        this.loadBalancedWebClientBuilder = loadBalancedWebClientBuilder;
        this.discoveryClient = discoveryClient;
        this.lbFunction = lbFunction;
        this.testDataService = testDataService;
    }
    @RequestMapping("/hello")
    public Mono<String> sayHello(){
//         List<ServiceInstance> serviceInstances = discoveryClient.getInstances("keyvaluestore");
        return loadBalancedWebClientBuilder
                .build()
                .get()
                .uri("test-hello")
                .retrieve()
                .bodyToMono(String.class);
    }

    @RequestMapping("/test-initial-user-data")
    public Mono<UserData> testInitialUserData(){
        return loadBalancedWebClientBuilder
                .build()
                .put()
                .uri("save")
                .body(Mono.just(testDataService.createUserData()), ObjectStore.class)
                .retrieve()
                .bodyToMono(ObjectStore.class)
                .map(o-> testDataService.objectStoreToUserData(o));
    }

    @PutMapping("/save")
    public Mono<ObjectStoreResponse> saveData(ObjectStore objectStore){
        return loadBalancedWebClientBuilder
                .build()
                .put()
                .uri("save")
                .body(BodyInserters.fromValue(objectStore))
                .retrieve().bodyToMono(ObjectStoreResponse.class);
    }

    @GetMapping("/get/{id}")
    public Mono<ObjectStoreResponse> getData(@RequestParam("id") String objectId){
        return loadBalancedWebClientBuilder
                .build()
                .get()
                .uri("id/"+objectId)
                .retrieve()
                .bodyToMono(ObjectStoreResponse.class);
    }

    @DeleteMapping("/delete-all-temp-data")
    public Mono<Void> deleteAllTempData(){
        return loadBalancedWebClientBuilder
                .build()
                .delete()
                .uri("delete-temp-data")
                .retrieve()
                .bodyToMono(Void.class);
    }

    @DeleteMapping("/delete-all-object-data")
    public Mono<Void> deleteAllObjectData(){
        return loadBalancedWebClientBuilder
                .build()
                .delete()
                .uri("delete-all-object-data")
                .retrieve()
                .bodyToMono(Void.class);
    }

    @GetMapping("/get-all-object-store")
    public Flux<ObjectStore> getAllObjectStore(){
        return loadBalancedWebClientBuilder
                .build()
                .get()
                .uri("get-all-object-store")
                .retrieve()
                .bodyToFlux(ObjectStore.class);
    }


    @GetMapping("/temp-data-all")
    public Mono<TempDataListResponse> getAllTempData(){
        return loadBalancedWebClientBuilder
                .build()
                .get()
                .uri("temp-data-all")
                .retrieve()
                .bodyToMono(TempDataListResponse.class);
    }

    @GetMapping("/temp-data")
    public Mono<TempDataListResponse> getTempDataByObjectId(@RequestParam("object-id") String objectId){
        return loadBalancedWebClientBuilder
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder.path("/temp-data")
                .queryParam("object-id", objectId).build())
                .retrieve()
                .bodyToMono(TempDataListResponse.class);
    }

    @GetMapping("/temp-data-by-status")
    public Mono<TempDataListResponse> getTempDataByObjectIdAndStatus(@RequestParam("object-id") String objectId, @RequestParam("status")Status status){
        return loadBalancedWebClientBuilder
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder.path("/temp-data-by-status")
                .queryParam("object-id", objectId)
                .queryParam("status", status).build())
                .retrieve()
                .bodyToMono(TempDataListResponse.class);
    }

    @GetMapping("/server-info")
    public Flux<ServerInfo> getAllServerInfo(){
        return loadBalancedWebClientBuilder
                .build()
                .get()
                .uri("server-info")
                .retrieve()
                .bodyToFlux(ServerInfo.class);
    }
}
