/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.arbs

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.optimizer.FitnessMinimizer
import io.kotest.property.Arb
import io.kotest.property.arbitrary.element

fun <T, G> Arb.Companion.optimizer() where G : Gene<T, G> =
    element(FitnessMinimizer<T, G>(), FitnessMinimizer())
