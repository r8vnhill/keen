/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */
package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.evolution.executors.ConstructorExecutor
import cl.ravenhill.keen.evolution.executors.SequentialConstructor
import cl.ravenhill.keen.genetic.GeneticMaterial
import cl.ravenhill.keen.genetic.genes.Gene
import kotlin.properties.Delegates

/**
 * An ordered collection of genes that defines a specific genetic material.
 *
 * A `Chromosome` is a sequence of [Gene]s, each of which contains a specific value of type [DNA].
 * The `Chromosome` interface extends the [GeneticMaterial] interface and therefore provides a
 * method to [flatten] the chromosome into a list of [DNA] objects.
 * It also implements the [Iterable] interface, allowing for easy iteration over the genes in the
 * chromosome.
 *
 * @param DNA The type of the genes' values.
 * @param G The type of the genes.
 * This parameter is needed to ensure type safety in the chromosome's operations and to provide a
 * way to access the genes' specific data type [DNA].
 * By specifying the gene type as a generic parameter, the Chromosome interface provides a flexible
 * way of representing genetic data, allowing different types of genes to be used in different
 * contexts
 *
 * @property genes The genes of the chromosome, ordered from start to end.
 * @property size The number of genes in the chromosome.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
interface Chromosome<DNA, G : Gene<DNA, G>> : GeneticMaterial<DNA, G>, Iterable<G> {

    val genes: List<G>

    val size: Int
        get() = genes.size

    /* Documentation inherited from [Verifiable]. */
    override fun verify() = genes.isNotEmpty() && genes.all { it.verify() }

    /* Documentation inherited from [Iterable]. */
    override fun iterator() = genes.iterator()

    /**
     * Returns the gene at the given ``index``.
     */
    operator fun get(index: Int) = genes[index]

    /**
     * Returns a new chromosome with the given ``genes``.
     */
    fun withGenes(genes: List<G>): Chromosome<DNA, G>

    /* Documentation inherited from [GeneticMaterial]. */
    override fun flatten(): List<DNA> = genes.fold(mutableListOf()) { acc, gene ->
        acc.apply { addAll(gene.flatten()) }
    }

    /**
     * Factory interface for creating [Chromosome] objects.
     *
     * @param DNA The type of the genes' values.
     * @param G The type of [Gene] contained in the chromosome.
     * @property executor The executor to use for creating [Gene] objects.
     * @property size The number of genes in the chromosome.
     */
    interface Factory<DNA, G : Gene<DNA, G>> {
        var executor: ConstructorExecutor<G>

        var size: Int

        /**
         * Creates a new [Chromosome] object.
         */
        fun make(): Chromosome<DNA, G>
    }

    /**
     * An abstract implementation of the [Factory] interface for creating [Chromosome] objects.
     *
     * @param DNA The type of the genes' values.
     * @param G The type of [Gene] contained in the chromosome.
     * @property executor The executor to use for creating [Gene] objects.
     * The default implementation uses a [SequentialConstructor] object.
     */
    abstract class AbstractFactory<DNA, G : Gene<DNA, G>> : Factory<DNA, G> {
        /* Documentation inherited from [Factory]. */
        override var size: Int by Delegates.notNull()

        /* Documentation inherited from [Factory]. */
        override var executor: ConstructorExecutor<G> = SequentialConstructor()
    }
}
