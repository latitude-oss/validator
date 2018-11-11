/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.latitude.validator.spring.config.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;

import com.latitude.validator.spi.ValidatorComponent;

public class ValidatorSpringBeanResolver {

    private final Collection<String> basePackages;

    public ValidatorSpringBeanResolver() {
        this.basePackages = new ArrayList<String>();
    }

    public void addBasePackages(String[] basePackages) {
        Assert.notNull(basePackages, "base-packages argument cannot be null");
        this.basePackages.addAll(Arrays.asList(basePackages));
    }

    public void addBasePackage(String basePackage) {
        Assert.notNull(basePackage, "base-package argument cannot be null");
        this.basePackages.add(basePackage);
    }

    public void scanValidatorComponents(BeanDefinitionRegistry beanDefinitionRegistry,
            BeanNameGenerator beanNameGenerator, Environment environment) {
        Assert.notNull(beanDefinitionRegistry, "BeanDefinitionRegistry is required");
        Assert.notNull(beanNameGenerator, "BeanNameGenerator is required");
        Assert.notNull(environment, "Environment is required");
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false,
                environment);
        provider.addIncludeFilter(new AnnotationTypeFilter(ValidatorComponent.class));
        if (basePackages.isEmpty()) {
            basePackages.add("*");
        }
        for (String basePackage : basePackages) {
            Set<BeanDefinition> beans = provider.findCandidateComponents(basePackage);
            for (BeanDefinition beanDefinition : beans) {
                String beanName = beanNameGenerator.generateBeanName(beanDefinition, beanDefinitionRegistry);
                beanDefinitionRegistry.registerBeanDefinition(beanName, beanDefinition);
            }
        }
    }

}
