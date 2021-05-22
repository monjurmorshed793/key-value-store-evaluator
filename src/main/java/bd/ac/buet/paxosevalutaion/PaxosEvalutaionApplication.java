package bd.ac.buet.paxosevalutaion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class PaxosEvalutaionApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaxosEvalutaionApplication.class, args);
	}

}
