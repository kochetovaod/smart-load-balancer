package com.company.loadbalancer.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Test for validating that all dependencies are properly resolved
 */
@DisplayName("Dependency Validation Test")
class DependencyValidationTest {

    @Test
    @DisplayName("Key dependencies should be available in classpath")
    void keyDependenciesShouldBeAvailable() {
        // Test SLF4J availability
        assertDoesNotThrow(() -> {
            Class<?> slf4jLoggerClass = Class.forName("org.slf4j.Logger");
            Class<?> slf4jLoggerFactoryClass = Class.forName("org.slf4j.LoggerFactory");
            assertThat(slf4jLoggerClass).isNotNull();
            assertThat(slf4jLoggerFactoryClass).isNotNull();
        });

        // Test Jackson availability
        assertDoesNotThrow(() -> {
            Class<?> objectMapperClass = Class.forName("com.fasterxml.jackson.databind.ObjectMapper");
            assertThat(objectMapperClass).isNotNull();
        });

        // Test JUnit 5 availability
        assertDoesNotThrow(() -> {
            Class<?> testClass = Class.forName("org.junit.jupiter.api.Test");
            Class<?> assertionsClass = Class.forName("org.junit.jupiter.api.Assertions");
            assertThat(testClass).isNotNull();
            assertThat(assertionsClass).isNotNull();
        });

        // Test AssertJ availability
        assertDoesNotThrow(() -> {
            Class<?> assertionsClass = Class.forName("org.assertj.core.api.Assertions");
            assertThat(assertionsClass).isNotNull();
        });
    }

    @Test
    @DisplayName("Project-specific classes should be accessible")
    void projectSpecificClassesShouldBeAccessible() {
        assertDoesNotThrow(() -> {
            Class<?> coreModuleClass = Class.forName("com.company.loadbalancer.core.CoreModule");
            Class<?> networkingModuleClass = Class.forName("com.company.loadbalancer.networking.NetworkingModule");
            Class<?> metricsModuleClass = Class.forName("com.company.loadbalancer.metrics.MetricsModule");
            Class<?> storageModuleClass = Class.forName("com.company.loadbalancer.storage.StorageModule");
            
            assertThat(coreModuleClass).isNotNull();
            assertThat(networkingModuleClass).isNotNull();
            assertThat(metricsModuleClass).isNotNull();
            assertThat(storageModuleClass).isNotNull();
        });
    }

    @Test
    @DisplayName("Classloader should be able to load basic Java classes")
    void classloaderShouldLoadBasicJavaClasses() {
        assertDoesNotThrow(() -> {
            Class<?> stringClass = Class.forName("java.lang.String");
            Class<?> listClass = Class.forName("java.util.List");
            Class<?> mapClass = Class.forName("java.util.Map");
            
            assertThat(stringClass).isNotNull();
            assertThat(listClass).isNotNull();
            assertThat(mapClass).isNotNull();
        });
    }
}
