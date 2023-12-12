/*
 *  Copyright (c) 2023, Ignacio Slater M.
 *  2-Clause BSD License.
 */


package cl.ravenhill.keen.arbs.evolution

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.arbs.datatypes.real
import cl.ravenhill.keen.evolution.executors.SequentialEvaluator
import cl.ravenhill.keen.genetic.genes.Gene
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.next

fun <T, G> Arb.Companion.evaluator(arb: Arb<Double> = real()) where G : Gene<T, G> = arbitrary {
    SequentialEvaluator<T, G> { arb.next() }
}
