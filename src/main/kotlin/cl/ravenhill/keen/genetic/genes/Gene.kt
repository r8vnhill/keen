/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.genetic.GeneticMaterial
import cl.ravenhill.keen.util.MultiStringFormat
import cl.ravenhill.keen.util.SelfReferential

/**
 * Represents a gene, which is a unit of heredity that encodes a single trait of an organism in a
 * genetic algorithm.
 *
 * A genetic algorithm is a search heuristic that is inspired by the process of natural selection.
 * The algorithm maintains a population of candidate solutions and evolves them over time through
 * processes like mutation and crossover, which involve manipulating the genes of individuals in the
 * population to create new candidate solutions.
 *
 * The [Gene] interface represents an individual gene within an organism, and provides methods for
 * generating new* genes with mutated or recombined values.
 *
 * @param DNA The type of the gene's value.
 * @param G The type of the gene.
 * This is used to enable fluent APIs that allow for chaining of genetic operations in a type-safe
 * way.
 *
 * @property dna The gene's value.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
interface Gene<DNA, G> :
    GeneticMaterial<DNA, G>,
    SelfReferential<G>,
    MultiStringFormat where G : Gene<DNA, G> {

    val dna: DNA

    /**
     * Creates a new gene with a mutated value.
     */
    fun mutate(): G = withDna(generator())

    /**
     * Generates a new random value for this gene.
     */
    fun generator(): DNA = dna

    /**
     * Creates a new gene with the given value.
     *
     * @param dna The value for the new gene.
     * @return A new [Gene] instance with the given [dna] value.
     */
    fun withDna(dna: DNA): G

    // / Documentation inherited from [GeneticMaterial]
    override fun flatMap(transform: (DNA) -> DNA) = listOf(transform(dna))

    override fun toSimpleString() = dna.toString()

    override fun toDetailedString() = "${this::class.simpleName}(dna=$dna)"
}
