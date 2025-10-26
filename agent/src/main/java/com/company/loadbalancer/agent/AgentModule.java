package com.company.loadbalancer.agent;

/**
 * Agent module for Smart Load Balancer
 * Handles metrics collection on service instances
 */
public class AgentModule {
    
    /**
     * Returns the name of the agent module
     * @return module name
     */
    public static String getModuleName() {
        return "agent";
    }
    
    /**
     * Checks if agent supports gRPC communication
     * @return true if gRPC is supported
     */
    public static boolean supportsGrpc() {
        return true;
    }
    
    /**
     * Checks if agent is active
     * @return true if agent is active
     */
    public static boolean isActive() {
        return true;
    }
}
