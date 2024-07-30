/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetics.genes

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.mixins.FlatMappable
import cl.ravenhill.keen.repr.Feature
import kotlin.random.Random

/**
 * Interface representing a gene in the Keen evolutionary computation framework.
 *
 * The `Gene` interface defines the basic structure and behavior of a gene, which is a fundamental unit in the
 * evolutionary process. It extends the [Feature] and [FlatMappable] interfaces, providing additional functionality
 * specific to genes, such as mutation and value generation.
 *
 * ## Usage:
 * This interface is used to define the behavior and properties of genes in evolutionary algorithms. Implementing
 * classes must provide the `generator` property and may override the `mutate` and `flatten` methods to customize the
 * gene's behavior.
 *
 * ### Example 1: Implementing a Gene
 * ```kotlin
 * class MyGene(
 *     override val value: Int,
 *     override val generator: (Random) -> Int
 * ) : Gene<Int, MyGene> {
 *     override fun duplicateWithValue(value: Int) = MyGene(value, generator)
 * }
 * ```
 *
 * ### Example 2: Mutating a Gene
 * ```kotlin
 * val gene = MyGene(5) { random -> random.nextInt(0, 10) }
 * val mutatedGene = gene.mutate()
 * ```
 *
 * @param T The type of the value held by the gene.
 * @param G The type of the gene, which must extend [Gene].
 * @property generator A function that generates a new value for the gene using a [Random] instance.
 */
interface Gene<T, G> : Feature<T, G>, FlatMappable<T> where G : Gene<T, G> {

    /**
     * A function that generates a new value for the gene using a [Random] instance.
     */
    val generator: (T, Random) -> T

    /**
     * Creates a mutated version of this gene by generating a new value and duplicating the gene with this value.
     *
     * @return A new instance of the gene with the mutated value.
     */
    fun mutate(): G = duplicateWithValue(generator(value, Domain.random))

    /**
     * Flattens the gene to a list containing its value.
     *
     * @return A list containing the value of the gene.
     */
    override fun flatten(): List<T> = listOf(value)
}
