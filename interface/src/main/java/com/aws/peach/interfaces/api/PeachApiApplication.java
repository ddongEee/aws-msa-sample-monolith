package com.aws.peach.interfaces.api;

import com.aws.peach.configuration.PeachApiContextConfig;
import com.aws.peach.infrastructure.configuration.DummyConfiguration;
import com.aws.peach.infrastructure.configuration.KafkaMessageConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


@SpringBootApplication
@Import(value = {
		PeachApiContextConfig.class,
		KafkaMessageConfiguration.class,
		DummyConfiguration.class
})
public class PeachApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(PeachApiApplication.class, args);
	}
}
