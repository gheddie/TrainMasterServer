package de.gravitex.trainmaster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// @ComponentScan(basePackages= {"de.gravitex.trainmaster.repo"}) 
public class TrainRunApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainRunApplication.class, args);
	}

}
