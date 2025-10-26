package com.company.loadbalancer.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for verifying complete project build
 */
@DisplayName("Project Build Integration Test")
class ProjectBuildTest {

    @TempDir
    static Path tempDir;

    @Test
    @DisplayName("Full project build should complete successfully")
    void fullProjectBuildShouldCompleteSuccessfully() throws Exception {
        // Get project root directory
        Path projectRoot = getProjectRoot();
        
        // Execute gradle build
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(projectRoot.toFile());
        processBuilder.command("./gradlew", "build", "--no-daemon", "--stacktrace", "--info");
        
        // Capture output
        Process process = processBuilder.start();
        
        // Read output in separate threads to prevent blocking
        StringBuilder output = new StringBuilder();
        StringBuilder error = new StringBuilder();
        
        Thread outputThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                    System.out.println("[BUILD] " + line);
                }
            } catch (IOException e) {
                error.append("Error reading output: ").append(e.getMessage());
            }
        });
        
        Thread errorThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    error.append(line).append("\n");
                    System.err.println("[BUILD-ERROR] " + line);
                }
            } catch (IOException e) {
                error.append("Error reading error stream: ").append(e.getMessage());
            }
        });
        
        outputThread.start();
        errorThread.start();
        
        // Wait for process completion with timeout
        boolean completed = process.waitFor(5, TimeUnit.MINUTES);
        
        // Wait for output threads to finish
        outputThread.join(5000);
        errorThread.join(5000);
        
        // Verify build completed successfully
        assertThat(completed)
            .as("Build process should complete within timeout")
            .isTrue();
            
        int exitCode = process.exitValue();
        assertThat(exitCode)
            .as("Build should exit with code 0. Output: %s\nError: %s", output, error)
            .isEqualTo(0);
    }

    @Test
    @DisplayName("All module JAR files should be created after build")
    void allModuleJarFilesShouldBeCreatedAfterBuild() {
        Path projectRoot = getProjectRoot();
        
        // Check for JAR files in each module
        String[] modules = {"core", "networking", "metrics", "storage", "agent", "monitoring"};
        
        for (String module : modules) {
            Path jarPath = projectRoot.resolve(module).resolve("build/libs")
                .resolve(module + "-1.0.0-SNAPSHOT.jar");
            
            assertThat(jarPath)
                .as("JAR file should exist for module: %s", module)
                .exists();
                
            assertThat(jarPath.toFile().length())
                .as("JAR file should not be empty for module: %s", module)
                .isGreaterThan(0);
        }
    }

    @Test
    @DisplayName("Gradle wrapper should be executable")
    void gradleWrapperShouldBeExecutable() {
        Path projectRoot = getProjectRoot();
        Path gradlew = projectRoot.resolve("gradlew");
        
        assertThat(gradlew)
            .as("Gradle wrapper script should exist")
            .exists();
            
        File gradlewFile = gradlew.toFile();
        assertThat(gradlewFile.canExecute())
            .as("Gradle wrapper should be executable")
            .isTrue();
    }

    @Test
    @DisplayName("Project structure should be valid")
    void projectStructureShouldBeValid() {
        Path projectRoot = getProjectRoot();
        
        // Check essential directories
        assertThat(projectRoot.resolve("src")).doesNotExist(); // No src in root
        assertThat(projectRoot.resolve("core/src/main/java")).exists();
        assertThat(projectRoot.resolve("networking/src/main/java")).exists();
        assertThat(projectRoot.resolve("metrics/src/main/java")).exists();
        assertThat(projectRoot.resolve("storage/src/main/java")).exists();
        
        // Check build files
        assertThat(projectRoot.resolve("build.gradle.kts")).exists();
        assertThat(projectRoot.resolve("settings.gradle.kts")).exists();
        assertThat(projectRoot.resolve("gradle.properties")).exists();
    }

    @Test
    @DisplayName("Dependency resolution should work without conflicts")
    void dependencyResolutionShouldWorkWithoutConflicts() throws Exception {
        Path projectRoot = getProjectRoot();
        
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(projectRoot.toFile());
        processBuilder.command("./gradlew", "dependencies", "--no-daemon", "--configuration", "compileClasspath");
        
        Process process = processBuilder.start();
        
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        
        boolean completed = process.waitFor(2, TimeUnit.MINUTES);
        assertThat(completed).isTrue();
        assertThat(process.exitValue()).isEqualTo(0);
        
        String dependenciesOutput = output.toString();
        
        // Check for common conflict indicators
        assertThat(dependenciesOutput)
            .as("Dependency output should not contain conflict warnings")
            .doesNotContain("Conflict")
            .doesNotContain("different versions")
            .doesNotContain("omitted for conflict");
    }

    @Test
    @DisplayName("Test compilation should succeed for all modules")
    void testCompilationShouldSucceedForAllModules() throws Exception {
        Path projectRoot = getProjectRoot();
        
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(projectRoot.toFile());
        processBuilder.command("./gradlew", "compileTestJava", "--no-daemon", "--parallel");
        
        Process process = processBuilder.start();
        
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        
        boolean completed = process.waitFor(3, TimeUnit.MINUTES);
        assertThat(completed).isTrue();
        
        assertThat(process.exitValue())
            .as("Test compilation should succeed. Output: %s", output)
            .isEqualTo(0);
    }

    @Test
    @DisplayName("All tests should pass")
    void allTestsShouldPass() throws Exception {
        Path projectRoot = getProjectRoot();
        
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(projectRoot.toFile());
        processBuilder.command("./gradlew", "test", "--no-daemon", "--continue");
        
        Process process = processBuilder.start();
        
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        
        boolean completed = process.waitFor(5, TimeUnit.MINUTES);
        assertThat(completed).isTrue();
        
        assertThat(process.exitValue())
            .as("All tests should pass. Output: %s", output)
            .isEqualTo(0);
            
        // Verify test reports were generated
        String[] modules = {"core", "networking", "metrics", "storage"};
        for (String module : modules) {
            Path testReport = projectRoot.resolve(module).resolve("build/reports/tests/test");
            assertThat(testReport)
                .as("Test report should exist for module: %s", module)
                .exists();
        }
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
}
