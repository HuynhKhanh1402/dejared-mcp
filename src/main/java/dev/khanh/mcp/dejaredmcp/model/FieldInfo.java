package dev.khanh.mcp.dejaredmcp.model;

public record FieldInfo(String name, String type, String modifiers) {

    @Override
    public String toString() {
        return modifiers + " " + type + " " + name;
    }
}
