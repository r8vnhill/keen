# Documentation Guidelines

### 1. Markdown Formatting for Code Snippets

When writing code snippets in documentation, **ALWAYS** use proper Markdown syntax. Enclose all code snippets with:

```
// ...
```

This ensures that the code is readable and properly formatted.

### 2. Use of @property Tag for Public Variables

When documenting classes, **PREFER** using the `@property` tag to describe public variables, rather than documenting
them within the class body.

### 3. Preferred Use of Reference Links

**PREFER** using reference links (e.g., [String]) over monospace formatting (e.g., `String`) when referencing types or
other significant elements in the documentation.

### 4. Usage Examples

When providing usage examples in docstring comments, follow this syntax:

````kotlin
/**
 * Short description of the function.
 * 
 * Detailed description of the function.
 *
 * ## Usage:
 * Usage details and scenarios.
 *
 * ### Example 1: Description
 * ```kotlin
 * // example 1 code
 * ```
 * ### Example 2: Description
 * ```kotlin
 * // example 2 code
 * ```
 * @tags
 */
fun foo(params) = elements.forEach(action)
````

### Important Note:
**Place examples before the `@tags`.** Examples of `@tags` include `@param`, `@return`, `@throws`, etc.