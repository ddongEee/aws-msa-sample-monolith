package com.aws.peach.infrastructure.configuration.api;

import com.aws.peach.infrastructure.configuration.DummyConfiguration;
import com.aws.peach.infrastructure.configuration.KafkaMessageConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {
        KafkaMessageConfiguration.class,
        DummyConfiguration.class
})
public class PeachApiInfrastructureContextConfig {
}
