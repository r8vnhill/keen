package cl.ravenhill.keen.operators.alteration

import cl.ravenhill.keen.operators.Operator
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation

/**
 * Marker interface for operators that alter the genetic representation in an evolutionary algorithm.
 *
 * The `Alterer` interface serves as a marker for operators that are specifically designed to modify or alter the
 * genetic representation of individuals in an evolutionary algorithm. This interface extends the [Operator] interface,
 * inheriting its core functionality while providing a semantic distinction that indicates the operator's primary role
 * is to alter the genetic material.
 *
 * @param T The type of value held by the features in the representation.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 */
interface Alterer<T, F, R> : Operator<T, F, R> where F : Feature<T, F>, R : Representation<T, F>
