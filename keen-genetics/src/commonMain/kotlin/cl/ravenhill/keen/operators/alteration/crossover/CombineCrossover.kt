/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.jakt.constrained
import cl.ravenhill.jakt.constraints.doubles.BeInRange
import cl.ravenhill.jakt.constraints.ints.BeAtLeast
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.utils.Exclusivity

/**
 * A crossover operator that combines genes from multiple parents to produce offspring in an evolutionary algorithm.
 *
 * The `CombineCrossover` class represents a genetic operator that performs crossover by combining genes from a list of
 * parent individuals to create offspring. This approach allows for the generation of new genetic material by
 * recombining existing genes, which is essential for maintaining diversity and exploring new solutions in the
 * evolutionary process.
 *
 * ## Constraints:
 * The `CombineCrossover` class enforces several constraints to ensure valid crossover operations:
 * - The `geneRate` and `chromosomeRate` must be between 0 and 1.
 * - The `numParents` must be at least 2.
 *
 * ## Usage:
 * This class is designed for use in evolutionary algorithms where crossover operations are a critical component of the
 * genetic evolution process. The class can be configured with different rates and a combiner function to tailor the
 * crossover behavior to specific needs.
 *
 * ### Example: Using `CombineCrossover`
 * ```kotlin
 * val crossover = CombineCrossover<Int, MyGene>(
 *     combiner = { genes -> genes.random(Domain.random) }, // Akin to uniform crossover
 *     chromosomeRate = 0.8,
 *     geneRate = 0.5,
 *     numParents = 3,
 *     exclusivity = Exclusivity.NON_EXCLUSIVE
 * )
 * ```
 *
 * @param T The type of value held by the genes.
 * @param G The type of gene, which must extend [Gene].
 * @param combiner The function that combines a list of genes from the parent individuals into a single gene for the
 *   offspring.
 * @param chromosomeRate The probability that a chromosome will be subject to crossover.
 * @param geneRate The probability that a specific gene within a chromosome will be subject to crossover.
 * @param numParents The number of parent individuals involved in the crossover.
 * @param exclusivity The exclusivity policy for selecting parents during the crossover.
 * @property numOffspring The number of offspring produced by the crossover operation.
 * @throws CompositeException if the gene rate is not between 0 and 1, the chromosome rate is not between 0 and 1, or
 *   the number of parents is less than 2.
 */
open class CombineCrossover<T, G>(
    val combiner: (List<G>) -> G,
    override val chromosomeRate: Double = TODO(),
    val geneRate: Double = TODO(),
    override val numParents: Int = TODO(),
    override val exclusivity: Exclusivity = TODO()
) : Crossover<T, G> where G : Gene<T, G> {

    /**
     * The number of offspring produced by the crossover operation.
     */
    override val numOffspring: Int = 1

    init {
        constrained {
            "The gene rate must be between 0 and 1" {
                geneRate must BeInRange(0.0..1.0)
            }
            "The chromosome rate must be between 0 and 1" {
                chromosomeRate must BeInRange(0.0..1.0)
            }
            "Number of parents must be greater or equal to 2" {
                numParents must BeAtLeast(2)
            }
        }.onLeft { throw it }
    }
}
