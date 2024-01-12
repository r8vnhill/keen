/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb.operators

import cl.ravenhill.keen.arb.datatypes.probability
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.alteration.mutation.RandomMutator
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

