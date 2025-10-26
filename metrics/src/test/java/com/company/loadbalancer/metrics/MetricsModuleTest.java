package com.company.loadbalancer.metrics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MetricsModuleTest {

    @Test
    void shouldReturnCorrectModuleName() {
        assertEquals("metrics", MetricsModule.getModuleName());
    }
    
    @Test
    void shouldReturnSupportedMetrics() {
        String[] metrics = MetricsModule.getSupportedMetrics();
        assertNotNull(metrics);
        assertTrue(metrics.length > 0);
        assertArrayEquals(new String[]{"rps", "latency", "error_rate", "active_connections"}, metrics);
    }
    
    @Test
    void shouldHaveStorageEnabled() {
        assertTrue(MetricsModule.isStorageEnabled());
    }
}
