/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */

package cl.ravenhill.keen.operators.mutator

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.probability
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
 * @property geneProbability The probability for each individual gene to be mutated.
 *                           By default, this is set to 0.5 meaning each gene has a
 *                           50% chance to mutate.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class BitFlipMutator<G : Gene<Boolean, G>>(probability: Double, geneProbability: Double = 0.5) :
    Mutator<Boolean, G>(probability) {

    /**
     * Mutates a given gene based on the defined `probability` and `geneProbability`.
     *
     * @param gene The gene to be mutated.
     * @return Returns a `MutatorResult` that contains the mutated gene and the
     *         number of mutations applied.
     */
    override fun mutateGene(gene: G) = when {
        probability eq 0.0 -> MutatorResult(gene)
        geneProbability eq 1.0 || Core.Dice.probability() < geneProbability ->
            MutatorResult(gene.withDna(!gene.dna), 1)

        else -> MutatorResult(gene)
    }
}

