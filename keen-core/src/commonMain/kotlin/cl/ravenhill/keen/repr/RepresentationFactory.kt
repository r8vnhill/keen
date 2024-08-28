package cl.ravenhill.keen.repr

import cl.ravenhill.keen.Domain
import kotlin.random.Random

/**
 * Factory interface for creating representations in an evolutionary algorithm.
 *
 * The `RepresentationFactory` interface defines a contract for factories that generate representations, which are
 * higher-level abstractions of collections of features (such as genes in a genetic algorithm). This interface is
 * designed to be flexible and supports asynchronous operations through Kotlin's `suspend` functions, making it suitable
 * for environments where non-blocking operations are essential, such as Kotlin/JS.
 *
 * ## Usage:
 * This interface is intended to be implemented by factories that create specific types of representations within an
 * evolutionary algorithm. The factory provides a mechanism for generating representations of a predefined size, using
 * a random generator to introduce variability. The `invoke` function is marked as `suspend` to enable asynchronous
 * execution, facilitating efficient, non-blocking construction of representations.
 *
 * ### Example: Implementing a Custom Representation Factory
 * ```kotlin
 * class MyRepresentationFactory : RepresentationFactory<Int, IntGene, IntChromosome> {
 *     override var size: Int = 10
 *
 *     override suspend fun invoke(random: Random): Result<IntChromosome> = runCatching {
 *         val genes = List(size) { IntGene(random.nextInt(0, 100)) }
 *         IntChromosome(genes)
 *     }
 * }
 * ```
 *
 * @param T The type of the value held by the features within the representation.
 * @param F The type of the feature, which must extend [Feature].
 * @param R The type of the representation, which must extend [Representation].
 */
interface RepresentationFactory<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {

    /**
     * The number of features to include in the generated representation.
     *
     * This property defines the size of the representation that will be generated. It determines the number of features
     * that will be included in the resulting representation, allowing for customizable and flexible factory behavior.
     */
    var size: Int

    /**
     * Asynchronously creates a representation of the predefined size.
     *
     * The `invoke` function is the primary method for generating representations. It is a `suspend` function, allowing
     * for non-blocking execution, which is particularly useful in environments like Kotlin/JS or when dealing with
     * large-scale, computationally intensive tasks. The function uses a `Random` instance to introduce variability
     * into the creation process.
     *
     * @param random The random number generator used to produce variability in the features. Defaults to
     *   [Domain.random].
     * @return A [Result] containing the generated representation, or an exception if the generation fails.
     */
    suspend operator fun invoke(random: Random = Domain.random): Result<R>
}
