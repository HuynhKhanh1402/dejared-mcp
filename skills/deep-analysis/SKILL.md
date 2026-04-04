---
name: deep-analysis
description: >
  Deep-dive into a specific Java class from a JAR file.
  Combines metadata extraction with multi-engine decompilation.
  Use when investigating a single class in detail.
---

# Deep Class Analysis

Thorough analysis of a single Java class from a JAR file.

## Workflow

### Step 1: Metadata First
Call `dejared_get_metadata` to extract:
- Class hierarchy (superclass, interfaces)
- Annotations (`@Service`, `@Entity`, `@Controller`, etc.)
- Fields (state the class holds)
- Methods (behavior — names, return types, parameters)

This tells you the class's role and size before decompiling.

### Step 2: Decompile
Call `dejared_decompile_class` (default engine: CFR). Analyze:
- Constructor logic and dependency injection
- Method implementations and business logic
- Error handling patterns
- Interactions with other classes

### Step 3: Cross-Reference (if needed)
If decompiled code references important classes:
- `dejared_search_class` to find them by name
- `dejared_dump_package_metadata` on the same package to see sibling classes (accepts a list of packages)

### Step 4: Try Alternative Engine (if needed)
If CFR output has problems (broken lambdas, goto statements, placeholder bodies):
- `vineflower` — best for modern Java features
- `procyon` — good for edge cases

## Report Structure
1. **Role** — What this class does (one sentence)
2. **Framework Integration** — Annotations, Spring beans, injection points
3. **Key Methods** — Important methods and what they do
4. **Dependencies** — Other classes/services it uses
5. **Notable Patterns** — Design patterns, security concerns, performance notes

## Rules
- All tool calls require the **absolute path** to the JAR file
- ALWAYS call `dejared_get_metadata` before `dejared_decompile_class`
