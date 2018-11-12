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
