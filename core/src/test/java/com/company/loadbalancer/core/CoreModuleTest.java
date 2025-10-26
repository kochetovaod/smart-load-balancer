package com.company.loadbalancer.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CoreModuleTest {

    @Test
    void shouldReturnCorrectModuleName() {
        // Given & When
        String moduleName = CoreModule.getModuleName();
        
        // Then
        assertEquals("core", moduleName);
    }
    
    @Test
    void shouldReturnCorrectVersion() {
        // Given & When
        String version = CoreModule.getVersion();
        
        // Then
        assertEquals("1.0.0-SNAPSHOT", version);
    }
    
    @Test
    void shouldBeInitialized() {
        // Given & When
        boolean initialized = CoreModule.isInitialized();
        
        // Then
        assertTrue(initialized);
    }
}
