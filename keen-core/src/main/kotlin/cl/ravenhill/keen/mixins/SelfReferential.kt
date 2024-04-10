/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.mixins


/**
 * An interface representing a self-referential type pattern, also known as the Curiously Recurring Template Pattern
 * (CRTP).
 *
 * This interface is used to define types where an instance of a class is expected to provide methods that return
 * instances of its own type or subtypes. It's particularly useful for creating fluent interfaces and hierarchical type
 * constraints in a type-safe manner.
 *
 * ## Features:
 * - **Type-Safe Self-Referencing**: Ensures that methods returning an instance of the implementing class or its
 *   subtypes are type-safe. This is particularly useful for method chaining and building fluent interfaces.
 * - **Hierarchical Type Constraints**: Allows for constraining methods to return instances of the class or its
 *   subclasses, making it suitable for creating extensible APIs with strict type hierarchies.
 *
 * ## Usage:
 * This interface is meant to be implemented by classes that need to refer to their own type in method signatures.
 * It's commonly used in builder patterns, fluent interfaces, and complex type hierarchies where methods return an
 * instance of the same class or its subtypes.
 *
 * ### Example:
 * Implementing `SelfReferential` in a fluent builder class:
 * ```kotlin
 * class FluentBuilder : SelfReferential<FluentBuilder> {
 *     fun withProperty(value: String): FluentBuilder {
 *         // Modify property
 *         return this
 *     }
 *
 *     fun build(): SomeObject {
 *         // Build and return the final object
 *     }
 * }
 *
 * val builder = FluentBuilder().withProperty("Value").build()
 * ```
 *
 * In this example, `FluentBuilder` implements `SelfReferential`, enabling methods like `withProperty` to return
 * an instance of `FluentBuilder`. This facilitates method chaining and improves the usability of the builder pattern.
 *
 * @param T The self-referential type. It should be the type of the implementing class itself or its subtypes.
 *
 * @see [Curiously Recurring Template Pattern (CRTP)](https://en.wikipedia.org/wiki/Curiously_recurring_template_pattern)
 *
 * @author [Ignacio Slater M.](https://www.github.com/r8vnhill)
 * @version 2.0.0
 * @since 2.0.0
 */
interface SelfReferential<T> where T : SelfReferential<T>
