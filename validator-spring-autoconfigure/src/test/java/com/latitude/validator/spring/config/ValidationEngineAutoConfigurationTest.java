/*
 * Copyright 2018 Latitude Srls
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.latitude.validator.spring.config;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.latitude.validator.spi.ValidationChain;
import com.latitude.validator.spi.ValidationChainBuilder;
import com.latitude.validator.spi.ValidationContext;
import com.latitude.validator.spi.Validator;
import com.latitude.validator.spi.ValidatorComponent;
import com.latitude.validator.spi.impl.AbstractGenericChainedValidator;
import com.latitude.validator.spi.impl.ValidationEngine;
import com.latitude.validator.spring.config.ValidationEngineAutoConfigurationTest.TestConfiguration;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class ValidationEngineAutoConfigurationTest {

    @Autowired
    private ValidationEngine validationEngine;

    @Test
    public void shouldAutoconfigure() {
        Assert.assertNotNull(validationEngine);
        Assert.assertTrue(validationEngine.tryFindValidator("DUMMY_VALIDATOR").isPresent());
        Assert.assertTrue(validationEngine.tryFindValidator("FIRST_CHAINED").isPresent());

        Assert.assertTrue(validationEngine.tryFindValidator("DISCOVERABLE_VALIDATOR").isPresent());
    }

    @Configuration
    @EnableValidationEngine(scanValidators = true, validatorPackages = "com.latitude.validator.spring.config")
    public static class TestConfiguration {

        @Bean
        @ValidatorComponent
        public Validator configuredValidator() {
            return new DummyValidator("DUMMY_VALIDATOR");
        }

        @Bean
        @ValidatorComponent
        public ValidationChain configuredValidationChain() {
            return new ValidationChainBuilder(new DummyValidator("FIRST_CHAINED"))
                    .withNext(new DummyValidator("SECOND_CHAINED")).withNext(new DummyValidator("THIRD_CHAINED"))
                    .build();
        }

    }

    public static class DummyValidator extends AbstractGenericChainedValidator<Object> {

        public DummyValidator() {
            this("DUMMY_VALIDATOR");
        }

        public DummyValidator(String name) {
            super(name);
        }

        @Override
        public boolean doSupports(Object subject) {
            return true;
        }

        @Override
        public void doValidation(Object subject, ValidationContext context) {
        }

    }

    @ValidatorComponent
    public static class DiscoverableValidator extends DummyValidator {

        public DiscoverableValidator() {
            super("DISCOVERABLE_VALIDATOR");
        }

    }

}
