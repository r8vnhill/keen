/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb.operators

import cl.ravenhill.keen.arb.datatypes.probability
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.alteration.mutation.InversionMutator
import cl.ravenhill.keen.operators.alteration.mutation.RandomMutator
import cl.ravenhill.keen.operators.alteration.mutation.SwapMutator
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary

/**
 * Creates a random mutator for genetic algorithms. This mutator operates at different levels (individual, chromosome,
 * and gene) with configurable rates of mutation.
 *
 * @param T The type of the individuals in the genetic algorithm.
 * @param G The type of the genes, which should inherit from [Gene]<[T], [G]>.
 * @param individualRate The probability of mutating an individual, defaults to a standard probability distribution.
 * @param chromosomeRate The probability of mutating a chromosome, defaults to a standard probability distribution.
 * @param geneRate The probability of mutating a gene, defaults to a standard probability distribution.
 * @return Returns a [RandomMutator]<[T], [G]> instance with the specified mutation rates.
 */
fun <T, G> Arb.Companion.randomMutator(
    individualRate: Arb<Double> = Arb.probability(),
    chromosomeRate: Arb<Double> = Arb.probability(),
    geneRate: Arb<Double> = Arb.probability(),
) where G : Gene<T, G> = arbitrary {
    RandomMutator<T, G>(
        individualRate = individualRate.bind(),
        chromosomeRate = chromosomeRate.bind(),
        geneRate = geneRate.bind()
    )
}

/**
 * Factory function to create arbitrary instances of [SwapMutator] for property-based testing.
 *
 * ## Overview
 * This function is part of the [Arb.Companion] object and is used to generate arbitrary instances of the `SwapMutator`
 * class. It's designed for use in property-based testing scenarios where varying configurations of the `SwapMutator`
 * are needed to thoroughly test its behavior under different conditions.
 *
 * @param T The type of value that the genes represent.
 * @param G The gene type, must extend [Gene]<[T], `G`>.
 * @param individualRate An [Arb]<[Double]> that provides an arbitrary double value for the individual mutation rate.
 *   Defaults to a probability distribution.
 * @param chromosomeRate An [Arb]<[Double]> that supplies an arbitrary double value for the chromosome mutation rate.
 *   Defaults to a probability distribution.
 * @param swapRate An [Arb]<[Double]> that generates an arbitrary double value for the swap rate within a chromosome.
 *   Defaults to a probability distribution.
 * @return An arbitrary instance of `SwapMutator<T, G>` with randomly generated mutation rates.
 */
fun <T, G> Arb.Companion.swapMutator(
    individualRate: Arb<Double> = Arb.probability(),
    chromosomeRate: Arb<Double> = Arb.probability(),
    swapRate: Arb<Double> = Arb.probability(),
) where G : Gene<T, G> = arbitrary {
    SwapMutator<T, G>(
        individualRate = individualRate.bind(),
        chromosomeRate = chromosomeRate.bind(),
        swapRate = swapRate.bind()
    )
}

/**
 * Factory function to create arbitrary instances of [InversionMutator] for property-based testing.
 *
 * ## Overview
 * This function is a part of the [Arb.Companion] object and is used to generate arbitrary instances of the
 * `InversionMutator` class. It's designed for use in property-based testing scenarios where different configurations of
 * the InversionMutator are needed to thoroughly test its behavior under various conditions.
 *
 * @param T The type of value that the genes represent.
 * @param G The gene type, must extend [Gene]<[T], `G`>.
 * @param individualRate An [Arb]<[Double]> that provides an arbitrary double value for the individual mutation rate.
 *   Defaults to [InversionMutator.DEFAULT_INDIVIDUAL_RATE].
 * @param chromosomeRate An [Arb]<[Double]> that supplies an arbitrary double value for the chromosome mutation rate.
 *   Defaults to a probability distribution.
 * @param inversionBoundaryProbability An [Arb]<[Double]> that generates an arbitrary double value for the probability
 *   of selecting inversion boundaries within a chromosome. Defaults to a probability distribution.
 * @return An arbitrary instance of `InversionMutator<T, G>` with randomly generated mutation rates and inversion
 *   boundary probabilities.
 */
fun <T, G> Arb.Companion.inversionMutator(
    individualRate: Arb<Double> = Arb.probability(),
    chromosomeRate: Arb<Double> = Arb.probability(),
    inversionBoundaryProbability: Arb<Double> = Arb.probability(),
) where G : Gene<T, G> = arbitrary {
    InversionMutator<T, G>(
        individualRate = individualRate.bind(),
        chromosomeRate = chromosomeRate.bind(),
        inversionBoundaryProbability = inversionBoundaryProbability.bind()
    )
}
