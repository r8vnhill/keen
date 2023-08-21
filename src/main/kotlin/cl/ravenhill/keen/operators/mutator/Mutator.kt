/*
 * Copyright (c) 2023, Ignacio Slater M.
 * BSD Zero Clause License.
 */

package cl.ravenhill.keen.operators.mutator

import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.GeneticMaterial
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.Alterer
import cl.ravenhill.keen.operators.AltererResult
import cl.ravenhill.keen.util.eq

/**
 * Represents an interface for mutation operations on genes within a population.
 *
 * The mutator works at multiple levels of the genome hierarchy: phenotype, genotype, and
 * chromosome.
 * Mutation operations modify individual genes based on mutation-specific logic.
 * This is essential for introducing new genetic variations within the population.
 *
 * @param DNA The type of genetic data the gene represents.
 * @param G The type of gene being mutated, parametrized by its DNA and its own type.
 */
interface Mutator<DNA, G : Gene<DNA, G>> : Alterer<DNA, G> {

    /**
     * Performs mutation operations on the entire population.
     *
     * @param population The current population of phenotypes.
     * @param generation The current generation number.
     * @return The result after applying mutation operations on the population.
     */
    override fun invoke(
        population: Population<DNA, G>,
        generation: Int,
    ): AltererResult<DNA, G> {
        if (probability eq 0.0) return AltererResult(population)
        val result = population.map {
            mutatePhenotype(it)
        }
        return AltererResult(
            result.map { it.mutated },
            result.sumOf { it.mutations }
        )
    }

    /**
     * Mutates a given phenotype.
     *
     * @param phenotype The phenotype to be mutated.
     * @return The mutated phenotype.
     */
    fun mutatePhenotype(
        phenotype: Phenotype<DNA, G>,
    ): MutatorResult<DNA, G, Phenotype<DNA, G>> = mutateGenotype(phenotype.genotype).map {
        Phenotype(it)
    }

    /**
     * Mutates a given genotype.
     *
     * @param genotype The genotype to be mutated.
     * @return The result containing the mutated genotype and mutation count.
     */
    fun mutateGenotype(
        genotype: Genotype<DNA, G>,
    ): MutatorResult<DNA, G, Genotype<DNA, G>> {
        val result = genotype.chromosomes.map { mutateChromosome(it) }
        return MutatorResult(
            Genotype(result.map { it.mutated }),
            result.sumOf { it.mutations }
        )
    }

    /**
     * Mutates a given chromosome.
     *
     * @param chromosome The chromosome to be mutated.
     * @return The result containing the mutated chromosome and mutation count.
     */
    fun mutateChromosome(
        chromosome: Chromosome<DNA, G>,
    ): MutatorResult<DNA, G, Chromosome<DNA, G>>
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
data class MutatorResult<DNA, G, T>(val mutated: T, val mutations: Int = 0)
    where T : GeneticMaterial<DNA, G>, G : Gene<DNA, G> {

    /**
     * Applies the given [transform] function to the [mutated] object and returns the
     * result.
     */
    fun <B : GeneticMaterial<DNA, G>> map(transform: (T) -> B) =
        MutatorResult(transform(mutated), mutations)
}
