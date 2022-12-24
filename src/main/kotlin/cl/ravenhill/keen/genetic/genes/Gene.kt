package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.genetic.GeneticMaterial

/**
 * Atomic unit of a chromosome.
 *
 * @param DNA   The type of the gene's value.
 * @property dna        The gene's value.
 */
interface Gene<DNA> : GeneticMaterial<DNA> {

    val dna: DNA

    /**
     * Creates a new gene with a mutated value.
     */
    fun mutate(): Gene<DNA> =
        duplicate(generator())

    fun generator(): DNA

    /**
     * Creates a new gene with the given value.
     */
    fun duplicate(dna: DNA): Gene<DNA>

    override fun flatten() = listOf(dna)
}