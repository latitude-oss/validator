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

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import com.latitude.validator.spring.config.EnableValidationEngine;

public class ValidatorSpringBeanResolverActuator implements BeanDefinitionRegistryPostProcessor {

    private final Environment environment;

    private final BeanNameGenerator beanNameGenerator;

    public ValidatorSpringBeanResolverActuator(Environment environment, BeanNameGenerator beanNameGenerator) {
        Assert.notNull(environment, "Environment is required and cannot be null");
        Assert.notNull(beanNameGenerator, "BeanNameGenerator is required and cannot be null");
        this.environment = environment;
        this.beanNameGenerator = beanNameGenerator;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        String[] candidateNames = registry.getBeanDefinitionNames();
        for (String beanName : candidateNames) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);
            String className = beanDefinition.getBeanClassName();
            if (beanDefinition instanceof AnnotatedBeanDefinition && className != null
                    && className.equals(((AnnotatedBeanDefinition) beanDefinition).getMetadata().getClassName())) {
                AnnotationMetadata metadata = ((AnnotatedBeanDefinition) beanDefinition).getMetadata();
                String annotationName = EnableValidationEngine.class.getName();
                if (metadata.hasAnnotation(annotationName)) {
                    AnnotationAttributes enableValidationEngine = AnnotationAttributes
                            .fromMap(metadata.getAnnotationAttributes(annotationName));
                    if (!enableValidationEngine.getBoolean("scanValidators")) {
                        return;
                    }
                    ValidatorSpringBeanResolver validatorComponentBeanResolver = new ValidatorSpringBeanResolver();
                    String[] validatorPackages = enableValidationEngine.getStringArray("validatorPackages");
                    if (validatorPackages != null && validatorPackages.length > 0) {
                        validatorComponentBeanResolver.addBasePackages(validatorPackages);
                    }
                    Class<?>[] validatorPackageClasses = enableValidationEngine
                            .getClassArray("validatorPackageClasses");
                    for (Class<?> validatorPackageClass : validatorPackageClasses) {
                        validatorComponentBeanResolver.addBasePackage(ClassUtils.getPackageName(validatorPackageClass));
                    }
                    validatorComponentBeanResolver.scanValidatorComponents(registry, beanNameGenerator, environment);
                    break;
                }
            }
        }
    }

}
