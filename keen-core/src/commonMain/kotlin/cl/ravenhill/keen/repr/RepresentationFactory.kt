package cl.ravenhill.keen.repr

import cl.ravenhill.keen.Domain
import kotlin.random.Random

/**
 * Factory interface for creating representations in an evolutionary algorithm.
 *
 * The `RepresentationFactory` interface defines the contract for factories that generate representations of a
 * particular type in an evolutionary computation framework. A representation typically consists of a collection of
 * features (e.g., genes) that define the structure or configuration of an individual in the population.
 *
 * This interface provides a method for generating representations, encapsulating the creation process in a `Result` to
 * handle potential errors gracefully. This allows for safe and reliable construction of representations, particularly
 * in scenarios where constraints or validations may fail during the generation process.
 *
 * ## Usage:
 * This interface is intended to be implemented by classes that need to generate specific types of representations, such
 * as chromosomes, genotypes, or other structures composed of features. The factory method `invoke` creates a
 * representation of the specified size, returning the result encapsulated in a `Result` object, which can either be a
 * successful representation or an error.
 *
 * ### Example 1: Implementing a Chromosome Factory
 * ```kotlin
 * class ChromosomeFactoryImpl : RepresentationFactory<Int, IntGene, Chromosome<Int, IntGene>> {
 *     override fun invoke(size: Int): Result<Chromosome<Int, IntGene>> = runCatching {
 *         require(size > 0) { "Size must be positive" }
 *         val genes = List(size) { IntGene(it) }
 *         IntChromosome(genes)
 *     }
 * }
 * ```
 *
 * ### Example 2: Handling Errors in Genotype Generation
 * ```kotlin
 * class GenotypeFactory : RepresentationFactory<Int, IntGene, Genotype<Int, IntGene>> {
 *     override fun invoke(size: Int): Result<Genotype<Int, IntGene>> = runCatching {
 *         require(size > 0) { "Size must be positive" }
 *         val chromosomes = List(size) { ChromosomeFactoryImpl().invoke(size).getOrThrow() }
 *         Genotype(chromosomes)
 *     }
 * }
 * ```
 *
 * @param T The type of the value held by the features in the representation.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 */
interface RepresentationFactory<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {

    /**
     * Creates a representation with the specified size, returning a result that encapsulates either the successful
     * representation or an error.
     *
     * This operator function generates a representation, typically a collection of features, with the given size.
     * The size parameter usually determines the number of features in the resulting representation. The process
     * is encapsulated in a `Result` to handle potential errors, such as invalid size parameters or failures in
     * feature generation.
     *
     * ## Constraints:
     * - Implementers should ensure that the size parameter is greater than zero to avoid having invalid or empty
     *   representations.
     *
     * @param size The size of the representation to create, usually indicating the number of features or elements it
     *  contains.
     * @param random The random number generator used to generate the representation.
     * @return A `Result` containing the representation of type `R` if successful, or an exception if an error occurs.
     */
    operator fun invoke(size: Int, random: Random = Domain.random): Result<R>
}
