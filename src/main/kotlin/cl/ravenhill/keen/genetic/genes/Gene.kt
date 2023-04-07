package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.genetic.GeneticMaterial

/**
 * Represents a gene, which is a unit of heredity that encodes a single trait of an organism.
 *
 * @property dna The gene's value.
 * @constructor Creates a new [Gene] instance with the given [dna].
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
interface Gene<DNA> : GeneticMaterial<DNA> {

    val dna: DNA

    /**
     * Creates a new gene with a mutated value.
     */
    fun mutate(): Gene<DNA> = withDna(generator())

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
    fun withDna(dna: DNA): Gene<DNA>

    // Documentation inherited from GeneticMaterial
    override fun flatten() = listOf(dna)
}