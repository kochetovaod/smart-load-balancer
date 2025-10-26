package com.company.loadbalancer.metrics;

/**
 * Metrics module for Smart Load Balancer
 * Handles collection, aggregation and analysis of performance metrics
 */
public class MetricsModule {
    
    /**
     * Returns the name of the metrics module
     * @return module name
     */
    public static String getModuleName() {
        return "metrics";
    }
    
    /**
     * Returns supported metric types
     * @return array of metric types
     */
    public static String[] getSupportedMetrics() {
        return new String[]{"rps", "latency", "error_rate", "active_connections"};
    }
    
    /**
     * Checks if metrics storage is enabled
     * @return true if storage is available
     */
    public static boolean isStorageEnabled() {
        return true;
    }
}
