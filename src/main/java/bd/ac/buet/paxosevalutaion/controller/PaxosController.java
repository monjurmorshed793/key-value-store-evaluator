package bd.ac.buet.paxosevalutaion.controller;

import bd.ac.buet.paxosevalutaion.dto.ObjectStore;
import bd.ac.buet.paxosevalutaion.dto.Status;
import bd.ac.buet.paxosevalutaion.dto.TempData;
import bd.ac.buet.paxosevalutaion.dto.UserData;
import bd.ac.buet.paxosevalutaion.service.TestDataService;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.web.bind.annotation.*;
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
    public Mono<ObjectStore> saveData(ObjectStore objectStore){
        return loadBalancedWebClientBuilder
                .build()
                .put()
                .uri("save")
                .retrieve().bodyToMono(ObjectStore.class);
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


    @GetMapping("/temp-data-all")
    public Flux<TempData> getAllTempData(){
        return loadBalancedWebClientBuilder
                .build()
                .get()
                .uri("temp-data-all")
                .retrieve()
                .bodyToFlux(TempData.class);
    }

    @GetMapping("/temp-data")
    public Flux<TempData> getTempDataByObjectId(@RequestParam("object-id") String objectId){
        return loadBalancedWebClientBuilder
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder.path("/temp-data")
                .queryParam("object-id", objectId).build())
                .retrieve()
                .bodyToFlux(TempData.class);
    }

    @GetMapping("/temp-data-by-status")
    public Flux<TempData> getTempDataByObjectIdAndStatus(@RequestParam("object-id") String objectId, @RequestParam("status")Status status){
        return loadBalancedWebClientBuilder
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder.path("/temp-data-by-status")
                .queryParam("object-id", objectId)
                .queryParam("status", status).build())
                .retrieve()
                .bodyToFlux(TempData.class);
    }
}
