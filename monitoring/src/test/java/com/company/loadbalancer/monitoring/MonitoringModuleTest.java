package com.company.loadbalancer.monitoring;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MonitoringModuleTest {

    @Test
    void shouldReturnCorrectModuleName() {
        assertEquals("monitoring", MonitoringModule.getModuleName());
    }
    
    @Test
    void shouldReturnSupportedMonitoring() {
        String[] monitoring = MonitoringModule.getSupportedMonitoring();
        assertNotNull(monitoring);
        assertTrue(monitoring.length > 0);
        assertArrayEquals(new String[]{"prometheus", "micrometer", "health_checks"}, monitoring);
    }
    
    @Test
    void shouldHaveMonitoringEnabled() {
        assertTrue(MonitoringModule.isMonitoringEnabled());
    }
}
