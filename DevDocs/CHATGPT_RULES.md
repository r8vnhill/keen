# ChatGPT Usage Rules

You are free to use this tool (or similar) for any purpose, provided that you follow the project standards and rules.

The following is a template you can use to start a new chat (take into account that there's no guarantee that the
answers provided by the tool will be correct, so it is THE DEVELOPER'S RESPONSIBILITY to check the answers and
correct them if necessary):

---

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

Functions:

```kotlin
/**
 * Brief description of the function
 * 
 * ## Overview:
 * Overview of the function
 * 
 * ## Any other section:
 * ...
 * 
 * ## Usage:
 * Usage details and scenarios.
 * 
 * ### Example 1: Details
 * example 1 code
 * 
 * ### Example 2: Details
 * Example 2 code
 * 
 * @param T Generic type
 * @param params Parameters
 * @return The result
 */
fun <T> foo(params) = elements.forEach(action)
```

Classes:

```kotlin
/**
 * Brief description of the class
 * 
 * ## Overview:
 * Overview of the class
 * 
 * ## Any other section:
 * ...
 * 
 * ## Usage:
 * Usage details and scenarios.
 * 
 * ### Example 1: Details
 * Example 1 code
 * 
 * ### Example 2: Details
 * Example 2 code
 * 
 * @param T Generic type
 * @param params Parameters
 * @property property1 Public properties are documented with @property
 * @return The result
 */
 class Foo<T>(params) {
    val property1: Int = 0
    /**
     * Private properties are documented in the class body
     */
    private val property2: Int = 0
    // ...
 }
```
Note that the examples must be placed before the @tags.
