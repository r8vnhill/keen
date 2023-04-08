package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.genetic.GeneticMaterial
import cl.ravenhill.keen.genetic.genes.Gene

/**
 * Sequence of genes.
 *
 * @param DNA   The type of the genes' values.
 *
 * @property genes  The genes of the chromosome.
 * @property size   The size of the chromosome.
 *
 * @author <a href="https://github.com/r8vnhill">R8V</a>
 */
interface Chromosome<DNA> : GeneticMaterial<DNA> {

    val genes: List<Gene<DNA>>

    val size: Int
        get() = genes.size

    /// {@inheritDoc}
    override fun verify() = genes.isNotEmpty() && genes.all { it.verify() }

    /**
     * Returns the gene at the given ``index``.
     */
    operator fun get(index: Int) = genes[index]

    /**
     * Returns a new chromosome with the given ``genes``.
     */
    fun withGenes(genes: List<Gene<DNA>>): Chromosome<DNA>

    fun sequence() = genes.asSequence()

    fun toDNA() = genes.map { it.dna }

    override fun flatten(): List<DNA> = genes.fold(mutableListOf()) { acc, gene ->
        acc.apply { addAll(gene.flatten()) }
    }

    /**
     * Builder for [Chromosome]s.
     *
     * @param DNA   The type of the genes' values.
     */
    interface Factory<DNA> {

        /**
         * Builds a new chromosome.
         */
        fun make(): Chromosome<DNA>

    }
}