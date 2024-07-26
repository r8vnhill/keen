/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.repr

import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.mixins.FlatMappable
import cl.ravenhill.keen.mixins.Verifiable

interface Representation<T, F> : Verifiable, FlatMappable<T> where F : Feature<T, F> {

    val size: Int
}
