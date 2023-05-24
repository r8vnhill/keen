# ChatGPT Rules

This is the `<n>`th chat about Keen, a Genetic Algorithm library for Kotlin.

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

### 3. Preservation of Original Intent in Code Modification

When modifying code, you should ALWAYS preserve any comments that start with `///` to maintain 
the original intent of the code, while adding your own comments to explain any changes.

### 4. Region Annotation Formatting for Code Grouping

When grouping code into regions, use the syntax `// region : -== REGION NAME ==- :` to mark the
beginning of a region and `// endregion REGION NAME` to mark the end of a region. 
Note that the region name should be in **ALL CAPS** and that there are spaces around the ``:``.

### 5. Preferred Use of @property Tag for Documenting Public Variables

When documenting classes, prefer using the `@property` tag for public variables instead of 
documenting them in the class' body

### 6. Preferred Use of Reference Links over Monospace

Prefer using references (like [IntToInt]) instead of monospace (like `IntToInt`) where adequate

### 7. File Summary Formatting and Content Ignorance

Whenever you are asked to summarize a file, you will create a summary, ignoring data as 
packages, authors or versions.
Your answer should be formatted as follows:

```kotlin
/***************************************************************************************************
 * Short description on the file's objective.
 * 
 * One-paragraph summary of the file.
 **************************************************************************************************/
```

### 8. Use of Line Breaks for Readability

Always use line breaks after the end of a sentence, and use two line breaks after the end of a
paragraph to maintain readability.

### 9. Usage examples

When providing examples usages on the docstring comments, you should follow the following syntax

```kotlin
/**
 * Documentation.
 *
 * ## Examples
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
fun foo(params) {
    elements.forEach(action)
```

Note that the examples must be placed before the @tags.
