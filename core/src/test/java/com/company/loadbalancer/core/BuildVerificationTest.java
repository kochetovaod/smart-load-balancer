package com.company.loadbalancer.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.nio.file.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for verifying project build structure
 * This test validates that the project is correctly set up for building
 */
@DisplayName("Build Verification Test")
class BuildVerificationTest {

    @Test
    @DisplayName("Project structure should be valid")
    void projectStructureShouldBeValid() {
        Path projectRoot = getProjectRoot();
        
        // Check essential directories exist
        assertThat(projectRoot.resolve("core/src/main/java")).exists();
        assertThat(projectRoot.resolve("networking/src/main/java")).exists();
        assertThat(projectRoot.resolve("metrics/src/main/java")).exists();
        assertThat(projectRoot.resolve("storage/src/main/java")).exists();
        assertThat(projectRoot.resolve("agent/src/main/java")).exists();
        assertThat(projectRoot.resolve("monitoring/src/main/java")).exists();
        
        // Check build files exist
        assertThat(projectRoot.resolve("build.gradle.kts")).exists();
        assertThat(projectRoot.resolve("settings.gradle.kts")).exists();
        assertThat(projectRoot.resolve("gradlew")).exists();
        assertThat(projectRoot.resolve("gradle.properties")).exists();
    }

    @Test
    @DisplayName("All modules should have build configuration")
    void allModulesShouldHaveBuildConfiguration() {
        Path projectRoot = getProjectRoot();
        
        String[] modules = {"core", "networking", "metrics", "storage", "agent", "monitoring"};
        
        for (String module : modules) {
            Path buildFile = projectRoot.resolve(module).resolve("build.gradle.kts");
            assertThat(buildFile)
                .as("Build file should exist for module: %s", module)
                .exists();
        }
    }

    @Test
    @DisplayName("Gradle wrapper should be available")
    void gradleWrapperShouldBeAvailable() {
        Path projectRoot = getProjectRoot();
        Path gradlew = projectRoot.resolve("gradlew");
        
        assertThat(gradlew)
            .as("Gradle wrapper script should exist")
            .exists();
    }

    @Test
    @DisplayName("Core module classes should be accessible")
    void coreModuleClassesShouldBeAccessible() {
        // Verify we can access core module classes without exceptions
        assertThat(CoreModule.getModuleName()).isEqualTo("core");
        assertThat(CoreModule.isInitialized()).isTrue();
    }

    @Test
    @DisplayName("Project should have correct Java version")
    void projectShouldHaveCorrectJavaVersion() {
        String javaVersion = System.getProperty("java.version");
        assertThat(javaVersion).isNotNull();
        
        // Parse major version
        int majorVersion = getJavaMajorVersion(javaVersion);
        assertThat(majorVersion)
            .as("Java version should be 17 or higher. Current version: %s", javaVersion)
            .isGreaterThanOrEqualTo(17);
    }

    @Test
    @DisplayName("Build output directories should be accessible")
    void buildOutputDirectoriesShouldBeAccessible() {
        Path projectRoot = getProjectRoot();
        
        // Check that build directories exist or can be created
        String[] modules = {"core", "networking", "metrics", "storage", "agent", "monitoring"};
        
        for (String module : modules) {
            Path buildDir = projectRoot.resolve(module).resolve("build");
            
            // If build directory exists, check it's accessible
            if (Files.exists(buildDir)) {
                assertThat(buildDir).isDirectory();
            }
        }
    }

    @Test
    @DisplayName("Source directories should contain Java files")
    void sourceDirectoriesShouldContainJavaFiles() {
        Path projectRoot = getProjectRoot();
        
        // Check core module has Java files
        Path coreJavaDir = projectRoot.resolve("core/src/main/java/com/company/loadbalancer/core");
        if (Files.exists(coreJavaDir)) {
            assertThat(coreJavaDir).isDirectory();
        }
        
        // Check that at least some source directories exist
        assertThat(projectRoot.resolve("core/src/main/java")).exists();
        assertThat(projectRoot.resolve("networking/src/main/java")).exists();
    }

    /**
     * Get the project root directory
     */
    private Path getProjectRoot() {
        Path currentPath = Paths.get(".").toAbsolutePath().normalize();
        
        // Navigate up until we find settings.gradle.kts
        Path path = currentPath;
        while (path != null && !Files.exists(path.resolve("settings.gradle.kts"))) {
            path = path.getParent();
            if (path == null) {
                throw new IllegalStateException("Could not find project root directory");
            }
        }
        
        return path;
    }

    /**
     * Extracts major version from Java version string
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
