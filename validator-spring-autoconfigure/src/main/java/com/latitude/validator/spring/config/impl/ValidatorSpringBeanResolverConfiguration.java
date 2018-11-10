package com.latitude.validator.spring.config.impl;

import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

@Configuration
public class ValidatorSpringBeanResolverConfiguration {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ValidatorSpringBeanResolverActuator validationBeanResolverActuator(Environment environment) {
        return new ValidatorSpringBeanResolverActuator(environment, new DefaultBeanNameGenerator());
    }

}
