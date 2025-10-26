package com.company.loadbalancer.agent;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AgentModuleTest {

    @Test
    void shouldReturnCorrectModuleName() {
        assertEquals("agent", AgentModule.getModuleName());
    }
    
    @Test
    void shouldSupportGrpc() {
        assertTrue(AgentModule.supportsGrpc());
    }
    
    @Test
    void shouldBeActive() {
        assertTrue(AgentModule.isActive());
    }
}
