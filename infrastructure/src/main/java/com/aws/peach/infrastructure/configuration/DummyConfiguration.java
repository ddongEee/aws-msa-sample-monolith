package com.aws.peach.infrastructure.configuration;

import com.aws.peach.infrastructure.dummy.Dummies;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {Dummies.class})
public class DummyConfiguration {
}
