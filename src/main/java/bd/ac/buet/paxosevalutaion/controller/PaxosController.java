package bd.ac.buet.paxosevalutaion.controller;

import bd.ac.buet.paxosevalutaion.dto.ObjectStore;
import bd.ac.buet.paxosevalutaion.dto.UserData;
import bd.ac.buet.paxosevalutaion.service.TestDataService;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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

}
