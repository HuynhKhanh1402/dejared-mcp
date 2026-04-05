# dejared-mcp

An MCP (Model Context Protocol) server that lets AI assistants explore, analyze, and decompile Java JAR files.

## Prerequisites

- **Node.js 18+** - required for the `npx` wrapper (not needed for pure Java usage)
- **Java 21+** - JRE is sufficient

## Quick Start

Most MCP-compatible tools use a JSON configuration like this:

```json
{
  "mcpServers": {
    "dejared": {
      "command": "npx",
      "args": ["-y", "dejared-mcp"]
    }
  }
}
```

See below for tool-specific instructions.

## MCP Registry

This server is published to the [MCP Registry](https://registry.modelcontextprotocol.io) as `io.github.huynhkhanh1402/dejared`.

You can verify it via the registry API:

```bash
curl "https://registry.modelcontextprotocol.io/v0.1/servers?search=io.github.huynhkhanh1402/dejared"
```

## Configuration by Tool

<details>
<summary><strong>Claude Desktop</strong></summary>

Edit the Claude Desktop config file:

- **macOS**: `~/Library/Application Support/Claude/claude_desktop_config.json`
- **Windows**: `%APPDATA%\Claude\claude_desktop_config.json`

```json
{
  "mcpServers": {
    "dejared": {
      "command": "npx",
      "args": ["-y", "dejared-mcp"]
    }
  }
}
```

Restart Claude Desktop after saving.

</details>

<details>
<summary><strong>Claude Code (CLI)</strong></summary>

Run this command in your terminal:

```bash
claude mcp add dejared -- npx -y dejared-mcp
```

Or add it to your project's `.mcp.json`:

```json
{
  "mcpServers": {
    "dejared": {
      "command": "npx",
      "args": ["-y", "dejared-mcp"]
    }
  }
}
```

</details>

<details>
<summary><strong>Cursor</strong></summary>

Create or edit `.cursor/mcp.json` in your project root (project-level) or `~/.cursor/mcp.json` (global):

```json
{
  "mcpServers": {
    "dejared": {
      "command": "npx",
      "args": ["-y", "dejared-mcp"]
    }
  }
}
```

</details>

<details>
<summary><strong>VS Code (GitHub Copilot)</strong></summary>

Create or edit `.vscode/mcp.json` in your project root:

```json
{
  "servers": {
    "dejared": {
      "command": "npx",
      "args": ["-y", "dejared-mcp"]
    }
  }
}
```

Or add to your VS Code `settings.json` for global availability.

After saving, click the **Start** button that appears in the MCP config file, then use Agent mode in Copilot Chat.

</details>

<details>
<summary><strong>JetBrains IDEs (GitHub Copilot)</strong></summary>

Go to **Settings** > **Tools** > **AI Assistant** > **Model Context Protocol (MCP)**.

Click **+ Add** and configure:

- **Name**: `dejared`
- **Transport**: `Stdio`
- **Command**: `npx`
- **Arguments**: `-y dejared-mcp`

</details>

<details>
<summary><strong>Gemini CLI</strong></summary>

**Option A** - CLI command:

```bash
gemini mcp add dejared npx -y dejared-mcp
```

**Option B** - Edit `~/.gemini/settings.json` (global) or `.gemini/settings.json` (project):

```json
{
  "mcpServers": {
    "dejared": {
      "command": "npx",
      "args": ["-y", "dejared-mcp"]
    }
  }
}
```

Use `/mcp` in a Gemini CLI session to verify the server is connected.

</details>

<details>
<summary><strong>Antigravity Editor</strong></summary>

Edit `~/.gemini/antigravity/mcp_config.json`:

```json
{
  "mcpServers": {
    "dejared": {
      "command": "npx",
      "args": ["-y", "dejared-mcp"]
    }
  }
}
```

Or install through the MCP Store if available.

</details>

<details>
<summary><strong>GitHub Copilot CLI</strong></summary>

**Option A** - Interactive:

```
/mcp add
```

Then fill in:
- **Server Name**: `dejared`
- **Server Type**: `STDIO`
- **Command**: `npx -y dejared-mcp`

**Option B** - Edit `~/.copilot/mcp-config.json`:

```json
{
  "mcpServers": {
    "dejared": {
      "type": "local",
      "command": "npx",
      "args": ["-y", "dejared-mcp"],
      "tools": ["*"]
    }
  }
}
```

</details>

<details>
<summary><strong>Pure Java (no Node.js required)</strong></summary>

If you prefer to run the server JAR directly without Node.js:

1. Download the latest JAR from [GitHub Releases](https://github.com/HuynhKhanh1402/dejared-mcp/releases).

   Direct link pattern:
   ```
   https://github.com/HuynhKhanh1402/dejared-mcp/releases/download/v{VERSION}/dejared-mcp-{VERSION}.jar
   ```

2. Run it:
   ```bash
   java -jar dejared-mcp-0.1.1.jar
   ```

3. Configure your MCP client to use the JAR directly instead of `npx`. For example, in Claude Desktop:

   ```json
   {
     "mcpServers": {
       "dejared": {
         "command": "java",
         "args": ["-jar", "/path/to/dejared-mcp-0.1.1.jar"]
       }
     }
   }
   ```

   Replace `/path/to/dejared-mcp-0.1.1.jar` with the actual path where you saved the JAR. This works with any MCP client that supports stdio transport.

</details>

## Custom Java Path

If Java is not in your system PATH, set the `DEJARED_JAVA_PATH` environment variable in your MCP config. This applies to all `npx`-based configurations:

```json
{
  "mcpServers": {
    "dejared": {
      "command": "npx",
      "args": ["-y", "dejared-mcp"],
      "env": {
        "DEJARED_JAVA_PATH": "/path/to/java"
      }
    }
  }
}
```

## Features

**Discovery** - Browse JAR structure:
- `dejared_list_packages` - List all packages with class counts
- `dejared_list_classes` - List classes in a specific package
- `dejared_list_resources` - List non-class resource files
- `dejared_read_resource` - Read text resources (yml, xml, properties, json, etc.)

**Hunting** - Search inside JARs:
- `dejared_search_class` - Search classes by name
- `dejared_search_string` - Search string literals in bytecode (URLs, SQL, error messages, etc.)

**Deep Analysis** - Inspect and decompile:
- `dejared_get_metadata` - Extract class metadata via ASM (fast, no decompilation)
- `dejared_dump_package_metadata` - Batch metadata for entire packages
- `dejared_decompile_class` - Decompile `.class` to Java source code

## Decompiler Engines

| Engine | Description |
|--------|-------------|
| **CFR** (default) | Reliable general-purpose decompiler |
| **Vineflower** | Modern fork of FernFlower, good with newer Java features |
| **Procyon** | Alternative engine, can handle some edge cases better |

## How It Works

The npm package is a thin Node.js wrapper. On first run it:
1. Checks the platform cache directory for a cached JAR matching the current version
   - Linux: `$XDG_CACHE_HOME/dejared-mcp` (default `~/.cache/dejared-mcp`)
   - macOS: `~/Library/Caches/dejared-mcp`
   - Windows: `%LOCALAPPDATA%\dejared-mcp`
2. Downloads the JAR from GitHub Releases if not cached
3. Spawns `java -jar` with stdio inherited for MCP transport

The server communicates over stdio.

## Server Configuration

| Property | Default | Description |
|----------|---------|-------------|
| `dejared.cache.max-size` | `500` | Max entries in the decompilation LRU cache |
| `dejared.security.max-resource-size` | `5242880` | Max resource file size (bytes) |
| `dejared.security.decompile-timeout-seconds` | `30` | Timeout per decompilation |

## Third-Party Licenses

This project uses the following open-source libraries:

| Library | License |
|---------|---------|
| [Spring Boot](https://spring.io/projects/spring-boot) | Apache 2.0 |
| [Spring AI](https://spring.io/projects/spring-ai) | Apache 2.0 |
| [ASM](https://asm.ow2.io/) | BSD 3-Clause |
| [CFR](https://github.com/leibnitz27/cfr) | MIT |
| [Vineflower](https://github.com/Vineflower/vineflower) | Apache 2.0 |
| [Procyon](https://github.com/mstrobel/procyon) | Apache 2.0 |

## License

[MIT](LICENSE)
