package bd.ac.buet.paxosevalutaion.configuration;

import com.google.gson.Gson;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class LoadBalancerConfig {
    @LoadBalanced
    @Bean
    WebClient.Builder webclientBuilder(){
        return WebClient.builder()
                .baseUrl("http://keyvaluestore");
    }

    @Bean
    Gson gson(){
        return new Gson();
    }

//    @Bean
//    WebClient webClient(WebClient.Builder builder){
//        return builder.build();
//    }



//    @Bean
//    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder){
//        return routeLocatorBuilder.routes()
//                .route("key-value-store", route-> route.path("/keyvaluestore").uri("http://keyvaluestore")).build();
//    }
}
