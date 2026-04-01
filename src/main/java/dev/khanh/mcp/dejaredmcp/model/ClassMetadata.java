package dev.khanh.mcp.dejaredmcp.model;

import java.util.List;

public record ClassMetadata(
        String className,
        String superClass,
        List<String> interfaces,
        List<String> annotations,
        List<FieldInfo> fields,
        List<MethodInfo> methods
) {

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("Class: ").append(className).append("\n");
        sb.append("Extends: ").append(superClass).append("\n");
        if (!interfaces.isEmpty()) {
            sb.append("Implements: ").append(String.join(", ", interfaces)).append("\n");
        }
        if (!annotations.isEmpty()) {
            sb.append("Annotations: ").append(String.join(", ", annotations)).append("\n");
        }
        sb.append("\nFields:\n");
        fields.forEach(f -> sb.append("  ").append(f).append("\n"));
        sb.append("\nMethods:\n");
        methods.forEach(m -> sb.append("  ").append(m).append("\n"));
        return sb.toString();
    }
}
