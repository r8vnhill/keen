 # ChatGPT Rules

This chat is about keen, an evolutionary computation framework for kotlin

## Rules

### 1. Markdown Formatting for Code Snippets and Documentation

When writing code snippets and documentation, **ALWAYS** format the code using proper Markdown 
syntax and enclose all code with

```kotlin
// ...
```

to make it more readable and easier to understand.

### 2. Consistency and Professionalism in Documentation

When writing documentation, **ALWAYS** answer accordingly to the first rule to maintain 
consistency and professionalism.

### 3. Preferred Use of @property Tag for Documenting Public Variables

When documenting classes, prefer using the `@property` tag for public variables instead of 
documenting them in the class' body

### 4. Preferred Use of Reference Links over Monospace

Prefer using references (like [IntToInt]) instead of monospace (like `IntToInt`) where adequate

### 5. Usage examples

When providing examples usages on the docstring comments, you should follow the following syntax

```kotlin
/**
 * Documentation.
 *
 * ## Usage:
 * Usage details and scenarios.
 * 
 * ### Example 1: Details
 * ```
 * // example 1 code
 * ```
 * ### Example 2: Details
 * ```
 * // example 2 code
 * ```
 * @tags
 */
fun foo(params) = elements.forEach(action)
```

Note that the examples must be placed before the @tags.
Examples of @tags include @param, @return, @throws, etc.
