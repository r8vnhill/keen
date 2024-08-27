/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetics.chromosomes

import cl.ravenhill.keen.evolution.executors.construction.ConstructorExecutor
import cl.ravenhill.keen.evolution.executors.construction.SequentialConstructor
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.repr.RepresentationFactory

/**
 * Factory interface for creating chromosomes in an evolutionary algorithm.
 *
 * The `ChromosomeFactory` interface defines the structure for factories that generate chromosomes, which are
 * collections of genes. It extends the `RepresentationFactory` interface, specifically targeting the creation of
 * `Chromosome` instances. The factory uses a `ConstructorExecutor` to handle the creation of the gene sequences that
 * make up the chromosome.
 *
 * ## Usage:
 * This interface is intended for use in scenarios where specific chromosomes need to be generated within an evolutionary
 * algorithm. It provides a flexible mechanism for customizing how the genes within a chromosome are constructed, by
 * allowing the use of different `ConstructorExecutor` implementations.
 *
 * ### Example 1: Creating a Custom Chromosome Factory
 * ```kotlin
 * class MyChromosomeFactory : ChromosomeFactory<Int, IntGene> {
 *     override var executor: ConstructorExecutor<IntGene> = MyCustomConstructor()
 *
 *     override fun create(): Chromosome<Int, IntGene> {
 *         val genes = executor.invoke(10) { IntGene(it) }
 *         return IntChromosome(genes)
 *     }
 * }
 * ```
 *
 * ## Recommendation:
 * It is generally recommended to use the `AbstractChromosomeFactory` class instead of directly implementing the
 * `ChromosomeFactory` interface. The abstract class provides a default implementation for the `executor` property,
 * making it easier to extend and customize the factory without needing to re-implement basic functionality.
 * Using the abstract factory pattern also promotes consistency and reduces boilerplate code across different
 * implementations.
 *
 * ## Benefits of Using `AbstractChromosomeFactory`:
 * - **Reduced Boilerplate**: The abstract class provides a default `SequentialConstructor` for the `executor`,
 *   reducing the need for repetitive code.
 * - **Extensibility**: By extending the abstract class, you can easily customize the behavior of the factory
 *   without having to start from scratch.
 * - **Consistency**: Using the abstract factory pattern ensures that all factories share a common structure and
 *   initialization process, making the codebase more maintainable and understandable.
 *
 * ### Example 2: Extending `AbstractChromosomeFactory`
 * ```kotlin
 * class MyChromosomeFactory : AbstractChromosomeFactory<Int, IntGene>() {
 *     override fun make(): Chromosome<Int, IntGene> {
 *         val genes = executor(10) { IntGene(it) }
 *         return IntChromosome(genes)
 *     }
 * }
 * ```
 *
 * @param T The type of the value held by the genes in the chromosome.
 * @param G The type of the gene, which must extend [Gene].
 * @property executor The `ConstructorExecutor` used to generate the sequence of genes within the chromosome.
 */
interface ChromosomeFactory<T, G> : RepresentationFactory<T, G, Chromosome<T, G>> where G : Gene<T, G> {

    /**
     * The `ConstructorExecutor` used to generate the sequence of genes within the chromosome.
     *
     * This property allows for customization of how the genes in the chromosome are created. By default, this can be
     * set to any implementation of `ConstructorExecutor` that suits the specific requirements of the genetic algorithm.
     */
    var executor: ConstructorExecutor<G>
}

/**
 * Abstract factory class for creating chromosomes in an evolutionary algorithm.
 *
 * The `AbstractChromosomeFactory` class provides a base implementation for the `ChromosomeFactory` interface,
 * offering a default `SequentialConstructor` for the `executor` property. This class is designed to be extended
 * by concrete factory implementations, simplifying the process of creating custom chromosome factories while
 * promoting code reuse and consistency.
 *
 * ## Usage:
 * Extend this class to create a custom chromosome factory. The default `SequentialConstructor` can be replaced
 * with a different `ConstructorExecutor` if needed, allowing for flexible and varied construction strategies
 * for the genes within the chromosome.
 *
 * ### Example 1: Extending `AbstractChromosomeFactory`
 * ```kotlin
 * class MyChromosomeFactory : AbstractChromosomeFactory<Int, IntGene>() {
 *     override fun create(): Chromosome<Int, IntGene> {
 *         val genes = executor.invoke(10) { IntGene(it) }
 *         return IntChromosome(genes)
 *     }
 * }
 * ```
 *
 * ## Benefits of Using `AbstractChromosomeFactory`:
 * - **Default Implementation**: The abstract class provides a sensible default for the `executor`, which reduces
 *   the amount of code you need to write when creating a new factory.
 * - **Ease of Extension**: This class is designed for easy extension, allowing developers to focus on the specific
 *   details of chromosome creation without worrying about the boilerplate.
 * - **Promotes Consistency**: By using this abstract class, you ensure that all chromosome factories in your
 *   application share a common structure, making your codebase more maintainable.
 *
 * @param T The type of the value held by the genes in the chromosome.
 * @param G The type of the gene, which must extend [Gene].
 * @property executor The `ConstructorExecutor` used to generate the sequence of genes within the chromosome.
 *   Defaults to a `SequentialConstructor`.
 */
abstract class AbstractChromosomeFactory<T, G> : ChromosomeFactory<T, G> where G : Gene<T, G> {
    override var executor: ConstructorExecutor<G> = SequentialConstructor()
}
