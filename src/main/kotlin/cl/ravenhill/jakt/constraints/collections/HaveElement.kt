/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.jakt.constraints.collections


/**
 * Represents a constraint that ensures a specific element is present within a collection.
 *
 * This class checks whether a given collection contains the specified `element`.
 * It serves as a concrete implementation of the [CollectionConstraint] interface for this specific check.
 *
 * @param element The element that the collection should contain to satisfy this requirement.
 * @property validator A lambda function that verifies the presence of the specified `element` in the collection.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class HaveElement<T>(private val element: T) : CollectionConstraint {
  override val validator = { collection: Collection<*> -> element in collection }
}
