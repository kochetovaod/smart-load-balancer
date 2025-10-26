package com.company.loadbalancer.core;

/**
 * Core module of Smart Load Balancer
 * Contains main business logic and configuration
 */
public class CoreModule {
    
    /**
     * Returns the name of the core module
     * @return module name
     */
    public static String getModuleName() {
        return "core";
    }
    
    /**
     * Returns the version of the core module
     * @return module version
     */
    public static String getVersion() {
        return "1.0.0-SNAPSHOT";
    }
    
    /**
     * Checks if core module is properly initialized
     * @return true if module is ready
     */
    public static boolean isInitialized() {
        return true;
    }
}
