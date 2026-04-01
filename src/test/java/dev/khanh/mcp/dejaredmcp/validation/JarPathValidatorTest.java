package dev.khanh.mcp.dejaredmcp.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class JarPathValidatorTest {

    @TempDir
    Path tempDir;

    @Test
    void validJarPath() throws IOException {
        Path jar = tempDir.resolve("test.jar");
        Files.write(jar, new byte[]{0x50, 0x4B, 0x03, 0x04}); // ZIP magic bytes
        String result = JarPathValidator.validate(jar.toString());
        assertNull(result, "Valid path should return null");
    }

    @Test
    void rejectsNonExistentFile() {
        String result = JarPathValidator.validate("/nonexistent/file.jar");
        assertNotNull(result);
        assertTrue(result.contains("File not found"));
    }

    @Test
    void rejectsNonJarExtension() throws IOException {
        Path txt = tempDir.resolve("file.txt");
        Files.writeString(txt, "hello");
        String result = JarPathValidator.validate(txt.toString());
        assertNotNull(result);
        assertTrue(result.contains("Not a .jar file"));
    }

    @Test
    void rejectsPathTraversal() {
        String result = JarPathValidator.validate("/some/../etc/passwd.jar");
        assertNotNull(result);
        assertTrue(result.contains(".."));
    }

    @Test
    void rejectsNullPath() {
        String result = JarPathValidator.validate(null);
        assertNotNull(result);
    }

    @Test
    void rejectsEmptyPath() {
        String result = JarPathValidator.validate("");
        assertNotNull(result);
    }
}
