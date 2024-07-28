/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetics.genes

import cl.ravenhill.keen.repr.Feature

interface Gene<T, G> : Feature<T, G> where G : Gene<T, G>
