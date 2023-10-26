/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.mutator

import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.enforcer.requirements.DoubleRequirement.BeInRange
import cl.ravenhill.keen.Core.Dice
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.probability
import cl.ravenhill.keen.util.eq

/**
 * The mutator operator is responsible for mutating the [Genotype] of the [Individual]s in the
 * [Population].
 * There are two distinct roles for the mutator:
 *
 * - Exploring the search space. This exploration is often slow compared to the crossover,
 * but in problems where crossover is disruptive this can be an important way to explore the
 * landscape.
 *
 * - Maintaining diversity. Mutation prevents the population from correlating.
 * Even if most of the search is done by crossover, mutation is still important to provide the
 * diversity the crossover needs to work.
 *
 * The mutation probability is the value that must be optimized.
 * The optimal value depends on the role mutation plays.
 * If the mutation is the main exploration mechanism, then the mutation probability should be high.
 *
 * @param DNA The type of the DNA
 * @constructor Creates a new [RandomMutator] with the given [probability]
 */
class RandomMutator<DNA, G : Gene<DNA, G>>(
    probability: Double,
    chromosomeRate: Double = 0.5,
    val geneRate: Double = 0.5,
) : AbstractMutator<DNA, G>(probability, chromosomeRate) {

    init {
        enforce {
            "The gene rate [$geneRate] must be in 0.0..1.0" {
                geneRate must BeInRange(0.0..1.0)
            }
        }
    }

    /**
     * Mutates a chromosome and returns a [MutatorResult] with the mutated chromosome and the
     * number of mutations.
     */
    override fun mutateChromosome(
        chromosome: Chromosome<DNA, G>,
    ): MutatorResult<DNA, G, Chromosome<DNA, G>> {
        val result = chromosome.genes.map { mutateGene(it) }
        return MutatorResult(
            chromosome.withGenes(result.map { it.mutated }),
            result.sumOf { it.mutations }
        )
    }

    /**
     * Mutates a gene and returns a [MutatorResult] with the mutated gene and the number
     * of mutations.
     *
     * The result is defined based in the mutation [probability], the behaviour of this
     * probability can be defined by three cases:
     *
     *  1. The probability is 0.0, then 0 mutations are performed and a copy of the
     *     original gene is returned.
     *  2. The probability is 1.0, then 1 mutation is performed and the mutated gene is
     *     returned.
     *  3. The probability is between 0.0 and 1.0, then a random number is generated and
     *     if it is less than the probability, then 1 mutation is performed and the mutated
     *     gene is returned, otherwise 0 mutations are performed and a copy of the original
     *     gene is returned.
     */
    private fun mutateGene(gene: G) = when {
        geneRate eq 0.0 -> MutatorResult(gene)
        geneRate eq 1.0 || Dice.probability() < geneRate ->
            MutatorResult(gene.mutate(), 1)

        else -> MutatorResult(gene)
    }
}
