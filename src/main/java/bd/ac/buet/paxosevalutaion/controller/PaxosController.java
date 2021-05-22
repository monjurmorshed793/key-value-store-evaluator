package bd.ac.buet.paxosevalutaion.controller;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class PaxosController {

    private final WebClient.Builder loadBalancedWebClientBuilder;
    private final DiscoveryClient discoveryClient;
    private final ReactorLoadBalancerExchangeFilterFunction lbFunction;

    public PaxosController(WebClient.Builder loadBalancedWebClientBuilder, DiscoveryClient discoveryClient, ReactorLoadBalancerExchangeFilterFunction lbFunction) {
        this.loadBalancedWebClientBuilder = loadBalancedWebClientBuilder;
        this.discoveryClient = discoveryClient;
        this.lbFunction = lbFunction;
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

}
