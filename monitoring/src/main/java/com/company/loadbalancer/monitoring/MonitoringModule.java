package com.company.loadbalancer.monitoring;

/**
 * Monitoring module for Smart Load Balancer
 * Handles metrics collection and monitoring
 */
public class MonitoringModule {
    
    /**
     * Returns the name of the monitoring module
     * @return module name
     */
    public static String getModuleName() {
        return "monitoring";
    }
    
    /**
     * Returns supported monitoring types
     * @return array of monitoring types
     */
    public static String[] getSupportedMonitoring() {
        return new String[]{"prometheus", "micrometer", "health_checks"};
    }
    
    /**
     * Checks if monitoring is enabled
     * @return true if monitoring is available
     */
    public static boolean isMonitoringEnabled() {
        return true;
    }
}
