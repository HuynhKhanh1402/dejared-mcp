package dev.khanh.mcp.dejaredmcp.model;

import java.util.List;

public record MethodInfo(String name, String returnType, List<String> parameterTypes, String modifiers) {

    @Override
    public String toString() {
        return modifiers + " " + returnType + " " + name + "(" + String.join(", ", parameterTypes) + ")";
    }
}
