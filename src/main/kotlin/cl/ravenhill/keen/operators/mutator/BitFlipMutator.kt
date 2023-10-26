/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.mutator

import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.enforcer.requirements.DoubleRequirement.BeInRange
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.eq

/**
 * A mutator class responsible for performing bit-flip mutation on genes.
 * In this mutation type, each gene in the chromosome has a probability
 * of being flipped (from true to false or vice-versa).
 *
 * ## Examples
 * ### Example 1: Creating an engine with a BitFlipMutator
 *
 * ```kotlin
 * val engine = engine(::fitnessFunction, genotype {
 *    chromosome {
 *      booleans {
 *        size = 10
 *        truesProbability = 0.5
 *      }
 *    }
 *  }) {
 *    alterers = listOf(BitFlipMutator(0.1, 0.5))
 *  }
 *  ```
 *
 * @property probability The global probability of mutation for the entire chromosome.
 * @property geneRate The probability for each individual gene to be mutated.
 *                           By default, this is set to 0.5 meaning each gene has a
 *                           50% chance to mutate.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class BitFlipMutator<G : Gene<Boolean, G>>(
    probability: Double,
    chromosomeRate: Double = 0.5,
    override val geneRate: Double = 0.5
) : AbstractMutator<Boolean, G>(probability, chromosomeRate), GeneMutator<Boolean, G> {

    init {
        enforce {
            "The gene rate [$geneRate] must be in 0.0..1.0" {
                geneRate must BeInRange(0.0..1.0)
            }
        }
    }

    override fun mutateChromosome(
        chromosome: Chromosome<Boolean, G>,
    ) = when {
        Core.random.nextDouble() < chromosomeRate -> {
            val result = chromosome.genes.map { mutateGene(it) }
            MutatorResult(
                chromosome.withGenes(result.map { it.mutated }),
                result.sumOf { it.mutations }
            )
        }

        else -> MutatorResult(chromosome)
    }

    /**
     * Mutates a given gene based on the defined `probability` and `geneProbability`.
     *
     * @param gene The gene to be mutated.
     * @return Returns a `MutatorResult` that contains the mutated gene and the
     *         number of mutations applied.
     */
    override fun mutateGene(gene: G) = when {
        geneRate eq 0.0 -> MutatorResult(gene)
        geneRate eq 1.0 || Core.random.nextDouble() < geneRate ->
            MutatorResult(gene.withDna(!gene.dna), 1)

        else -> MutatorResult(gene)
    }
}
