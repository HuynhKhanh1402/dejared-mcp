[![npm version](https://img.shields.io/npm/v/dejared-mcp?style=flat-square)](https://www.npmjs.com/package/dejared-mcp)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue?style=flat-square)](LICENSE)
[![Node.js](https://img.shields.io/badge/Node.js-18%2B-339933?style=flat-square&logo=node.js&logoColor=white)]()
[![Java](https://img.shields.io/badge/Java-21%2B-ED8B00?style=flat-square&logo=openjdk&logoColor=white)]()

# dejared-mcp

A Model Context Protocol (MCP) server for exploring, analyzing, and decompiling Java JAR files.
Designed for AI-powered development tools, dejared-mcp connects your IDE or CLI assistant
to inspect package structures, search bytecode, and decompile classes
using CFR, Vineflower, or Procyon.

## Table of Contents

- [Overview](#overview)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Features](#features)
- [Decompiler Engines](#decompiler-engines)
- [Installation and Configuration](#installation-and-configuration)
- [Custom Java Path](#custom-java-path)
- [Server Configuration](#server-configuration)
- [How It Works](#how-it-works)
- [FAQ and Troubleshooting](#faq-and-troubleshooting)
- [Contributing](#contributing)
- [Third-Party Licenses](#third-party-licenses)
- [License](#license)

## Overview

dejared-mcp is a Java-based MCP server distributed as an npm package.
It provides nine tools organized into three categories: discovery, hunting,
and deep analysis. AI assistants use these tools to navigate JAR file
structures, search for classes and string literals in bytecode, and
decompile `.class` files back to readable Java source code.

The npm package acts as a thin wrapper that downloads and caches the
server JAR on first run, then spawns it via `java -jar` using stdio
transport.

## Prerequisites

- **Node.js** 18 or later
- **Java** 21 or later (JRE is sufficient)

## Quick Start

Add the following to your MCP client configuration:

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

See [Installation and Configuration](#installation-and-configuration)
for tool-specific instructions.

## Features

### Discovery

Browse and read JAR contents.

| Tool | Description |
|------|-------------|
| `dejared_list_packages` | List all packages with class counts |
| `dejared_list_classes` | List classes in a specific package |
| `dejared_list_resources` | List non-class resource files |
| `dejared_read_resource` | Read text resources (YAML, XML, properties, JSON, and others) |

### Hunting

Search inside JAR files.

| Tool | Description |
|------|-------------|
| `dejared_search_class` | Search classes by name |
| `dejared_search_string` | Search string literals in bytecode (URLs, SQL, error messages) |

### Deep Analysis

Inspect metadata and decompile classes.

| Tool | Description |
|------|-------------|
| `dejared_get_metadata` | Extract class metadata via ASM (fast, no decompilation) |
| `dejared_dump_package_metadata` | Batch metadata extraction for entire packages |
| `dejared_decompile_class` | Decompile `.class` files to Java source code |

## Decompiler Engines

dejared-mcp supports three decompiler engines. The engine can be
specified per request via the `dejared_decompile_class` tool.

| Engine | Description |
|--------|-------------|
| **CFR** (default) | Reliable general-purpose decompiler |
| **Vineflower** | Modern fork of FernFlower, handles newer Java features well |
| **Procyon** | Alternative engine, can handle some edge cases better |

## Installation and Configuration

The standard configuration below works with most MCP-compatible tools.
Expand the relevant section for tool-specific instructions.

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

<details>
<summary><strong>Amp</strong></summary>

Add via the Amp VS Code extension settings screen or by updating your `settings.json` file:

```json
"amp.mcpServers": {
  "dejared": {
    "command": "npx",
    "args": ["-y", "dejared-mcp"]
  }
}
```

**Amp CLI:**

```bash
amp mcp add dejared -- npx -y dejared-mcp
```

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
<summary><strong>Claude Code</strong></summary>

```bash
claude mcp add dejared -- npx -y dejared-mcp
```

Or add it to your project's `.mcp.json` using the standard config above.

**Plugin (Marketplace)** installs both the MCP server and the `/jar-analysis` skill:

```bash
claude plugin marketplace add HuynhKhanh1402/dejared-mcp
claude plugin install dejared@dejared-mcp-marketplace
```

</details>

<details>
<summary><strong>Claude Desktop</strong></summary>

Edit the Claude Desktop config file:

- **macOS**: `~/Library/Application Support/Claude/claude_desktop_config.json`
- **Windows**: `%APPDATA%\Claude\claude_desktop_config.json`

Add the standard config above and restart Claude Desktop.

</details>

<details>
<summary><strong>Cline</strong></summary>

Add the standard config above to your MCP settings file.

</details>

<details>
<summary><strong>Codex</strong></summary>

```bash
codex mcp add dejared npx "-y dejared-mcp"
```

Or edit `~/.codex/config.toml`:

```toml
[mcp_servers.dejared]
command = "npx"
args = ["-y", "dejared-mcp"]
```

</details>

<details>
<summary><strong>Copilot CLI</strong></summary>

Interactive:

```
/mcp add
```

Then fill in:
- **Server Name**: `dejared`
- **Server Type**: `STDIO`
- **Command**: `npx -y dejared-mcp`

Or edit `~/.copilot/mcp-config.json`:

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
<summary><strong>Cursor</strong></summary>

Create or edit `.cursor/mcp.json` in your project root (project-level) or `~/.cursor/mcp.json` (global) using the standard config above.

</details>

<details>
<summary><strong>Gemini CLI</strong></summary>

```bash
gemini mcp add dejared npx -y dejared-mcp
```

Or edit `~/.gemini/settings.json` (global) or `.gemini/settings.json` (project) using the standard config above.

Use `/mcp` in a Gemini CLI session to verify the server is connected.

</details>

<details>
<summary><strong>Goose</strong></summary>

Go to **Advanced settings** > **Extensions** > **Add custom extension**. Name to your liking, use type `STDIO`, and set the command to `npx -y dejared-mcp`.

</details>

<details>
<summary><strong>JetBrains IDEs</strong></summary>

Go to **Settings** > **Tools** > **AI Assistant** > **Model Context Protocol (MCP)**.

Click **+ Add** and configure:

- **Name**: `dejared`
- **Transport**: `Stdio`
- **Command**: `npx`
- **Arguments**: `-y dejared-mcp`

</details>

<details>
<summary><strong>Kiro</strong></summary>

Create or edit `.kiro/settings/mcp.json` using the standard config above.

</details>

<details>
<summary><strong>opencode</strong></summary>

Edit `~/.config/opencode/opencode.json`:

```json
{
  "$schema": "https://opencode.ai/config.json",
  "mcp": {
    "dejared": {
      "type": "local",
      "command": ["npx", "-y", "dejared-mcp"],
      "enabled": true
    }
  }
}
```

</details>

<details>
<summary><strong>Qodo Gen</strong></summary>

Open [Qodo Gen](https://docs.qodo.ai/qodo-documentation/qodo-gen) chat panel in VS Code or IntelliJ > Connect more tools > + Add new MCP > Paste the standard config above > Save.

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

After saving, click the **Start** button that appears in the MCP config file, then use Agent mode in Copilot Chat.

</details>

<details>
<summary><strong>Windsurf</strong></summary>

Open Windsurf settings, navigate to MCP servers, and add a new server using the `command` type with `npx -y dejared-mcp`. Or add the standard config under `mcpServers` in your settings.

</details>

<details>
<summary><strong>Pure Java (no Node.js required)</strong></summary>

If you prefer to run the server JAR directly without Node.js:

1. Download the latest JAR from [GitHub Releases](https://github.com/HuynhKhanh1402/dejared-mcp/releases).

2. Run it:
   ```bash
   java -jar dejared-mcp-0.1.3.jar
   ```

3. Configure your MCP client to use the JAR directly instead of `npx`:

   ```json
   {
     "mcpServers": {
       "dejared": {
         "command": "java",
         "args": ["-jar", "/path/to/dejared-mcp-0.1.3.jar"]
       }
     }
   }
   ```

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

## Server Configuration

| Property | Default | Description |
|----------|---------|-------------|
| `dejared.cache.max-size` | `500` | Max entries in the decompilation LRU cache |
| `dejared.security.max-resource-size` | `5242880` | Max resource file size (bytes) |
| `dejared.security.decompile-timeout-seconds` | `30` | Timeout per decompilation |

## How It Works

The npm package is a thin Node.js wrapper. On first run it:

1. Checks the platform cache directory for a cached JAR matching the current version.
   - **Linux**: `$XDG_CACHE_HOME/dejared-mcp` (defaults to `~/.cache/dejared-mcp`)
   - **macOS**: `~/Library/Caches/dejared-mcp`
   - **Windows**: `%LOCALAPPDATA%\dejared-mcp`
2. Downloads the JAR from GitHub Releases if not cached.
3. Spawns `java -jar` with stdio inherited for MCP transport.

The server communicates over stdio using the Model Context Protocol.

## FAQ and Troubleshooting

**Q: Java is installed but the server cannot find it.**

Set the `DEJARED_JAVA_PATH` environment variable in your MCP
configuration. See [Custom Java Path](#custom-java-path).

**Q: The server fails to start with a permission error.**

Ensure that the cached JAR file is readable. The cache location
depends on your platform. See [How It Works](#how-it-works) for
the cache directory paths.

**Q: Decompilation times out or returns an error.**

Some classes are difficult to decompile. Try a different engine
by specifying `vineflower` or `procyon` in the `dejared_decompile_class`
tool. The default timeout is 30 seconds and can be adjusted via
`dejared.security.decompile-timeout-seconds`.

**Q: Which Java version do I need?**

Java 21 or later. A JRE is sufficient; you do not need a full JDK.

**Q: Can I run the server without Node.js?**

Yes. Download the JAR from GitHub Releases and run it directly
with `java -jar`. See the "Pure Java" section under
[Installation and Configuration](#installation-and-configuration).

## Contributing

Contributions are welcome. Please follow these guidelines:

1. Fork the repository and create a feature branch from `master`.
2. Ensure your changes compile and pass existing tests.
3. Write clear commit messages describing the purpose of each change.
4. Open a pull request against `master` with a description of what
   the change does and why.

### Project Structure

```
dejared-mcp/
├── bin/            # Node.js CLI entry point
├── lib/            # Node.js wrapper (JAR download and process management)
├── java-mcp/       # Java MCP server (Spring Boot, Gradle)
│   └── src/
├── skills/         # Claude Code plugin skills
├── package.json    # npm package manifest
└── server.json     # MCP Registry manifest
```

### Building from Source

```bash
cd java-mcp
./gradlew build
```

### Reporting Issues

Open an issue on [GitHub Issues](https://github.com/HuynhKhanh1402/dejared-mcp/issues)
with steps to reproduce the problem, your Java version, and your
Node.js version.

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

This project is licensed under the [MIT License](LICENSE).
