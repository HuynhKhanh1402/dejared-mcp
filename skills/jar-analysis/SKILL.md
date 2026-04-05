---
name: jar-analysis
description: >
  Explore, search, and decompile Java JAR files. Quick use cases for
  common tasks (lookup, read config, get signatures) and workflows for
  deeper analysis (Explore, Hunt, Deep Analysis).
---

# JAR Analysis

## Available Tools

| Tool | Cost | Purpose |
|------|------|---------|
| `dejared_list_packages` | Cheap | List all packages with class counts |
| `dejared_list_classes` | Cheap | List classes in a package (`recursive=true` for sub-packages) |
| `dejared_list_resources` | Cheap | List all non-class resource files with sizes |
| `dejared_read_resource` | Cheap | Read a resource file (yml, properties, xml, json, txt, sql, conf) |
| `dejared_dump_package_metadata` | Cheap | Batch metadata for multiple packages at once (annotations, fields, methods via ASM) |
| `dejared_get_metadata` | Cheap | Single class metadata (ASM-based, no decompilation) |
| `dejared_search_class` | Cheap | Find classes by name keyword (case-insensitive) |
| `dejared_search_string` | Cheap | Find string literals in bytecode constant pools (case-insensitive) |
| `dejared_decompile_class` | **Expensive** | Full source code decompilation (CFR/vineflower/procyon) |

## Shared Rules

- All tool calls require the **absolute path** to the JAR file.
- NEVER decompile a class without checking its metadata first.
- Default decompiler: CFR. If output is broken, try `vineflower` or `procyon`.
- Report results in structured format.

---

## Quick Use Cases

**Quick Lookup** - find a class by name (`dejared_search_class`) or find a string in bytecode (`dejared_search_string`). One call, done.

**Read Config** - `dejared_list_resources` to see what's in the JAR, then `dejared_read_resource` to read it (application.yml, pom.xml, logback.xml, etc.)

**Get Signatures** - `dejared_get_metadata` for a single class, or `dejared_dump_package_metadata` for an entire package (accepts a list). Returns class hierarchy, annotations, fields, and method signatures without decompiling.

---

## Workflows

**Explore** - top-down JAR mapping:
`dejared_list_packages` → `dejared_list_classes` (use `recursive=true`) → `dejared_list_resources` → `dejared_read_resource` for configs → `dejared_dump_package_metadata` (batch all important packages in one call) → selective `dejared_decompile_class` only where method body logic is needed.

**Hunt** - search-driven investigation:
Pick search tool (`dejared_search_class` or `dejared_search_string`) → triage results → `dejared_get_metadata` on promising matches → `dejared_decompile_class` only if needed. Never skip the metadata step.

**Deep Analysis** - single class investigation:
`dejared_get_metadata` first → `dejared_decompile_class` (CFR default) → cross-reference with `dejared_search_class` or `dejared_dump_package_metadata` if needed. If CFR output has issues (broken lambdas, `goto` statements, missing method bodies), retry with `vineflower` (best for modern Java features like records, sealed classes, pattern matching) or `procyon` (handles some obfuscated/edge-case bytecode better).
