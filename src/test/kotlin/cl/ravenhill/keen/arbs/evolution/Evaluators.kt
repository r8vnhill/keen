package cl.ravenhill.keen.arbs.evolution

import cl.ravenhill.keen.evolution.executors.SequentialEvaluator
import cl.ravenhill.keen.genetic.genes.Gene
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary

fun <T, G> Arb.Companion.evaluator() where G : Gene<T, G> = arbitrary {
    SequentialEvaluator<T, G> { 0.0 }
}