package cl.ravenhill.keen.arbs.operators

import cl.ravenhill.keen.arbs.datatypes.probability
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.operators.mutator.Mutator
import cl.ravenhill.keen.operators.mutator.strategies.InversionMutator
import cl.ravenhill.keen.operators.mutator.strategies.RandomMutator
import cl.ravenhill.keen.operators.mutator.strategies.SwapMutator
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.choice


/**
 * Provides an arbitrary generator for creating instances of [InversionMutator], a mutation operator
 * used in genetic algorithms. This generator is particularly useful for property-based testing,
 * allowing the creation of [InversionMutator] instances with various configurations.
 *
 * @param probability An [Arb]<[Double]> that generates the probability of mutation occurring during the mutation
 *        process.
 *        This parameter controls the likelihood that any given gene will undergo mutation.
 * @param chromosomeRate An [Arb]<[Double]> that generates the rate at which chromosomes are selected for mutation.
 *        It influences the selection of chromosomes within the population for undergoing inversion mutation.
 * @param inversionBoundary An [Arb]<[Double]> that generates the boundary rate within which gene inversion occurs.
 *        This rate determines the portion of the chromosome that is subject to inversion.

 * @return An arbitrary ([Arb]) generator that produces [InversionMutator] instances with random configurations
 *         based on the provided probabilities and rates.

 * @param T The type of the gene's value.
 * @param G The specific type of [Gene] for which the [InversionMutator] is applicable.
 */
fun <T, G> Arb.Companion.inversionMutator(
    probability: Arb<Double> = Arb.probability(),
    chromosomeRate: Arb<Double> = Arb.probability(),
    inversionBoundary: Arb<Double> = Arb.probability()
) where G : Gene<T, G> = arbitrary {
    InversionMutator<T, G>(probability.bind(), chromosomeRate.bind(), inversionBoundary.bind())
}

/**
 * Generates an arbitrary [RandomMutator] for property-based testing.
 *
 * The [Arb.Companion.randomMutator] function provides a flexible way to generate random instances of
 * `RandomMutator`, useful for testing various configurations in property-based tests. It leverages
 * Kotest's [Arb] (Arbitrary) API for generating probabilities and rates for the mutator's parameters.
 *
 * @param probability An [Arb]<[Double]> generator for the mutation probability.
 * @param chromosomeRate An [Arb]<[Double]> generator for the chromosome mutation rate.
 * @param geneRate An [Arb]<[Double]> generator for the gene mutation rate.
 *
 * @return An arbitrary generator that produces instances of [RandomMutator] with randomized parameters.
 *
 * @param T The type representing the genetic data or information.
 * @param G The type of [Gene] that the mutator operates on, which holds [T] type data.
 */
fun <T, G> Arb.Companion.randomMutator(
    probability: Arb<Double> = Arb.probability(),
    chromosomeRate: Arb<Double> = Arb.probability(),
    geneRate: Arb<Double> = Arb.probability()
) where G : Gene<T, G> = arbitrary {
    RandomMutator<T, G>(probability.bind(), chromosomeRate.bind(), geneRate.bind())
}

/**
 * Creates an arbitrary generator for [SwapMutator], tailored for property-based testing.
 * This function is ideal for generating diverse instances of `SwapMutator` with varying
 * configurations, aiding in thorough testing of genetic algorithms.
 *
 * The [SwapMutator] is a mutation operator that swaps genes within a chromosome, based on
 * specified rates. This function utilizes the Kotest's [Arb] (Arbitrary) API to create random
 * values for these rates, allowing for the generation of mutators with different mutation behaviors.
 *
 * @param probability An [Arb]<[Double]> generator for the overall mutation probability.
 *                    It determines the likelihood of mutation occurring in the genetic process.
 * @param chromosomeRate An [Arb]<[Double]> generator for the rate at which chromosomes are
 *                       selected for mutation. It affects how often chromosomes are picked for
 *                       undergoing the swap mutation.
 * @param swapRate An [Arb]<[Double]> generator for the rate at which gene swapping occurs
 *                 within a selected chromosome. This rate defines the frequency of swap mutations
 *                 happening within each chosen chromosome.
 *
 * @return An [Arb] generator that produces instances of [SwapMutator] with varying mutation
 *         probabilities and rates, offering a diverse range of mutation scenarios for testing.
 *
 * @param T The type representing the genetic data or information.
 * @param G The specific type of [Gene] that the [SwapMutator] will target.
 */
fun <T, G> Arb.Companion.swapMutator(
    probability: Arb<Double> = Arb.probability(),
    chromosomeRate: Arb<Double> = Arb.probability(),
    swapRate: Arb<Double> = Arb.probability()
) where G : Gene<T, G> = arbitrary {
    SwapMutator<T, G>(probability.bind(), chromosomeRate.bind(), swapRate.bind())
}

/**
 * Generates an arbitrary [Mutator] for `IntGene`, suitable for property-based testing.
 * This function provides a diverse range of mutators by randomly selecting from
 * [InversionMutator], [RandomMutator], and [SwapMutator], each tailored for `IntGene`.
 *
 * Utilizing Kotest's [Arb] (Arbitrary) API, this function can create different types of mutators,
 * each with its unique mutation strategy. This variety is essential for testing genetic algorithms
 * under various mutation scenarios, ensuring robustness and versatility of the algorithm.
 *
 * - [InversionMutator] randomly inverts a segment within the chromosome.
 * - [RandomMutator] alters genes randomly based on a specified rate.
 * - [SwapMutator] swaps genes within the chromosome at a given rate.
 *
 * @return An [Arb] that randomly generates one of the three specific mutators for `IntGene`.
 *         Each call to this generator results in a potentially different type of mutator,
 *         providing varied mutation strategies for testing purposes.
 */
fun <T, G> Arb.Companion.mutator(): Arb<Mutator<Int, IntGene>> where G : Gene<T, G> =
    choice(inversionMutator(), randomMutator(), swapMutator())
