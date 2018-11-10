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
