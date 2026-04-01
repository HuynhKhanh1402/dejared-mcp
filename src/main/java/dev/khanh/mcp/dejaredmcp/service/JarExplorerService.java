package dev.khanh.mcp.dejaredmcp.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

@Service
public class JarExplorerService {

    private static final Set<String> SUPPORTED_EXTENSIONS = Set.of(
            "yml", "yaml", "xml", "properties", "json", "txt", "sql", "conf"
    );

    public String listPackages(String jarFilePath) {
        try (var jarFile = new JarFile(jarFilePath)) {
            Map<String, Integer> packageCounts = new TreeMap<>();

            jarFile.stream()
                    .map(JarEntry::getName)
                    .filter(name -> name.endsWith(".class"))
                    .forEach(name -> {
                        int lastSlash = name.lastIndexOf('/');
                        String pkg = lastSlash > 0
                                ? name.substring(0, lastSlash).replace('/', '.')
                                : "(default package)";
                        packageCounts.merge(pkg, 1, Integer::sum);
                    });

            if (packageCounts.isEmpty()) {
                return "No packages found in " + jarFilePath;
            }

            return packageCounts.entrySet().stream()
                    .map(e -> e.getKey() + " (" + e.getValue() + " classes)")
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            return "Error: Failed to read JAR file: " + e.getMessage();
        }
    }

    public String listClasses(String jarFilePath, String packageName) {
        try (var jarFile = new JarFile(jarFilePath)) {
            String packagePath = packageName.replace('.', '/') + "/";

            List<String> classes = jarFile.stream()
                    .map(JarEntry::getName)
                    .filter(name -> name.endsWith(".class"))
                    .filter(name -> name.startsWith(packagePath))
                    .filter(name -> !name.substring(packagePath.length()).contains("/")) // direct children only
                    .map(name -> name.substring(0, name.length() - 6).replace('/', '.'))
                    .sorted()
                    .toList();

            if (classes.isEmpty()) {
                return "Error: Package '" + packageName + "' not found in " + jarFilePath;
            }

            return String.join("\n", classes);
        } catch (IOException e) {
            return "Error: Failed to read JAR file: " + e.getMessage();
        }
    }

    public String searchClass(String jarFilePath, String keyword) {
        try (var jarFile = new JarFile(jarFilePath)) {
            String lowerKeyword = keyword.toLowerCase();

            List<String> matches = jarFile.stream()
                    .map(JarEntry::getName)
                    .filter(name -> name.endsWith(".class"))
                    .map(name -> name.substring(0, name.length() - 6).replace('/', '.'))
                    .filter(name -> {
                        String simpleName = name.substring(name.lastIndexOf('.') + 1);
                        return simpleName.toLowerCase().contains(lowerKeyword);
                    })
                    .sorted()
                    .toList();

            if (matches.isEmpty()) {
                return "No classes found matching '" + keyword + "' in " + jarFilePath;
            }

            return String.join("\n", matches);
        } catch (IOException e) {
            return "Error: Failed to read JAR file: " + e.getMessage();
        }
    }

    public String readResource(String jarFilePath, String resourcePath) {
        String extension = getExtension(resourcePath);
        if (!SUPPORTED_EXTENSIONS.contains(extension)) {
            return "Error: Resource '" + resourcePath + "' not a supported text format (supported: "
                    + String.join(", ", SUPPORTED_EXTENSIONS) + ")";
        }

        try (var jarFile = new JarFile(jarFilePath)) {
            JarEntry entry = jarFile.getJarEntry(resourcePath);
            if (entry == null) {
                return "Error: Resource '" + resourcePath + "' not found in " + jarFilePath;
            }

            try (var is = jarFile.getInputStream(entry)) {
                return new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            return "Error: Failed to read resource: " + e.getMessage();
        }
    }

    private static String getExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        return dot >= 0 ? filename.substring(dot + 1).toLowerCase() : "";
    }
}
