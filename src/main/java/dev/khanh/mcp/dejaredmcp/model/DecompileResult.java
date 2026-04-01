package dev.khanh.mcp.dejaredmcp.model;

public record DecompileResult(boolean success, String engineUsed, String content, String error) {

    public static DecompileResult ok(String engineUsed, String sourceCode) {
        return new DecompileResult(true, engineUsed, sourceCode, null);
    }

    public static DecompileResult fail(String engineUsed, String error) {
        return new DecompileResult(false, engineUsed, null, error);
    }
}
