package com.company.loadbalancer.core;

import com.company.loadbalancer.networking.NetworkingModule;
import com.company.loadbalancer.metrics.MetricsModule;
import com.company.loadbalancer.storage.StorageModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for verifying project structure and module accessibility
 */
@DisplayName("Project Structure Test")
class ProjectStructureTest {

    @Test
    @DisplayName("All modules should be accessible and return correct names")
    void allModulesShouldBeAccessible() {
        // Verify core module
        assertThat(CoreModule.getModuleName()).isEqualTo("core");
        assertThat(CoreModule.getVersion()).isEqualTo("1.0.0-SNAPSHOT");
        assertThat(CoreModule.isInitialized()).isTrue();

        // Verify networking module
        assertThat(NetworkingModule.getModuleName()).isEqualTo("networking");
        assertThat(NetworkingModule.getDescription()).isNotEmpty();
        assertThat(NetworkingModule.supportsHttp()).isTrue();

        // Verify metrics module
        assertThat(MetricsModule.getModuleName()).isEqualTo("metrics");
        assertThat(MetricsModule.getSupportedMetrics()).isNotEmpty();
        assertThat(MetricsModule.isStorageEnabled()).isTrue();

        // Verify storage module
        assertThat(StorageModule.getModuleName()).isEqualTo("storage");
        assertThat(StorageModule.getSupportedDatabases()).isNotEmpty();
        assertThat(StorageModule.isConnectionPoolingEnabled()).isTrue();
    }

    @Test
    @DisplayName("Java version should be 17 or higher")
    void javaVersionShouldBe17OrHigher() {
        String javaVersion = System.getProperty("java.version");
        assertThat(javaVersion).isNotNull();
        
        // Parse major version (handles versions like "17.0.9", "21", etc.)
        int majorVersion = getJavaMajorVersion(javaVersion);
        assertThat(majorVersion)
            .as("Java version should be 17 or higher. Current version: %s", javaVersion)
            .isGreaterThanOrEqualTo(17);
    }

    @Test
    @DisplayName("Project should have correct system properties")
    void projectShouldHaveCorrectSystemProperties() {
        assertThat(System.getProperty("java.version")).isNotNull();
        assertThat(System.getProperty("java.home")).isNotNull();
        assertThat(System.getProperty("user.dir")).isNotNull();
    }

    @Test
    @DisplayName("Modules should have valid configurations")
    void modulesShouldHaveValidConfigurations() {
        // Core module validation
        assertThat(CoreModule.getModuleName()).isNotBlank();
        assertThat(CoreModule.getVersion()).matches("^\\d+\\.\\d+\\.\\d+(-SNAPSHOT)?$");

        // Networking module validation
        String[] networkingCapabilities = {NetworkingModule.getModuleName(), NetworkingModule.getDescription()};
        assertThat(networkingCapabilities).noneMatch(String::isEmpty);

        // Metrics module validation
        String[] metrics = MetricsModule.getSupportedMetrics();
        assertThat(metrics).contains("rps", "latency", "error_rate", "active_connections");

        // Storage module validation
        String[] databases = StorageModule.getSupportedDatabases();
        assertThat(databases).contains("PostgreSQL", "TimescaleDB");
    }

    @Test
    @DisplayName("Module classes should be loaded successfully")
    void moduleClassesShouldBeLoadedSuccessfully() {
        // Verify classes can be instantiated and methods can be called
        assertDoesNotThrow(() -> {
            CoreModule core = null; // Static methods, no instance needed
            String coreName = CoreModule.getModuleName();
            
            NetworkingModule networking = null;
            String networkingName = NetworkingModule.getModuleName();
            
            MetricsModule metrics = null;
            String metricsName = MetricsModule.getModuleName();
            
            StorageModule storage = null;
            String storageName = StorageModule.getModuleName();
            
            // Verify all names are valid
            assertThat(coreName).isEqualTo("core");
            assertThat(networkingName).isEqualTo("networking");
            assertThat(metricsName).isEqualTo("metrics");
            assertThat(storageName).isEqualTo("storage");
        });
    }

    @Test
    @DisplayName("Project build properties should be correct")
    void projectBuildPropertiesShouldBeCorrect() {
        // Verify that we're running in a test environment
        assertThat(System.getProperty("org.gradle.test.worker")).isNotNull();
        
        // Verify test-specific properties
        String testWorker = System.getProperty("org.gradle.test.worker");
        assertThat(testWorker).isNotEmpty();
    }

    /**
     * Extracts major version from Java version string
     * Handles different version formats: "17", "17.0.9", "21.0.1", etc.
     */
    private int getJavaMajorVersion(String javaVersion) {
        // Handle versions like "1.8.0_292" (Java 8 and earlier)
        if (javaVersion.startsWith("1.")) {
            return Integer.parseInt(javaVersion.substring(2, 3));
        }
        
        // Handle versions like "17.0.9", "21", "21.0.1"
        int dotIndex = javaVersion.indexOf('.');
        if (dotIndex > -1) {
            return Integer.parseInt(javaVersion.substring(0, dotIndex));
        }
        
        // Handle versions without dots like "17"
        return Integer.parseInt(javaVersion);
    }
}
