/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.operators.mutator

import cl.ravenhill.keen.Core.Dice
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.GeneticMaterial
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.AbstractAlterer
import cl.ravenhill.keen.operators.AltererResult
import cl.ravenhill.keen.probability
import cl.ravenhill.keen.util.math.eq


/**
 * The mutator operator is responsible for mutating the [Genotype] of the [Phenotype]s in the
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
 * @constructor Creates a new [Mutator] with the given [probability]
 */
open class Mutator<DNA>(probability: Double) : AbstractAlterer<DNA>(probability) {

    /**
     * Mutates a population.
     *
     * @param population The population to mutate
     * @param generation The current generation
     * @return The mutated population
     */
    override fun invoke(
        population: Population<DNA>,
        generation: Int
    ): AltererResult<DNA> {
        if (probability eq 0.0) return AltererResult(population)
        val result = population.map {
            mutatePhenotype(it, generation)
        }
        return AltererResult(
            result.map { it.mutated },
            result.sumOf { it.mutations }
        )
    }

    /**
     * Mutates a [Phenotype] and returns a [MutatorResult] with the mutated [Phenotype]s
     * and the number of mutations performed.
     */
    internal fun mutatePhenotype(
        phenotype: Phenotype<DNA>,
        generation: Int
    ) = mutateGenotype(phenotype.genotype).map {
        Phenotype(it, generation)
    }

    /**
     * Mutates a genotype and returns a [MutatorResult] with the mutated genotype and the
     * number of mutations.
     */
    internal fun mutateGenotype(
        genotype: Genotype<DNA>
    ): MutatorResult<Genotype<DNA>> {
        val result = genotype.chromosomes.map { mutateChromosome(it) }
        return MutatorResult(
            genotype.duplicate(result.map { it.mutated }),
            result.sumOf { it.mutations }
        )
    }

    /**
     * Mutates a chromosome and returns a [MutatorResult] with the mutated chromosome and the
     * number of mutations.
     */
    internal open fun mutateChromosome(
        chromosome: Chromosome<DNA>
    ): MutatorResult<Chromosome<DNA>> {
        val result = chromosome.genes.map { mutateGene(it) }
        return MutatorResult(
            chromosome.duplicate(result.map { it.mutated }),
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
    internal fun mutateGene(gene: Gene<DNA>) =
        if (probability eq 0.0)
            MutatorResult(gene)
        else if (probability eq 1.0 || Dice.probability() < probability)
            MutatorResult(gene.mutate(), 1)
        else MutatorResult(gene)

    override fun toString() = "Mutator { " +
            "probability: $probability }"
}

/**
 * A [MutatorResult] is the result of a mutation operation.
 *
 * @param T The type of the mutated object
 * @property mutated The result of a mutation operation.
 * @property mutations The number of mutations performed.
 * @constructor Creates a new [MutatorResult] with the given [mutated] object and the
 * number of [mutations] performed (default 0).
 */
class MutatorResult<T: GeneticMaterial<*>>(val mutated: T, val mutations: Int = 0) {

    /**
     * Applies the given [transform] function to the [mutated] object and returns the
     * result.
     */
    fun <B: GeneticMaterial<*>> map(transform: (T) -> B) = MutatorResult(transform(mutated), mutations)

    /**
     * The [mutated] value.
     */
    operator fun component1() = mutated

    /**
     * The number of [mutations] done to the mutated value.
     */
    operator fun component2() = mutations
}