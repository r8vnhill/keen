package cl.ravenhill.keen.arb.operators

import cl.ravenhill.keen.arb.datatypes.arbProbability
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.alteration.crossover.CombineCrossover
import cl.ravenhill.keen.operators.alteration.crossover.SinglePointCrossover
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.int

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
    SinglePointCrossover(
        chromosomeRate = chromosomeRate?.bind() ?: 1.0,
        exclusivity = exclusivity?.bind() ?: false
    )
}

/**
 * Generates an [Arb] of [CombineCrossover] instances for testing purposes, with configurable parameters
 * for chromosome rate, gene rate, number of parents, and exclusivity. This allows for extensive and flexible
 * testing of the `CombineCrossover` class under various conditions.
 *
 * @param chromosomeRate
 *  An optional `Arb<Double>` that specifies the probabilities for chromosome crossover rate. If `null`, defaults to 1.0,
 *  representing a 100% crossover rate.
 * @param geneRate
 *  An optional `Arb<Double>` that specifies the probabilities for gene crossover rate. If `null`, defaults to 1.0,
 *  representing a 100% gene crossover rate.
 * @param numParents An optional
 *  `Arb<Int>` to specify the number of parents involved in the crossover. If `null`, defaults to 2.
 * @param exclusivity
 *  An optional `Arb<Boolean>` to specify if the crossover should be exclusive. If `null`, defaults to false.
 * @return
 *  An `Arb<CombineCrossover<T, G>>` which generates instances of [CombineCrossover] based on the provided or default
 *  configurations.
 * @param T The type parameter of the gene values.
 * @param G The type of `Gene` being used, which must conform to `Gene<T, G>`.
 */
fun <T, G> arbCombineCrossover(
    chromosomeRate: Arb<Double>? = arbProbability(),
    geneRate: Arb<Double>? = arbProbability(),
    numParents: Arb<Int>? = Arb.int(2..5),
    exclusivity: Arb<Boolean>? = Arb.boolean()
): Arb<CombineCrossover<T, G>> where G : Gene<T, G> = arbitrary {
    CombineCrossover(
        { genes -> genes.last() },
        chromosomeRate = chromosomeRate?.bind() ?: 1.0,
        geneRate = geneRate?.bind() ?: 1.0,
        numParents = numParents?.bind() ?: 2,
        exclusivity = exclusivity?.bind() ?: false
    )
}
