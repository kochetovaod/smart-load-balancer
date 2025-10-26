package com.company.loadbalancer.storage;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StorageModuleTest {

    @Test
    void shouldReturnCorrectModuleName() {
        assertEquals("storage", StorageModule.getModuleName());
    }
    
    @Test
    void shouldReturnSupportedDatabases() {
        String[] databases = StorageModule.getSupportedDatabases();
        assertNotNull(databases);
        assertTrue(databases.length > 0);
        assertArrayEquals(new String[]{"PostgreSQL", "TimescaleDB"}, databases);
    }
    
    @Test
    void shouldHaveConnectionPoolingEnabled() {
        assertTrue(StorageModule.isConnectionPoolingEnabled());
    }
}
