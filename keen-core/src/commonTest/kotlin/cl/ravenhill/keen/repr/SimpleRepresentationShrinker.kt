/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.repr

import cl.ravenhill.SimpleRepresentation

/**
 * A [RepresentationShrinker] implementation that provides shrinking for [SimpleRepresentation] instances.
 *
 * @param T The type of value stored by the features.
 * @param F The type of feature used in the representation, which must implement [Feature].
 */
class SimpleRepresentationShrinker<T, F> :
        RepresentationShrinker<T, F, SimpleRepresentation<T, F>> where F : Feature<T, F> {

    /**
     * Shrinks the given [SimpleRepresentation] by either dropping the first element or taking all but the last element.
     *
     * @param value The [SimpleRepresentation] to shrink.
     * @return A list of smaller [SimpleRepresentation] instances.
     */
    override fun shrink(value: SimpleRepresentation<T, F>) =
        listOf(value.drop(1), value.take(value.size - 1))
}
