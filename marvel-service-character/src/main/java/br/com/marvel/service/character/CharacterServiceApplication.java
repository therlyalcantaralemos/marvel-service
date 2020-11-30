package br.com.marvel.service.character;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@EnableMongoRepositories
@EnableMongoAuditing
@EnableCaching
@EnableEurekaClient
@SpringBootApplication
public class CharacterServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CharacterServiceApplication.class, args);
	}

}
