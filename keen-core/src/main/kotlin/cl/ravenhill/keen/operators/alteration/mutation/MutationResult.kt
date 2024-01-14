/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.keen.genetic.GeneticMaterial
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene


/**
 * Represents the result of a mutation operation in a genetic algorithm.
 *
 * ## Overview
 * `MutationResult` is a sealed interface that encapsulates the outcome of a mutation process on genetic material. It
 * is designed to be a generic, extendable representation of mutation results, providing essential information such
 * as the subject of the mutation and the number of mutations applied.
 *
 * @param T The type of value that the genes represent.
 * @param G The gene type, must extend [Gene]<[T], [G]>.
 * @param S The type of the genetic material, such as a chromosome or a gene, extending [GeneticMaterial]<[T], [G]>.
 * @property subject The genetic material (such as a chromosome or a gene) that was subjected to mutation.
 * @property mutations The total number of mutations applied to the subject.
 */
sealed interface MutationResult<T, G, out S> where G : Gene<T, G>, S : GeneticMaterial<T, G> {
    val subject: S
    val mutations: Int
}

/**
 * Represents the result of a mutation operation on a chromosome in a genetic algorithm.
 *
 * ## Overview
 * `ChromosomeMutationResult` is a data class that implements the `MutationResult` interface, specifically for the case
 * where the subject of mutation is a chromosome. It holds details about the mutated chromosome and the number of
 * mutations that were applied to it.
 *
 * @param T Represents the type of value held by the genes in the chromosome.
 * @param G The gene type, extending [Gene]<[T], [G]>.
 * @param subject The chromosome that underwent mutation. This is the mutated version of the chromosome.
 * @param mutations An integer representing the total number of mutations applied to the chromosome.
 */
data class ChromosomeMutationResult<T, G>(
    override val subject: Chromosome<T, G>,
    override val mutations: Int,
) : MutationResult<T, G, Chromosome<T, G>> where G : Gene<T, G>
