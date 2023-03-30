package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.genetic.GeneticMaterial

/**
 * Atomic unit of a chromosome.
 *
 * @param DNA The type of the gene's value.
 * @property dna The gene's value.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
interface Gene<DNA> : GeneticMaterial<DNA> {

    /**
     * The gene's value.
     */
    val dna: DNA

    /**
     * Creates a new gene with a mutated value.
     */
    fun mutate(): Gene<DNA> = duplicate(generator())

    /**
     * Generates a new random value for this gene.
     */
    fun generator(): DNA

    /**
     * Creates a new gene with the given value.
     */
    fun duplicate(dna: DNA): Gene<DNA>

    // Documentation inherited from GeneticMaterial
    override fun flatten() = listOf(dna)
}