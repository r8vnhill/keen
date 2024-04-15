/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.exceptions

/**
 * Represents a throwable object used to indicate an attempt to perform an operation that is logically absurd or
 * impossible.
 *
 * `AbsurdOperation` is an object derived from `Throwable`, signifying an attempt to execute an operation that is
 * fundamentally illogical or nonsensical in the given context. This can be particularly useful in programming paradigms
 * or situations where the type system allows expressions that are type-correct but semantically absurd.
 *
 * ## Usage:
 * This throwable is typically used in situations where a function or operation reaches a state that should be logically
 * impossible, or when an operation is performed on a type that inherently disallows such operations. It acts as a
 * safeguard against nonsensical code execution paths.
 *
 * ### Example:
 * ```kotlin
 * fun processValue(value: Nothing): String {
 *     throw AbsurdOperation
 * }
 * ```
 * In this example, the `processValue` function accepts a value of type `Nothing`, which is a type that cannot be
 * instantiated. This function will never be called, as it is impossible to provide a value of type `Nothing`.
 * However, the compiler will not complain about this function, as it is type-correct.
 */
@Suppress("JavaIoSerializableObjectMustHaveReadResolve")
object AbsurdOperation : Throwable()
