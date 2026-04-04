---
name: analyze-jar
description: >
  Systematically explore a Java JAR file structure. Top-down approach:
  packages -> classes -> resources -> metadata -> selective decompilation.
  Use when exploring unknown JARs or understanding JAR architecture.
---

# Systematic JAR Exploration

Follow this workflow to analyze a JAR file incrementally. Use cheap tools first — save decompilation for last.

## Available Tools

| Tool | Cost | Purpose |
|------|------|---------|
| `dejared_list_packages` | Cheap | List all packages with class counts |
| `dejared_list_classes` | Cheap | List classes in a package (`recursive=true` for sub-packages) |
| `dejared_list_resources` | Cheap | List all non-class resource files with sizes |
| `dejared_read_resource` | Cheap | Read a resource file (yml, properties, xml, json, txt, sql, conf) |
| `dejared_dump_package_metadata` | Cheap | Batch metadata for multiple packages at once (annotations, fields, methods via ASM) |
| `dejared_get_metadata` | Cheap | Single class metadata (ASM-based, no decompilation) |
| `dejared_search_class` | Cheap | Find classes by name keyword |
| `dejared_search_string` | Cheap | Find string literals in bytecode constant pools |
| `dejared_decompile_class` | **Expensive** | Full source code decompilation (CFR/vineflower/procyon) |

## Workflow

### Step 1: Map Package Structure
Call `dejared_list_packages` to get the full layout.
- Identify the root application package (highest class count, deepest nesting)
- Distinguish app code from shaded/third-party dependencies

### Step 2: List Classes
Call `dejared_list_classes` with `recursive=true` on the root application package to get all classes in one call.
- For targeted exploration of specific packages, use `recursive=false`

### Step 3: Discover Resources
Call `dejared_list_resources` to see what non-class files exist in the JAR.
- No more guessing names — you see exactly which config files are available

### Step 4: Read Configuration
Call `dejared_read_resource` for files discovered in Step 3:
- `application.yml` / `application.yaml` / `application.properties`
- `META-INF/spring.factories` or `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
- `logback.xml` / `log4j2.xml`

### Step 5: Batch Metadata
Call `dejared_dump_package_metadata` with **all important packages at once** (it accepts a list).
Example: `packageNames: ["com.example.service", "com.example.config", "com.example.controller"]`
- Returns annotations, fields, and method signatures for every class
- One call replaces dozens of individual `dejared_get_metadata` calls

### Step 6: Selective Decompilation
Call `dejared_decompile_class` ONLY for classes where you need method body logic.
- You MUST have reviewed metadata from Step 5 first
- Default engine: CFR. If output is broken, try `vineflower` or `procyon`
- Decompile one class at a time; summarize before continuing

## Rules
- All tool calls require the **absolute path** to the JAR file
- NEVER decompile a class without checking its metadata first
- For JARs with >20 packages, group by prefix and summarize before diving in
- If the user asks about a specific feature, switch to the hunt-in-jar skill
