/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BeInRange
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene


/**
 * Represents a genotype in a genetic algorithm, comprising a collection of chromosomes.
 *
 * A genotype is a fundamental concept in genetics and genetic algorithms, representing the entire genetic information
 * of an individual. This class encapsulates a collection of chromosomes, each carrying a segment of genetic data.
 *
 * ## Usage Example:
 * ```kotlin
 * val chromosome1 = MyChromosome(MyGene(1), MyGene(2))
 * val chromosome2 = MyChromosome(MyGene(3), MyGene(4))
 * val genotype = Genotype(listOf(chromosome1, chromosome2)) // or Genotype(chromosome1, chromosome2)
 * // Utilize genotype in genetic algorithm operations
 * ```
 *
 * @param T The type of data stored within the genes of the chromosomes.
 * @param G The specific type of gene contained within the chromosomes.
 * @property chromosomes The list of chromosomes constituting the genotype.
 * @property size The total count of chromosomes within the genotype.
 *
 * @constructor Constructs a genotype from a list or varargs of chromosomes.
 */
data class Genotype<T, G>(val chromosomes: List<Chromosome<T, G>>) :
    GeneticMaterial<T, G>, Collection<Chromosome<T, G>> where G : Gene<T, G> {


    /**
     * Secondary constructor for creating a [Genotype] instance using varargs of chromosomes.
     *
     * This constructor simplifies the creation of a [Genotype] instance by allowing the direct passing of chromosomes
     * as varargs, instead of a list. It is particularly useful when the exact number of chromosomes is known at the
     * point of genotype creation or when creating genotypes with a small number of chromosomes.
     *
     * ## Example Usage:
     * ```kotlin
     * // Creating individual chromosomes
     * val chromosome1 = Chromosome(MyGene(1), MyGene(2))
     * val chromosome2 = Chromosome(MyGene(3), MyGene(4))
     * // Constructing a genotype from individual chromosomes
     * val genotype = Genotype(chromosome1, chromosome2)
     * ```
     *
     * In the example above, the genotype is constructed directly by passing individual chromosomes,
     * enhancing readability and convenience, especially for genotypes with a predefined set of chromosomes.
     *
     * @param T The type of data encapsulated by the genes in the chromosomes.
     * @param G The specific type of gene contained within the chromosomes.
     * @param chromosomes Varargs of chromosomes to be included in the genotype.
     */
    constructor(vararg chromosomes: Chromosome<T, G>) : this(chromosomes.toList())

    override val size = chromosomes.size

    override fun isEmpty() = chromosomes.isEmpty()

    override fun containsAll(elements: Collection<Chromosome<T, G>>) = chromosomes.containsAll(elements)

    override fun contains(element: Chromosome<T, G>) = chromosomes.contains(element)

    /**
     * Verifies the integrity and validity of all chromosomes in the genotype.
     *
     * This method plays a crucial role in ensuring the genetic integrity of the genotype by performing a verification
     * check on each chromosome it contains. It sequentially invokes the `verify()` ([Chromosome.verify]) method
     * on every chromosome in the genotype's collection. The method adopts a conservative approach, where the entire
     * genotype is considered valid only if every single chromosome passes its verification test.
     *
     * If any chromosome fails its verification (i.e., its `verify()` method returns `false`), this method will
     * immediately return `false`, indicating that the genotype as a whole does not satisfy the verification criteria.
     * Otherwise, if all chromosomes pass their verification, the method returns `true`.
     *
     * This method is particularly important for maintaining the overall health and validity of the evolutionary
     * algorithm's population, especially in scenarios where specific constraints or rules must be adhered to by the
     * genetic material.
     *
     * ## Usage Example:
     * ```
     * // Assuming ChromosomeImpl is a concrete implementation of Chromosome
     * val chromosome1 = ChromosomeImpl(/* ... */)
     * val chromosome2 = ChromosomeImpl(/* ... */)
     *
     * val genotype = Genotype(chromosome1, chromosome2)
     *
     * if (genotype.verify()) {
     *     println("Genotype is valid.")
     * } else {
     *     println("Genotype validation failed.")
     * }
     * ```
     * In this example, a genotype is constructed with two chromosomes. The `verify` method checks if
     * both chromosomes in the genotype are valid. Depending on the result of this verification, a message
     * is printed indicating whether the genotype is valid or not.
     *
     * @return A boolean value indicating the verification result: `true` if all chromosomes in the genotype
     *   are valid, and `false` if any chromosome fails the verification.
     */
    override fun verify() = chromosomes.all { it.verify() }

    /**
     * Provides an iterator over the chromosomes in the genotype.
     *
     * This method facilitates iteration through the genotype's collection of chromosomes. It returns an iterator
     * that can be used to traverse the list of chromosomes in a sequential manner. This is particularly useful
     * for scenarios where you need to process or inspect each chromosome individually within the genotype.
     *
     * ## Usage Example:
     * ```
     * val genotype = Genotype(/* ... */) // Assuming genotype is initialized with chromosomes
     *
     * // Iterating through the chromosomes in the genotype
     * for (chromosome in genotype) {
     *     // Process each chromosome
     *     println(chromosome)
     * }
     * ```
     * In the example above, the `iterator` method is used to loop through each chromosome in the genotype.
     * This approach is beneficial for operations that require accessing or manipulating every chromosome
     * within the genotype, such as fitness evaluation or mutation in a genetic algorithm.
     *
     * @return An iterator that allows for iterating over the chromosomes in the genotype.
     */
    override fun iterator() = chromosomes.iterator()

    /**
     * Flattens the genotype into a list containing all the genetic values from its chromosomes.
     *
     * This method aggregates all the genetic values from each chromosome within the genotype into a single list.
     * It is particularly useful in contexts where a consolidated view of all genetic data across chromosomes is needed,
     * such as in fitness evaluation or genetic analysis. The `flatten` method simplifies the process of extracting
     * and combining genetic values from multiple chromosomes into one contiguous collection.
     *
     * ## Usage:
     * The `flatten` function can be used in various genetic algorithm operations, especially those that require
     * a holistic view of the genetic makeup of an individual represented by the genotype.
     *
     * ### Example:
     * Assuming a genotype composed of chromosomes with character genes:
     * ```kotlin
     * class CharGene(val char: Char) : Gene<Char, CharGene> {
     *     // Implementation of other methods...
     * }
     * val chromosome1 = Chromosome(listOf(CharGene('H'), CharGene('e')))
     * val chromosome2 = Chromosome(listOf(CharGene('l'), CharGene('l'), CharGene('o')))
     * val genotype = Genotype(listOf(chromosome1, chromosome2))
     * val flattenedValues = genotype.flatten() // Returns ['H', 'e', 'l', 'l', 'o']
     * ```
     * In this example, the `flatten` method combines the genetic values ('H', 'e', 'l', 'l', 'o') from
     * both chromosomes in the genotype into a single list.
     *
     * @return A list containing the aggregated genetic values from all chromosomes in the genotype.
     */
    override fun flatten() = chromosomes.flatMap { it.flatten() }

    /**
     * Accesses the chromosome at the specified index within the genotype.
     *
     * This method serves as a convenient way to retrieve specific chromosomes from the genotype using their index
     * positions. It ensures safe access to chromosomes, guarding against invalid index values that could lead to
     * out-of-bounds exceptions.
     *
     * ## Behavior:
     * - The method checks if the provided index is within the valid range (from 0 to the last index of the chromosome
     *   list).
     * - If the index is valid, the method returns the chromosome located at that index.
     * - If the index is outside the valid range, it throws a [CompositeException] containing an
     *   [IntConstraintException] to indicate the violation of index bounds.
     *
     * ## Usage Example:
     * ```
     * // Assuming a Genotype with a known number of chromosomes
     * val genotype = Genotype(/* ... */)
     *
     * // Retrieving a chromosome by index
     * val chromosomeAtIndex = genotype[2] // Retrieves the third chromosome
     * ```
     * In this example, the `get` operator is used to access the third chromosome (index 2) in the genotype.
     * It provides a concise and intuitive way to access chromosomes by their index, similar to accessing
     * elements in a list or array.
     *
     * @param index The zero-based index of the chromosome to retrieve. Must be within the range of the genotype's
     *   chromosome list size.
     * @return The chromosome located at the specified index in the genotype.
     * @throws CompositeException containing [IntConstraintException] if the index is outside the valid range.
     */
    @Throws(CompositeException::class)
    operator fun get(index: Int): Chromosome<T, G> {
        constraints {
            "The index [$index] must be in the range [0, $size)" {
                index must BeInRange(0..chromosomes.lastIndex)
            }
        }
        return chromosomes[index]
    }

    /**
     * Generates a concise and readable string representation of the genotype's chromosomes.
     *
     * This method creates a string that summarizes the genotype's content by concatenating the simple string
     * representations of each chromosome. It is especially useful for logging, debugging, or displaying the genotype in
     * a human-readable format. The method encapsulates each chromosome's simple string representation within a single
     * string, separated by commas and enclosed in brackets.
     *
     * ## Behavior:
     * - Each chromosome's `toSimpleString()` ([Chromosome.toSimpleString]) method is invoked to get its simple
     *   representation.
     * - Chromosome representations are then concatenated, separated by commas, and enclosed in square brackets.
     *
     * ## Usage Example:
     * ```
     * // Assuming a Genotype with specific chromosomes
     * val genotype = Genotype(/* ... */)
     *
     * // Getting a simple string representation of the genotype
     * val simpleString = genotype.toSimpleString()
     * // Output might look like: "[Chromosome1, Chromosome2, Chromosome3]"
     * ```
     * In this example, `toSimpleString` provides a straightforward overview of the genotype's chromosomes,
     * making it easy to understand the genotype's structure at a glance.
     *
     * @return A string representing the genotype's chromosomes in a simple, human-readable format.
     */
    override fun toSimpleString() =
        chromosomes.joinToString(separator = ", ", prefix = "[", postfix = "]") { it.toSimpleString() }


    /**
     * Provides a detailed string representation of the genotype.
     *
     * This method constructs a comprehensive string that encapsulates the overall structure and content of the
     * genotype.
     * It is designed to give a clear and detailed overview of the genotype, specifically highlighting its chromosome
     * composition. The method is particularly useful when a more descriptive representation of the genotype is
     * required, such as in detailed logs, diagnostic messages, or for debugging purposes.
     *
     * ## Structure:
     * - The representation includes the class name (`Genotype`) followed by a concise representation of its
     *   chromosomes.
     * - The `toSimpleString()` method is used to represent the chromosomes, providing a summary that is both
     *   informative and compact.
     *
     * ## Usage Example:
     * ```
     * // Assuming a Genotype with specific chromosomes
     * val genotype = Genotype(/* ... */)
     *
     * // Getting the detailed string representation of the genotype
     * val detailedString = genotype.toString()
     * // Output might look like: "Genotype(chromosomes=[Chromosome1, Chromosome2, Chromosome3])"
     * ```
     * In this example, `toString` generates a string that not only mentions it is a `Genotype` object but also gives a
     * quick look at its chromosomes' composition, offering a balance between detail and brevity.
     *
     * @return A detailed string representation of the genotype, including its class name and a summary of its
     *   chromosomes.
     */
    override fun toString() = "Genotype(chromosomes=${toSimpleString()})"


    /**
     * Generates an in-depth and detailed string representation of the genotype.
     *
     * This method provides a comprehensive view of the genotype's structure by including detailed string
     * representations of each chromosome. It is especially useful for in-depth analysis, debugging, or logging purposes
     * where a thorough understanding of the genotype's composition is necessary.
     *
     * ## Structure:
     * - The method constructs a string that encapsulates detailed information about each chromosome within the
     *   genotype.
     * - Chromosomes are represented using their `toDetailedString()` method, ensuring that each chromosome's specifics
     *   are clearly presented.
     * - Chromosomes are listed, separated by commas, and enclosed within square brackets, enhancing
     *   readability and clarity.
     *
     * ## Usage Example:
     * ```
     * // Assuming a Genotype with specific chromosomes
     * val genotype = Genotype(/* ... */)
     *
     * // Getting the detailed string representation of the genotype
     * val detailedString = genotype.toDetailedString()
     * // Output might look like:
     * // "Genotype(chromosomes=[ Chromosome1(details), Chromosome2(details), Chromosome3(details) ])"
     * ```
     * In this example, `toDetailedString` provides an extensive overview of each chromosome within
     * the genotype, detailing their individual characteristics. This representation is beneficial when
     * a more intricate understanding of the genotype's composition is required.
     *
     * @return A string that comprehensively represents the genotype, including detailed descriptions of each
     *   chromosome.
     */
    override fun toDetailedString() = "Genotype(chromosomes=${
        chromosomes.joinToString(
            prefix = "[ ",
            postfix = " ]",
            separator = ", "
        ) { it.toDetailedString() }
    })"

    /**
     * A factory for constructing [Genotype] instances with customizable chromosome configurations.
     *
     * This factory class plays a pivotal role in the generation of genotypes for genetic algorithms. It allows for the
     * creation of genotypes with diverse and specific genetic structures by leveraging a set of chromosome factories.
     * Each factory in the list is responsible for producing a chromosome, thereby enabling the assembly of genotypes
     * with varying genetic compositions.
     *
     * ## Key Features:
     * - **Customizable Chromosome Configuration**: The list of chromosome factories (`chromosomes`) can be dynamically
     *   adjusted to define the structure and characteristics of the resulting genotype.
     * - **Versatility in Genotype Creation**: Suitable for generating genotypes for a wide range of evolutionary
     *   algorithms, accommodating different types of genetic material and gene structures.
     *
     * ## Usage:
     * - Define a set of chromosome factories, each responsible for creating a specific type of chromosome.
     * - Add these factories to the `chromosomes` list of the factory.
     * - Use the `make` method to create a genotype, which will consist of chromosomes generated by these factories.
     *
     * ### Example:
     * ```kotlin
     * val chromosomeFactory1 = MyChromosomeFactory1()
     * val chromosomeFactory2 = MyChromosomeFactory2()
     *
     * val genotypeFactory = Genotype.Factory<MyDataType, MyGeneType>().apply {
     *     chromosomes += chromosomeFactory1
     *     chromosomes += chromosomeFactory2
     * }
     *
     * val genotype = genotypeFactory.make() // Genotype with chromosomes from the specified factories
     * ```
     * In this example, the `Genotype.Factory` is used to create a genotype that combines chromosomes
     * produced by two different chromosome factories.
     *
     * @param T The type of data encapsulated by the genes in the chromosomes.
     * @param G The specific type of gene contained within the chromosomes.
     * @property chromosomes A mutable list of [Chromosome.Factory] instances, each responsible for
     *   producing a chromosome. The genotype's chromosome composition is defined by these factories.
     *
     * @see Chromosome.Factory for details on how individual chromosome factories work.
     */
    class Factory<T, G> where G : Gene<T, G> {

        var chromosomes: MutableList<Chromosome.Factory<T, G>> = mutableListOf()

        /**
         * Constructs a [Genotype] instance composed of chromosomes created by the factories in the `chromosomes` list.
         *
         * This method iterates over each chromosome factory in the list, invoking its `make` method to
         * produce a chromosome. These chromosomes are then collectively used to assemble a new [Genotype] object.
         *
         * @return A [Genotype] instance composed of the chromosomes generated by the factories in the `chromosomes`
         *   list.
         */
        fun make() = Genotype(chromosomes.map { it.make() })
    }
}
