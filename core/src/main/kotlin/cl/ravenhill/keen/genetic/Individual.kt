/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.utils.isNotNaN
import java.util.*

/**
 * Represents an individual in an evolutionary algorithm population.
 *
 * An `Individual` is a fundamental entity in evolutionary algorithms, encapsulating both genetic information (genotype)
 * and a measure of its suitability (fitness) for the problem being solved. The genotype represents the set of genes
 * or characteristics that the individual possesses, while the fitness score typically reflects how well these
 * characteristics perform in the given environment or problem space.
 *
 * ## Usage:
 * In an evolutionary algorithm, individuals are typically created, evaluated for fitness, and then
 * used in genetic operations like selection, crossover, and mutation to evolve the population towards
 * better solutions.
 *
 * ### Example:
 * ```
 * val genotype = Genotype(MyChromosome(MyGene(1), MyGene(2)))
 * val individual = Individual(genotype, fitness = 5.0)
 * ```
 *
 * In this example, an `Individual` with a specific genotype and fitness score is created.
 *
 * @param T The type of the gene values in the genotype.
 * @param G The type of the genes in the genotype.
 * @param genotype The genotype of the individual, representing its genetic makeup.
 * @param fitness The fitness value of the individual, quantifying its performance or suitability.
 * @property size The number of genes in the individual's genotype.
 *
 * @constructor Creates a new individual with the specified genotype and fitness.
 *   The fitness is a measure of how well the individual's genotype performs in the given problem context.
 */
data class Individual<T, G>(val genotype: Genotype<T, G>, val fitness: Double = Double.NaN) :
    GeneticMaterial<T, G> where G : Gene<T, G> {

    val size by lazy { genotype.size }

    /**
     * Validates the integrity of an individual's genetic makeup and its fitness evaluation.
     *
     * This method plays a crucial role in ensuring the validity of an individual within an evolutionary algorithm.
     * It performs a two-fold validation:
     * 1. **Genotype Validation**: Checks the integrity and validity of the individual's genotype. This involves
     *    verifying each chromosome within the genotype to ensure they conform to expected constraints and rules.
     * 2. **Fitness Evaluation**: Confirms that the fitness score assigned to the individual is a valid number.
     *    This step is essential to avoid computational errors during fitness-based operations like selection or
     *    ranking. A valid fitness score should not be NaN (Not a Number).
     *
     * A successful validation requires both the genotype to be valid and the fitness score to be a real number.
     * The method returns `true` only if both conditions are met, indicating that the individual is suitable for
     * further processing in the evolutionary algorithm.
     *
     * ## Example:
     * ```
     * val individual = Individual(myGenotype, fitness = 4.5)
     * if (individual.verify()) {
     *     println("Individual is valid.")
     * } else {
     *     println("Invalid individual.")
     * }
     * ```
     * In this example, `individual.verify()` checks both the integrity of `myGenotype` and the validity of the
     * fitness score `4.5`. If either is invalid, it returns `false`.
     *
     * @return `true` if the individual's genotype is valid and the fitness score is a real number, `false` otherwise.
     *
     * @see Genotype.verify
     */
    override fun verify() = genotype.verify() && fitness.isNotNaN()

    /**
     * Flattens the individual's genotype into a list containing all the genetic values.
     *
     * This method aggregates the genetic values from each gene in the individual's genotype into a single list.
     * It provides a streamlined way to access and manipulate the individual's entire set of genetic data as a
     * unified collection. This functionality is especially useful in genetic algorithms for operations like
     * fitness evaluation, genetic analysis, or mutation, where working with a flat list of genetic values is
     * more convenient.
     *
     * ## Usage:
     * The `flatten` function can be applied in various scenarios within evolutionary algorithms, particularly
     * when an individual's complete genetic makeup needs to be evaluated or modified as a whole.
     *
     * ### Example:
     * Assuming an individual with a genotype of character genes:
     * ```kotlin
     * class CharGene(val char: Char) : Gene<Char, CharGene> {
     *     // Implementation of other methods...
     * }
     * val chromosome = Chromosome(listOf(CharGene('G'), CharGene('A')))
     * val individual = Individual(Genotype(listOf(chromosome)))
     * val flattenedGenes = individual.flatten() // Returns ['G', 'A']
     * ```
     * In this example, the `flatten` method consolidates the genetic values ('G', 'A') from the individual's
     * genotype into a single list, making it easier to process the genes collectively.
     *
     * @return A list containing the aggregated genetic values from the individual's genotype.
     */
    override fun flatten(): List<T> = genotype.flatten()

    /**
     * Determines whether the fitness value of the individual has been evaluated.
     *
     * In evolutionary algorithms, fitness evaluation is a critical step where each individual's performance or
     * suitability is quantified. This method checks whether the individual has undergone this fitness evaluation
     * process.
     *
     * ## Functionality:
     * - The method returns `true` if the fitness value of the individual is a valid number, signifying that fitness
     *   evaluation has been completed.
     * - If the fitness value is NaN (Not a Number), which is often used as an initial placeholder, the method returns
     *   `false`, indicating that the fitness evaluation is pending or has not been conducted.
     *
     * ## Usage:
     * This method can be used to filter or select individuals who have already been evaluated for fitness, which is
     * particularly useful in stages of the algorithm where only evaluated individuals are relevant, such as selection
     * or ranking processes.
     *
     * ### Example:
     * ```
     * val individual = Individual(genotype, Double.NaN) // Fitness not evaluated yet
     * if (individual.isEvaluated()) {
     *     // Perform operations on evaluated individuals
     * } else {
     *     // Handle individuals awaiting fitness evaluation
     * }
     * ```
     * In this example, `isEvaluated` checks if `individual` has a valid fitness score. It returns `false` initially,
     * indicating that fitness evaluation is pending.
     *
     * @return `true` if the fitness value of the individual is a valid number, indicating that fitness evaluation has
     *   been completed; `false` if the fitness is NaN, suggesting that the evaluation is yet to be performed.
     */
    fun isEvaluated() = fitness.isNotNaN()

    /**
     * Generates a simplified string representation of the individual, highlighting its genotype and fitness.
     *
     * This method provides a concise overview of the individual by combining its genotype's simple string
     * representation with its fitness value. It is particularly useful for quick inspections, logging, or any scenario
     * where a brief summary of the individual is sufficient.
     *
     * ## Format:
     * - The method formats the string as `"<Genotype> -> <Fitness>"`.
     * - `<Genotype>` is the simple string representation of the individual's genotype, obtained via
     *   [Genotype.toSimpleString].
     * - `<Fitness>` is the individual's fitness score.
     *
     * @return A string that concisely represents the individual, comprising its genotype and fitness.
     */
    override fun toSimpleString() = "${genotype.toSimpleString()} -> $fitness"

    /**
     * Generates a detailed string representation of the individual, including its genotype and fitness.
     *
     * This method constructs a comprehensive string representation that encapsulates the key aspects of the individual:
     * its genetic composition (genotype) and its fitness value. The representation is detailed, providing clear
     * visibility into the individual's genetic structure and its performance measure, which is essential for in-depth
     * analysis and debugging purposes.
     *
     * ## Format:
     * - The method formats the string as `"Individual(genotype=<Genotype>, fitness=<Fitness>)"`.
     * - `<Genotype>` is the simple string representation of the individual's genotype, obtained via
     *   [Genotype.toSimpleString].
     * - `<Fitness>` is the individual's fitness score, reflecting its suitability or performance in the given problem
     *   context.
     *
     * @return A string representing the individual with details of its genotype and fitness.
     */
    override fun toString() = "Individual(genotype=${genotype.toSimpleString()}, fitness=$fitness)"

    /**
     * Produces a comprehensive string representation of the individual, encompassing detailed information about its
     * genotype and fitness.
     *
     * This method extends the functionality of `toString()` by providing a more granular and detailed view of the
     * individual. It is particularly useful for in-depth analysis, where a complete overview of the individual's
     * genetic structure and its corresponding fitness score is required. The detailed string representation includes
     * both the intricate details of the genotype and the fitness value.
     *
     * ## Format:
     * - The output format is `"Individual(genotype=<DetailedGenotype>, fitness=<Fitness>)"`.
     * - `<DetailedGenotype>` represents a detailed string description of the individual's genotype, obtained via
     *   [Genotype.toDetailedString]. This includes more specifics about each gene and its attributes.
     * - `<Fitness>` is the fitness score of the individual, indicating its performance or suitability.
     *
     * @return A detailed string representation of the individual, including both a comprehensive view of its genotype
     *   and its fitness value.
     */
    override fun toDetailedString() = "Individual(genotype=${genotype.toDetailedString()}, fitness=$fitness)"


    /**
     * Determines if the given object is equal to this individual based on genetic composition.
     *
     * This method overrides the default equality check to focus primarily on the genetic makeup (genotype)
     * of the individual. Two individuals are considered equal if they possess identical genotypes, reflecting
     * the same genetic structure and traits. This equality check is crucial in evolutionary algorithms for
     * identifying similar or duplicate individuals within a population.
     *
     * ## Key Aspects:
     * - **Identity Check**: First, it checks if the compared object is the same instance as this individual.
     * - **Type Check**: Verifies that the other object is also an instance of `Individual`.
     * - **Genotype Equality**: Compares the genotypes of both individuals. If the genotypes are identical,
     *   the method returns `true`, signifying equality.
     *
     * @param other The object to be compared with this individual.
     * @return `true` if the other object is also an `Individual` with an identical genotype; `false` otherwise.
     */
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is Individual<*, *> -> false
        else -> genotype == other.genotype
    }

    /**
     * Generates a hash code for this individual, primarily based on its genetic makeup (genotype).
     *
     * The hash code of an individual is a crucial aspect in data structures like hash sets or hash maps,
     * where it helps in efficiently categorizing and retrieving objects. In the context of evolutionary
     * algorithms, a consistent and unique hash code for each individual can significantly optimize
     * performance when managing populations, especially in large-scale scenarios or when checking for
     * genetic duplicates.
     *
     * ## Computation:
     * - The hash code is computed using a combination of the individual's class identity and its genotype.
     * - The [Objects.hash] method is used to generate a composite hash code, ensuring a uniform distribution
     *   of hash codes and reducing collision probability.
     * - The genotype's hash code plays a primary role, as it encapsulates the individual's genetic information.
     *   This ensures that individuals with identical genotypes will have the same hash code.
     *
     * @return An integer representing the hash code of this individual, derived from its genotype.
     */
    override fun hashCode() = Objects.hash(Individual::class, genotype)
}
