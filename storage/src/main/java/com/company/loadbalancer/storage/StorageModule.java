package com.company.loadbalancer.storage;

/**
 * Storage module for Smart Load Balancer
 * Handles database operations and data persistence
 */
public class StorageModule {
    
    /**
     * Returns the name of the storage module
     * @return module name
     */
    public static String getModuleName() {
        return "storage";
    }
    
    /**
     * Returns supported database types
     * @return array of database types
     */
    public static String[] getSupportedDatabases() {
        return new String[]{"PostgreSQL", "TimescaleDB"};
    }
    
    /**
     * Checks if connection pooling is enabled
     * @return true if connection pooling is available
     */
    public static boolean isConnectionPoolingEnabled() {
        return true;
    }
}
