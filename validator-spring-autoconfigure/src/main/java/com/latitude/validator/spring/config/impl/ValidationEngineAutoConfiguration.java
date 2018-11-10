package com.latitude.validator.spring.config.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.latitude.validator.spi.ExecutionMode;
import com.latitude.validator.spi.ValidationChain;
import com.latitude.validator.spi.ValidationOperations;
import com.latitude.validator.spi.Validator;
import com.latitude.validator.spi.impl.ValidationEngine;

@Configuration
public class ValidationEngineAutoConfiguration {

    @Value("${validation.engine.executionMode:STRICT}")
    private ExecutionMode executionMode;

    private Collection<Validator> validators;

    private Collection<ValidationChain> validationChains;

    @Bean
    public ValidationOperations validationOperations() {
        ValidationEngine validationEngine = new ValidationEngine();
        validationEngine.setExecutionMode(executionMode);
        if (validators != null) {
            for (Validator validator : validators) {
                validationEngine.addValidator(validator);
            }
        }
        if (validationChains != null) {
            for (ValidationChain validationChain : validationChains) {
                validationEngine.addValidationChain(validationChain);
            }
        }
        return validationEngine;
    }

    @Autowired(required = false)
    public void setValidators(Collection<Validator> validators) {
        this.validators = validators;
    }

    @Autowired(required = false)
    public void setValidationChains(Collection<ValidationChain> validationChains) {
        this.validationChains = validationChains;
    }

}
