/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.features.Feature
import cl.ravenhill.keen.genetic.GeneticMaterial
import cl.ravenhill.keen.mixins.MultiStringFormat
import cl.ravenhill.keen.mixins.SelfReferential


/**
 * An interface representing a gene in a genetic algorithm.
 *
 * The `Gene` interface extends several other interfaces, including `GeneticMaterial`, `Feature`, and
 * `MultiStringFormat` (deprecated).
 * It provides a common structure for genes used in genetic algorithms, with methods for generating, mutating, and
 * duplicating genes, as well as methods for flattening and string representation.
 *
 * ## Usage:
 * This interface is intended to be implemented by classes that represent specific types of genes in a genetic
 * algorithm. Implementing classes should provide concrete implementations of the abstract methods.
 *
 * ### Example 1: Implementing a Simple Gene
 * ```kotlin
 * data class SimpleGene(override val value: Int) : Gene<Int, SimpleGene> {
 *     override fun duplicateWithValue(value: Int) = SimpleGene(value)
 * }
 *
 * val gene = SimpleGene(42)
 * println(gene) // Outputs: SimpleGene(value=42)
 * println(gene.mutate()) // Outputs: SimpleGene(value=42) by default, as generator() returns the same value
 * ```
 *
 * ### Example 2: Implementing a Custom Gene with Mutation
 * ```kotlin
 * data class CustomGene(override val value: Int) : Gene<Int, CustomGene> {
 *     override fun generator(): Int = (0..100).random()
 *     override fun duplicateWithValue(value: Int) = CustomGene(value)
 * }
 *
 * val gene = CustomGene(42)
 * println(gene) // Outputs: CustomGene(value=42)
 * println(gene.mutate()) // Outputs: CustomGene(value=<random value between 0 and 100>)
 * ```
 *
 * @param T The type of the value held by the gene.
 * @param G The type of the gene itself, which must extend `Gene`.
 */
interface Gene<T, G> : GeneticMaterial<T, G>, Feature<T, G>, MultiStringFormat where G : Gene<T, G> {

    /**
     * Generates a new value for the gene. By default, it returns the current value.
     *
     * @return The generated value.
     */
    fun generator(): T = value

    /**
     * Mutates the gene by generating a new value and creating a duplicate gene with that value.
     *
     * @return A new gene with the mutated value.
     */
    fun mutate(): G = duplicateWithValue(generator())

    /**
     * Creates a duplicate of the gene with the specified value.
     *
     * @param value The value for the new gene.
     * @return A new gene with the specified value.
     */
    override fun duplicateWithValue(value: T): G

    /**
     * Flattens the gene into a list containing its value.
     *
     * @return A list containing the value of the gene.
     */
    override fun flatten(): List<T> = listOf(value)

    /**
     * Deprecated. Use the standard `toString` method instead.
     *
     * @return A simple string representation of the gene's value.
     */
    @Deprecated("Use the standard toString method instead", replaceWith = ReplaceWith("toString()"))
    override fun toSimpleString() = value.toString()

    /**
     * Deprecated. Use the standard `toString` method instead.
     *
     * @return A detailed string representation of the gene, including its class name and value.
     */
    @Deprecated("Use the standard toString method instead", replaceWith = ReplaceWith("toString()"))
    override fun toDetailedString() = "${this::class.simpleName}(value=$value)"
}
