package cl.ravenhill.keen.arb.operators

import cl.ravenhill.keen.arb.datatypes.arbProbability
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.alteration.crossover.SinglePointCrossover
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean

/**
 * Generates an [Arb] of [SinglePointCrossover] instances for testing purposes. This function provides flexible
 * configurations for `chromosomeRate` and `exclusivity`, making it suitable for a wide range of evolutionary algorithm
 * testing scenarios.
 *
 * @param chromosomeRate
 *  An optional `Arb<Double>` that specifies the probabilities for chromosome crossover rate. If `null`, a default
 *  probability generator is used.
 * @param exclusivity
 *  An optional `Arb<Boolean>` to specify if the crossover should be exclusive. If `null`, a boolean generator is used.
 * @return An `Arb<SinglePointCrossover<T, G>>` which generates instances of [SinglePointCrossover] based on the
 *  provided or default configurations.
 * @param T The type parameter of the gene values.
 * @param G The type of [Gene] being used, which must conform to [Gene<T, G>].
 */
fun <T, G> arbSinglePointCrossover(
    chromosomeRate: Arb<Double>? = arbProbability(),
    exclusivity: Arb<Boolean>? = Arb.boolean()
): Arb<SinglePointCrossover<T, G>> where G : Gene<T, G> = arbitrary {
    when {
        chromosomeRate != null && exclusivity != null -> SinglePointCrossover(chromosomeRate.bind(), exclusivity.bind())
        chromosomeRate != null -> SinglePointCrossover(chromosomeRate.bind())
        exclusivity != null -> SinglePointCrossover(exclusivity = exclusivity.bind())
        else -> SinglePointCrossover()
    }
}
