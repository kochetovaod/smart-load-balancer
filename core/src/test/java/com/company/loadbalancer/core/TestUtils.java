package com.company.loadbalancer.core;

import java.io.*;
import java.nio.file.*;
import java.util.concurrent.*;

/**
 * Utility class for integration tests
 */
public final class TestUtils {
    
    private TestUtils() {
        // Utility class
    }
    
    /**
     * Execute command and return result
     */
    public static ProcessResult executeCommand(Path workingDir, String... command) 
            throws IOException, InterruptedException, TimeoutException {
        
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(workingDir.toFile());
        processBuilder.redirectErrorStream(true);
        
        Process process = processBuilder.start();
        
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        
        boolean completed = process.waitFor(5, TimeUnit.MINUTES);
        if (!completed) {
            process.destroyForcibly();
            throw new TimeoutException("Command execution timed out: " + String.join(" ", command));
        }
        
        return new ProcessResult(process.exitValue(), output.toString());
    }
    
    /**
     * Check if file exists and is not empty
     */
    public static boolean isFileValid(Path filePath) {
        return Files.exists(filePath) && Files.isRegularFile(filePath) && 
               filePath.toFile().length() > 0;
    }
    
    /**
     * Get project root directory
     */
    public static Path getProjectRoot() {
        Path currentPath = Paths.get(".").toAbsolutePath().normalize();
        
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
     * Result of process execution
     */
    public static class ProcessResult {
        private final int exitCode;
        private final String output;
        
        public ProcessResult(int exitCode, String output) {
            this.exitCode = exitCode;
            this.output = output;
        }
        
        public int getExitCode() {
            return exitCode;
        }
        
        public String getOutput() {
            return output;
        }
        
        public boolean isSuccess() {
            return exitCode == 0;
        }
    }
}
