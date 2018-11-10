package com.latitude.validator.spring.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.latitude.validator.spring.config.impl.ValidationEngineConfigurationSelector;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(value = ValidationEngineConfigurationSelector.class)
public @interface EnableValidationEngine {

    boolean scanValidators() default false;

    String[] validatorPackages() default {};

    Class<?>[] validatorPackageClasses() default {};

}
