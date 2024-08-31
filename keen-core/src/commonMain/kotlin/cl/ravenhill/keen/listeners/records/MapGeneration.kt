/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners.records

import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.utils.box.Box

/**
 * Applies a transformation to a [GenerationRecord] contained within a [Box].
 *
 * The `mapGeneration` function allows you to apply a transformation or operation to a `GenerationRecord` within a
 * nullable `Box`. If the `Box` contains a `GenerationRecord`, the provided [block] of operations will be applied to it.
 * If the `Box` is null, the function does nothing.
 *
 * ## Usage:
 * This function is useful when you need to perform an operation on a `GenerationRecord` that is wrapped in a `Box` and
 * may be null. The function safely handles the nullability and only applies the `block` if the `GenerationRecord` is
 * present. This helps to prevent potential null pointer exceptions and simplifies the code that operates on the
 * `GenerationRecord`.
 *
 * @param generation The `Box` containing a `GenerationRecord` or null.
 * @param block The block of code to be applied to the `GenerationRecord` if it exists. This block operates on the
 *   `GenerationRecord` and can modify its properties.
 *
 * @param T The type of value held by the features.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 */
fun <T, F, R> applyToGeneration(
    generation: Box<GenerationRecord<T, F, R>?>,
    block: GenerationRecord<T, F, R>.() -> Unit
) where F : Feature<T, F>,
        R : Representation<T, F> {
    generation.map { it?.apply(block) }
}
