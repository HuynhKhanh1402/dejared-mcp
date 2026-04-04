---
name: hunt-in-jar
description: >
  Search within a Java JAR file for specific classes, strings, or patterns.
  Uses class name search and bytecode string search to locate relevant code.
  Use when looking for specific functionality, URLs, SQL, error messages, or credentials.
---

# Hunting in JARs

Search for specific code inside a JAR, then inspect findings with metadata before decompiling.

## Search Tools

### `dejared_search_class` — Find by class name
Case-insensitive keyword match against simple class names.
- Find specific classes: "UserService", "DatabaseConfig"
- Find patterns: "Controller", "Repository", "Factory", "Handler"

### `dejared_search_string` — Find by string literal
Scans bytecode constant pools across all classes (case-insensitive).
- URLs, endpoints, API paths
- SQL queries, table names
- Error messages, log messages
- Config keys, credentials, secrets
- Useful keywords for security audits: "password", "secret", "token", "key", "jdbc", "http://"

## Workflow

1. **Search** — Pick the right search tool based on the user's goal
2. **Triage** — Review results, identify most relevant matches
3. **Inspect** — Call `dejared_get_metadata` on promising classes to understand their role (annotations, fields, methods)
4. **Decompile** — Call `dejared_decompile_class` ONLY on classes where you need to see method body logic. Never skip step 3
5. **Report** — Summarize findings with context and implications

## Rules
- All tool calls require the **absolute path** to the JAR file
- String search finds compiled constants only, not runtime-generated strings
- NEVER decompile without checking metadata first — metadata tells you class size and role
- Default decompiler: CFR. Alternatives: `vineflower`, `procyon`
