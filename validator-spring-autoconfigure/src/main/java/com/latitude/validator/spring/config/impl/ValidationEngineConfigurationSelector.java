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

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import com.latitude.validator.spring.config.EnableValidationEngine;

public class ValidationEngineConfigurationSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Class<?> annotationType = EnableValidationEngine.class;
        AnnotationAttributes enableValidationEngine = AnnotationAttributes
                .fromMap(importingClassMetadata.getAnnotationAttributes(annotationType.getName(), false));
        Assert.notNull(enableValidationEngine, String.format("@%s is not present on importing class '%s' as expected",
                annotationType.getSimpleName(), importingClassMetadata.getClassName()));
        Collection<String> selectors = new ArrayList<String>();
        selectors.add(ValidationEngineAutoConfiguration.class.getName());
        boolean scanValidators = enableValidationEngine.getBoolean("scanValidators");
        if (scanValidators) {
            selectors.add(ValidatorSpringBeanResolverConfiguration.class.getName());
        }
        return selectors.toArray(new String[] {});
    }

}
