package com.company.loadbalancer.networking;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NetworkingModuleTest {

    @Test
    void shouldReturnCorrectModuleName() {
        assertEquals("networking", NetworkingModule.getModuleName());
    }
    
    @Test
    void shouldReturnDescription() {
        assertNotNull(NetworkingModule.getDescription());
        assertFalse(NetworkingModule.getDescription().isEmpty());
    }
    
    @Test
    void shouldSupportHttp() {
        assertTrue(NetworkingModule.supportsHttp());
    }
}
