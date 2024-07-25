/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.evolution.executors.ConstructorExecutor
import cl.ravenhill.keen.evolution.executors.SequentialConstructor
import cl.ravenhill.keen.features.Representation
import cl.ravenhill.keen.genetic.GeneticMaterial
import cl.ravenhill.keen.genetic.genes.Gene
import kotlin.properties.Delegates


/**
 * Represents a chromosome in an evolutionary algorithm.
 *
 * The `Chromosome` interface extends the [GeneticMaterial], [Representation], and [Collection] interfaces, providing
 * a contract for types that encapsulate a collection of genes and support various operations on them. It includes
 * methods for duplication, verification, and transformation of genes.
 *
 * ## Usage:
 * This interface is used to manage the collection of genes that make up a chromosome in evolutionary algorithms.
 * It provides methods for accessing, verifying, duplicating, and manipulating the genes.
 *
 * ### Example:
 * ```
 * class MyChromosome<T, G : Gene<T, G>>(override val genes: List<G>) : Chromosome<T, G> {
 *     override fun duplicateWithGenes(genes: List<G>) = MyChromosome(genes)
 * }
 * ```
 *
 * @param T The type of the value held by the genes.
 * @param G The type of the gene, which must extend [Gene].
 * @property genes The list of genes in the chromosome.
 * @property size The number of genes in the chromosome.
 */
interface Chromosome<T, G> : GeneticMaterial<T, G>, Representation<T, G>, Collection<G> where G : Gene<T, G> {

    val genes: List<G>

    override val size: Int
        get() = genes.size

    /**
     * Creates a duplicate of the chromosome with the specified list of genes.
     *
     * @param genes The list of genes for the new chromosome.
     * @return A new instance of `Chromosome` with the specified genes.
     */
    fun duplicateWithGenes(genes: List<G>): Chromosome<T, G>

    /**
     * Verifies the integrity of the chromosome.
     *
     * @return `true` if all genes are valid, `false` otherwise.
     */
    override fun verify() = genes.all { it.verify() }

    /**
     * Checks if the chromosome is empty.
     *
     * @return `true` if there are no genes, `false` otherwise.
     */
    override fun isEmpty() = genes.isEmpty()

    /**
     * Returns an iterator over the genes in the chromosome.
     *
     * @return An iterator over the genes.
     */
    override fun iterator() = genes.iterator()

    /**
     * Checks if the chromosome contains the specified gene.
     *
     * @param element The gene to check.
     * @return `true` if the gene is contained in the chromosome, `false` otherwise.
     */
    override fun contains(element: G) = genes.contains(element)

    /**
     * Checks if the chromosome contains all the specified genes.
     *
     * @param elements The collection of genes to check.
     * @return `true` if all the specified genes are contained in the chromosome, `false` otherwise.
     */
    override fun containsAll(elements: Collection<G>) = genes.containsAll(elements)

    /**
     * Accesses the gene at the specified index.
     *
     * @param index The index of the gene to access.
     * @return The gene at the specified index.
     */
    operator fun get(index: Int): G = genes[index]

    /**
     * Flattens the chromosome into a list of values from all genes.
     *
     * @return A flat list of values.
     */
    override fun flatten(): List<T> = genes.flatMap { it.flatten() }

    /**
     * Factory interface for creating `Chromosome` instances.
     *
     * The `Factory` interface allows for the configuration and creation of chromosomes with specified genes.
     *
     * It is __recommended__ to use the `AbstractFactory` class to create custom factories, as it provides default
     * implementations for the interface's properties.
     *
     * @param T The type of the value held by the genes.
     * @param G The type of the gene, which must extend [Gene].
     * @property executor The executor responsible for constructing genes.
     * @property size The size of the chromosome to be created.
     */
    interface Factory<T, G> where G : Gene<T, G> {
        var executor: ConstructorExecutor<G>
        var size: Int

        /**
         * Creates a `Chromosome` instance using the configured gene constructor.
         *
         * @return A new `Chromosome` instance.
         */
        fun make(): Chromosome<T, G>
    }

    /**
     * Abstract factory class for creating `Chromosome` instances.
     *
     * The `AbstractFactory` class provides default implementations for the `Factory` interface, allowing for
     * customization and extension. This class simplifies the creation of custom factories by providing default
     * behaviors for the essential properties and methods required to construct `Chromosome` instances.
     *
     * ## Usage:
     * Extend this class to create custom factories for specific types of chromosomes. Override the `make` method to
     * define the logic for creating a `Chromosome` instance.
     *
     * ### Example:
     * ```
     * class MyChromosomeFactory<T, G : Gene<T, G>> : Chromosome.AbstractFactory<T, G>() {
     *     override fun make(): Chromosome<T, G> {
     *         // Custom logic to create a chromosome
     *     }
     * }
     * ```
     *
     * @param T The type of the value held by the genes.
     * @param G The type of the gene, which must extend [Gene].
     */
    abstract class AbstractFactory<T, G> : Factory<T, G> where G : Gene<T, G> {
        override var size: Int by Delegates.notNull()
        override var executor: ConstructorExecutor<G> = SequentialConstructor()
    }
}
