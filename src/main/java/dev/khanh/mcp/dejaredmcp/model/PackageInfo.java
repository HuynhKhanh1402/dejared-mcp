package dev.khanh.mcp.dejaredmcp.model;

public record PackageInfo(String packageName, int classCount) {

    @Override
    public String toString() {
        return packageName + " (" + classCount + " classes)";
    }
}
