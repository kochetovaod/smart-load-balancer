package com.company.loadbalancer.networking;

/**
 * Networking module for Smart Load Balancer
 * Handles HTTP server, client and network communications
 */
public class NetworkingModule {
    
    /**
     * Returns the name of the networking module
     * @return module name
     */
    public static String getModuleName() {
        return "networking";
    }
    
    /**
     * Returns the module description
     * @return module description
     */
    public static String getDescription() {
        return "Netty-based HTTP server and client for load balancing";
    }
    
    /**
     * Checks if networking module supports HTTP
     * @return true if HTTP is supported
     */
    public static boolean supportsHttp() {
        return true;
    }
}
