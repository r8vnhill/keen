package cl.ravenhill.keen.arbs.evolution

import cl.ravenhill.keen.evolution.executors.SequentialEvaluator
import cl.ravenhill.keen.genetic.genes.Gene
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double

fun <T, G> Arb.Companion.evaluator(arb: Arb<Double> = double()) where G : Gene<T, G> = arbitrary {
    val bound = arb.bind()
    SequentialEvaluator<T, G> { bound }
}
