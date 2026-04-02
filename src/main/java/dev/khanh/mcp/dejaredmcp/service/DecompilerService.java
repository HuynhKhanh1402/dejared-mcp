package dev.khanh.mcp.dejaredmcp.service;

import dev.khanh.mcp.dejaredmcp.decompiler.DecompilerEngine;
import dev.khanh.mcp.dejaredmcp.model.DecompileResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.jar.JarFile;

@Service
public class DecompilerService {

    private final Map<String, DecompilerEngine> engines;
    private final Map<String, String> cache;

    public DecompilerService(List<DecompilerEngine> engineList,
                             @Value("${dejared.cache.max-size:500}") int maxCacheSize) {
        this.engines = new HashMap<>();
        for (DecompilerEngine engine : engineList) {
            this.engines.put(engine.name(), engine);
        }

        this.cache = Collections.synchronizedMap(new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > maxCacheSize;
            }
        });
    }

    public String decompile(String jarFilePath, String className, String engine) {
        String engineName = (engine == null || engine.isBlank()) ? "cfr" : engine.toLowerCase();

        DecompilerEngine decompilerEngine = engines.get(engineName);
        if (decompilerEngine == null) {
            return "Error: Unknown engine '" + engine + "'. Available engines: " + String.join(", ", engines.keySet());
        }

        String cacheKey = jarFilePath + ":" + className + ":" + engineName;
        String cached = cache.get(cacheKey);
        if (cached != null) {
            return cached;
        }

        // Extract class bytes from JAR
        String classPath = className.replace('.', '/') + ".class";
        byte[] classBytes;
        try (var jarFile = new JarFile(jarFilePath)) {
            var entry = jarFile.getJarEntry(classPath);
            if (entry == null) {
                return "Error: Class '" + className + "' not found in " + jarFilePath;
            }
            try (var is = jarFile.getInputStream(entry)) {
                classBytes = is.readAllBytes();
            }
        } catch (IOException e) {
            return "Error: Failed to read JAR file: " + e.getMessage();
        }

        DecompileResult result = decompilerEngine.decompile(classBytes, className);

        if (result.success()) {
            String output = result.content();
            cache.put(cacheKey, output);
            return output;
        } else {
            List<String> alternatives = engines.keySet().stream()
                    .filter(name -> !name.equals(engineName))
                    .sorted()
                    .toList();
            return "Error: " + result.error()
                    + ". Try again with engine=" + String.join(" or engine=", alternatives) + ".";
        }
    }
}
