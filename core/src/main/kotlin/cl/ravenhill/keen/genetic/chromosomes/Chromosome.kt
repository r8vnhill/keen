/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.evolution.executors.ConstructorExecutor
import cl.ravenhill.keen.evolution.executors.SequentialConstructor
import cl.ravenhill.keen.genetic.GeneticMaterial
import cl.ravenhill.keen.genetic.genes.Gene
import kotlin.properties.Delegates


/**
 * Represents a chromosome in genetic algorithms, encapsulating a collection of genes.
 *
 * This interface extends `GeneticMaterial` and implements `Iterable<G>` to provide
 * functionalities relevant to genetic operations. A chromosome is a sequence of genes,
 * each holding a piece of genetic data.
 *
 * ## Key Features:
 * - **Gene Collection**: Holds a list of genes (`genes`) which together form the chromosome.
 * - **Verification**: Implements the `verify` method from `GeneticMaterial` to ensure the validity
 *   of all genes within the chromosome.
 * - **Iteration and Indexing**: Allows iteration over genes and direct access to genes at specific indices.
 * - **Transformation and Copying**: Supports transforming the genetic data and creating copies of the chromosome.
 *
 * ## Usage:
 * Implement this interface to create custom chromosome types for use in genetic algorithms.
 * A chromosome is a fundamental component in these algorithms, representing a segment of
 * an individual's genetic makeup.
 *
 * ### Example:
 * Implementing a custom chromosome:
 * ```kotlin
 * data class MyChromosome(val myGenes: List<MyGene>) : Chromosome<Int, MyGene> {
 *     override val genes = myGenes
 *
 *     override fun copy(genes: List<MyGene>): MyChromosome = MyChromosome(genes)
 *
 *     // Optional: Override other methods as needed
 * }
 * ```
 * In this example, `MyChromosome` implements `Chromosome` to hold a list of `MyGene`.
 * The `copy` method allows creating new instances of `MyChromosome` with potentially modified genes.
 *
 * It is __recommended__ to implement chromosomes as _data classes_ to leverage the built-in functionalities.
 *
 * @param T The type of the genetic data.
 * @param G The specific type of `Gene` that encapsulates the genetic data.
 *
 * @property genes A list of genes that make up the chromosome.
 * @property size The size of the chromosome, determined by the number of genes it contains.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @version 2.0.0
 * @since 1.0.0
 */
interface Chromosome<T, G> : GeneticMaterial<T, G>, Collection<G> where G : Gene<T, G> {

    val genes: List<G>

    override val size: Int
        get() = genes.size


    /**
     * Creates a new chromosome by duplicating the genes from the provided list.
     *
     * This function is essential in scenarios where a new chromosome instance needs to be created based on an existing
     * set of genes. The provided list of genes is duplicated, creating a new set of genes that are then used to
     * construct a new chromosome. This approach is particularly useful in evolutionary algorithms during operations
     * such as crossover or mutation, where new chromosomes are formed from existing genetic material.
     *
     * ## Key Features:
     * - **Genetic Duplication**: The method duplicates each gene in the provided list, ensuring that the new chromosome
     *   is a distinct entity with its own set of genes.
     * - **Preservation of Gene Characteristics**: The duplication process maintains the properties and characteristics
     *   of each gene, ensuring the new chromosome accurately reflects the original genetic information.
     *
     * ## Usage:
     * Implementing classes can use this function to create new chromosomes as part of evolutionary processes,
     * such as during the reproduction phase, where offspring chromosomes are formed from parent genes.
     *
     * ### Example:
     * Assuming an implementation of a `Chromosome` class:
     * ```kotlin
     * class MyChromosome(val myGenes: List<MyGene>) : Chromosome<MyDataType, MyGene> {
     *     // Implementations of other chromosome methods...
     *
     *     override fun duplicateWithGenes(genes: List<MyGene>) = MyChromosome(genes.map { it.copy() })
     * }
     *
     * val originalChromosome = MyChromosome(listOf(MyGene(1), MyGene(2)))
     * val newChromosome = originalChromosome.duplicateWithGenes(originalChromosome.genes)
     * ```
     * In this example, `duplicateWithGenes` is used to create a new instance of `MyChromosome` using the
     * genes from an existing chromosome.
     *
     * @param genes A list of genes of type [G] from which to create the new chromosome. These genes are duplicated
     *   to form the genetic material of the new chromosome.
     * @return A new [Chromosome] instance constructed with the duplicated genes.
     */
    fun duplicateWithGenes(genes: List<G>): Chromosome<T, G>

    /**
     * Verifies the chromosome by ensuring all its genes pass their respective verification checks.
     *
     * @return `true` if all genes are valid, otherwise `false`.
     */
    override fun verify() = genes.all { it.verify() }

    /**
     * Determines whether the chromosome has no genes.
     *
     * This function checks if the chromosome contains any genes. It is an essential method for validating the state of
     * a chromosome, especially in scenarios where the presence of genetic material is critical for further processing
     * or evaluation.
     *
     * ## Key Features:
     * - **Empty State Check**: The method provides a quick and straightforward way to verify if the chromosome is
     *   empty, i.e., devoid of any genes.
     * - **Genetic Validity**: An empty chromosome might be indicative of an erroneous or incomplete genetic setup. This
     *   method can be used as part of validation checks in evolutionary algorithms.
     *
     * ## Usage:
     * This method is particularly useful in genetic algorithms and other evolutionary computation scenarios where
     * chromosomes must contain genes to be considered valid or functional. It can be used in checks before performing
     * operations like crossover, mutation, or fitness evaluation to ensure that chromosomes have the necessary
     * genetic material.
     *
     * ### Example:
     * ```
     * class MyChromosome(val myGenes: List<MyGene>) : Chromosome<MyDataType, MyGene> {
     *     /* ... */
     * }
     *
     * val chromosome = MyChromosome(emptyList())
     * if (chromosome.isEmpty()) {
     *     println("Chromosome has no genes.")
     * } else {
     *     println("Chromosome contains genes.")
     * }
     * ```
     * In this example, `isEmpty` is used to check whether `MyChromosome` contains any genes. An empty chromosome
     * will trigger a message indicating the absence of genes.
     *
     * The overriding of this method is __optional__, as the default implementation is sufficient for most use cases.
     *
     * @return `true` if the chromosome contains no genes, `false` otherwise.
     */
    override fun isEmpty() = genes.isEmpty()

    /**
     * Creates an iterator for traversing through the genes of the chromosome.
     *
     * This method allows for iterating over the genes contained within the chromosome, facilitating access to each gene
     * in sequence. The iterator follows the order in which the genes are stored in the chromosome, starting from the
     * first gene and progressing to the last. This feature is particularly useful for scenarios where genes need to be
     * examined or processed one at a time, such as in genetic analysis or during mutation and crossover operations in
     * evolutionary algorithms.
     *
     * ## Usage:
     * The iterator can be used in a `for` loop or other iteration constructs to access and manipulate individual genes
     * within the chromosome.
     *
     * ### Example:
     * ```
     * class MyChromosome(val myGenes: List<MyGene>) : Chromosome<MyDataType, MyGene> {
     *     // Other implementations...
     * }
     *
     * val chromosome = MyChromosome(listOf(MyGene(1), MyGene(2), MyGene(3)))
     * for (gene in chromosome) {
     *     println("Gene value: ${gene.value}")
     * }
     * ```
     * In this example, the iterator is used to loop through each gene in `MyChromosome`, allowing for individual
     * processing or examination of each gene.
     *
     * @return An iterator that provides sequential access to the genes within the chromosome.
     */
    override fun iterator() = genes.iterator()

    /**
     * Checks if a specific gene is present in the chromosome.
     *
     * This method is used to determine whether a given gene ([element]) is part of the chromosome's gene sequence.
     * It provides an efficient way to ascertain the presence of a particular gene within the chromosome, which can be
     * useful in various genetic algorithm operations, such as ensuring genetic diversity, verifying gene combinations,
     * or during selection processes.
     *
     * ## Usage:
     * This method can be called to verify if a specific gene is included in the chromosome. This is particularly
     * relevant in situations where the presence or absence of certain genes can influence the behavior or outcome
     * of genetic operations.
     *
     * ### Example:
     * ```
     * class MyChromosome(val myGenes: List<MyGene>) : Chromosome<MyDataType, MyGene> {
     *     // Other implementations...
     * }
     *
     * val myGene = MyGene(420)
     * val chromosome = MyChromosome(listOf(MyGene(1), myGene))
     *
     * if (myGene in chromosome) {
     *     println("Gene is present in the chromosome.")
     * } else {
     *     println("Gene is not in the chromosome.")
     * }
     * ```
     * In this example, `contains` is used to check if `myGene` is part of `MyChromosome`. The method returns `true` if
     * the gene is found, indicating its presence.
     *
     * @param element The gene to be checked for its presence in the chromosome.
     * @return `true` if the chromosome contains the specified gene, `false` otherwise.
     */
    override fun contains(element: G) = genes.contains(element)

    /**
     * Checks if all specified genes are present in the chromosome.
     *
     * This method is used to verify whether a collection of genes ([elements]) is entirely included within the
     * chromosome's gene sequence. It is particularly useful in evolutionary algorithms for ensuring that a set of
     * desired genes is present in a chromosome, which can be important for maintaining specific traits, diversity, or
     * characteristics in the genetic population.
     *
     * ## Usage:
     * This method can be employed in scenarios where the presence of a specific combination or set of genes is
     * critical. For example, it can be used during the initialization of a population to ensure that certain genetic
     * traits are present, or in crossover and mutation operations to maintain essential genes.
     *
     * ### Example:
     * ```
     * class MyChromosome(val myGenes: List<MyGene>) : Chromosome<MyDataType, MyGene> {
     *     // Other implementations...
     * }
     *
     * val requiredGenes = listOf(MyGene(1), MyGene(2))
     * val chromosome = MyChromosome(listOf(MyGene(1), MyGene(2), MyGene(3)))
     *
     * if (chromosome.containsAll(requiredGenes)) {
     *     println("All required genes are present in the chromosome.")
     * } else {
     *     println("Some required genes are missing from the chromosome.")
     * }
     * ```
     * In this example, `containsAll` is used to check if `chromosome` includes all genes listed in `requiredGenes`.
     * The method returns `true` if all the genes in `requiredGenes` are found in `chromosome`.
     *
     * @param elements A collection of genes to be checked for their presence in the chromosome.
     * @return `true` if the chromosome contains all the specified genes, `false` otherwise.
     */
    override fun containsAll(elements: Collection<G>) = genes.containsAll(elements)

    /**
     * Retrieves the gene at the specified index within the chromosome.
     *
     * This method is essential for accessing individual genes in a chromosome by their position. It is particularly
     * useful in evolutionary algorithms during processes like crossover, mutation, or fitness evaluation, where
     * specific genes need to be accessed and potentially altered or analyzed.
     *
     * ## Usage:
     * The `get` method allows direct access to genes based on their index, enabling precise manipulation or inspection
     * of genetic data within the chromosome. This is crucial in scenarios where the position of a gene in the
     * chromosome plays a significant role in the evolutionary algorithm.
     *
     * ### Example:
     * ```
     * class MyChromosome(val myGenes: List<MyGene>) : Chromosome<MyDataType, MyGene> {
     *     // Other implementations...
     * }
     *
     * val chromosome = MyChromosome(listOf(MyGene(1), MyGene(2), MyGene(3)))
     * val firstGene = chromosome[0] // Accessing the first gene
     * ```
     * In this example, the `get` method is used to access the first gene in `MyChromosome`. This approach can be
     * extended to access any gene at a specific index within the chromosome.
     *
     * @param index The zero-based index of the gene to retrieve. It must be within the bounds of the chromosome's gene
     *   list.
     * @return The gene located at the specified index within the chromosome.
     * @throws IndexOutOfBoundsException if the index is out of the chromosome's bounds.
     */
    operator fun get(index: Int): G = genes[index]

    /**
     * Applies a transformation function to each gene's value in the chromosome and accumulates the results.
     *
     * This method is crucial for performing collective transformations on the genetic material within a chromosome. It
     * iterates over each gene in the chromosome, applies a transformation function to the gene's value, and then
     * aggregates the transformed values into a new list. This approach is particularly useful in evolutionary
     * algorithms where modifications or analyses of gene values are necessary.
     *
     * ## Functionality:
     * - Iterates through each gene in the chromosome.
     * - Applies the provided [transform] function to the value of each gene.
     * - Collects and returns the transformed values in a new list.
     *
     * ## Usage:
     * This method can be employed in various stages of a genetic algorithm, such as fitness evaluation or mutation,
     * where gene values need to be processed or altered en masse. It allows for the application of complex
     * transformations to genetic data and aggregation of the results.
     *
     * ### Example:
     * ```
     * class MyChromosome(val myGenes: List<MyGene>) : Chromosome<MyDataType, MyGene> {
     *     // Other implementations...
     * }
     *
     * val chromosome = MyChromosome(listOf(MyGene(1), MyGene(2), MyGene(3)))
     * // Applying a transformation function to double each gene's value
     * val transformedValues = chromosome.flatMap { it * 2 }
     * // The result is a list of transformed gene values: [2, 4, 6]
     * ```
     * In this example, `flatMap` is used to apply a transformation (doubling the value) to each gene in `MyChromosome`.
     * The result is a list of these transformed values.
     *
     * @param transform A function that takes a value of type [T] and returns a transformed value of the same type.
     * @return A list containing the transformed values obtained by applying [transform] to each gene's value in the
     *   chromosome.
     */
    override fun flatMap(transform: (T) -> T): List<T> = genes.flatMap { gene -> gene.flatMap(transform) }

    /**
     * Generates a simple string representation of the chromosome's genes.
     *
     * This method concatenates the simple string representations of each gene in the chromosome into a single,
     * readable string. The representations are separated by commas and enclosed within square brackets. This format
     * is useful for providing a concise overview of the chromosome's genetic makeup.
     *
     * The `toSimpleString` ([Gene.toSimpleString]) method of each gene is called to obtain its individual
     * representation. This approach allows for the customization of gene string representations while maintaining a
     * consistent overall format for the chromosome.
     *
     * @return A string representing the chromosome's genes, formatted as a list enclosed in square brackets and
     *   separated by commas.
     */
    override fun toSimpleString() = genes.joinToString(
        separator = ", ",
        prefix = "[",
        postfix = "]"
    ) { it.toSimpleString() }

    /**
     * Generates a detailed string representation of the chromosome.
     *
     * This method provides a comprehensive view of the chromosome by including detailed information about each gene.
     * It constructs a string that identifies the chromosome's class and enumerates its genes in a detailed format.
     * This is particularly useful for in-depth analysis or debugging purposes, where more information about the
     * chromosome's genetic composition is required.
     *
     * The method calls `toDetailedString` on each gene, which should return a string representation that includes
     * more detailed or specific information about the gene. The results for each gene are then combined into a
     * single string, providing a complete overview of the chromosome's genetic makeup.
     *
     * @return A detailed string representation of the chromosome, including the class name and detailed information
     *   about each gene.
     */
    override fun toDetailedString() =
        "${this::class.simpleName}(genes=${genes.map { it.toDetailedString() }}"

    /**
     * A factory interface for creating chromosome instances.
     *
     * This interface defines the essential structure for factories that produce chromosomes for use in genetic
     * algorithms. It allows for the customization and instantiation of chromosomes with specific genetic
     * configurations.
     *
     * ### Usage:
     * Implement this interface to define a custom chromosome factory. The factory can then be used to create
     * chromosomes with specific characteristics, suitable for the genetic algorithm being implemented.
     *
     * It is __recommended__ to extend the [AbstractFactory] class when implementing this interface.
     *
     * @param T The type representing the genetic data or information.
     * @param G The specific type of [Gene] that the factory produces.
     * @property executor An instance of [ConstructorExecutor] that is responsible for constructing gene instances. It
     *   defines how individual genes are created and initialized.
     * @property size An integer representing the number of genes in the chromosome. This property dictates the length
     *   of the chromosome being constructed.
     */
    interface Factory<T, G> where G : Gene<T, G> {
        var executor: ConstructorExecutor<G>
        var size: Int

        /**
         * Creates a new chromosome instance.
         *
         * @return A new chromosome of type [Chromosome].
         */
        fun make(): Chromosome<T, G>
    }

    /**
     * An abstract class providing a base implementation for the [Factory] interface.
     *
     * This class serves as a foundational template for creating chromosome factories within genetic algorithms.
     * It implements common aspects of the [Factory] interface, offering a starting point for further customization
     * and specialization.
     *
     * Implementors of this class should provide specific logic for chromosome creation while leveraging the
     * predefined structure and functionality of this abstract class.
     *
     * ### Implementation:
     * Concrete subclasses are required to define the [make] method, which creates and returns a chromosome instance.
     *
     * ### Example Usage:
     * ```kotlin
     * class MyChromosomeFactory : AbstractFactory<MyDataType, MyGeneType>() {
     *     init {
     *         size = 10 // Setting the size of chromosomes to be created
     *     }
     *
     *     override fun make(): Chromosome<MyDataType, MyGeneType> {
     *         // Implement the logic to create a chromosome
     *     }
     * }
     * ```
     *
     * In this example, `MyChromosomeFactory` extends `AbstractFactory` and sets the size of chromosomes.
     * The `make` method is implemented to define how chromosomes are created, specific to the requirements
     * of the genetic algorithm.
     *
     * @param T The type representing the genetic data or information.
     * @param G The specific type of [Gene] produced by the factory.}
     * @property size The number of genes in the chromosome. Must be initialized by the concrete implementation.
     * @property executor The executor responsible for constructing gene instances. Defaults to [SequentialConstructor],
     *   but can be overridden for custom gene creation logic.
     */
    abstract class AbstractFactory<T, G> : Factory<T, G> where G : Gene<T, G> {

        override var size: Int by Delegates.notNull()

        override var executor: ConstructorExecutor<G> = SequentialConstructor()
    }
}
