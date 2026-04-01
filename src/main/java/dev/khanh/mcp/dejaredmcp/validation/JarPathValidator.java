package dev.khanh.mcp.dejaredmcp.validation;

import java.nio.file.Files;
import java.nio.file.Path;

public final class JarPathValidator {

    private JarPathValidator() {}

    /**
     * Validates the given JAR file path.
     * Returns null if valid, or an error message string if invalid.
     */
    public static String validate(String jarFilePath) {
        if (jarFilePath == null || jarFilePath.isBlank()) {
            return "Error: JAR file path is required.";
        }

        if (jarFilePath.contains("..")) {
            return "Error: Invalid path contains '..': " + jarFilePath;
        }

        Path path = Path.of(jarFilePath);

        if (!Files.exists(path)) {
            return "Error: File not found: " + jarFilePath;
        }

        if (!jarFilePath.toLowerCase().endsWith(".jar")) {
            return "Error: Not a .jar file: " + jarFilePath;
        }

        return null;
    }
}
