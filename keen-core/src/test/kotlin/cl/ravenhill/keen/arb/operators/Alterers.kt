/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb.operators

import cl.ravenhill.keen.arb.datatypes.arbProbability
import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import cl.ravenhill.keen.operators.alteration.Alterer
import cl.ravenhill.keen.operators.alteration.crossover.Crossover
import cl.ravenhill.keen.operators.alteration.mutation.BitFlipMutator
import cl.ravenhill.keen.operators.alteration.mutation.Mutator
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.next

/**
 * Generates an arbitrary generator for `Alterer<T, G>` instances, designed for property-based testing in evolutionary
 * algorithms.
 *
 * @return An `Arb<Alterer<T, G>>` that produces `Alterer` instances capable of truncating populations.
 * @param T The type parameter representing the value type within the gene.
 * @param G The gene type, constrained to be a subclass of `Gene<T, G>`.
 */
fun <T, G> arbAlterer(): Arb<Alterer<T, G>> where G : Gene<T, G> = arbitrary {
    object : Alterer<T, G> {
        override fun invoke(state: EvolutionState<T, G>, outputSize: Int) =
            state.copy(population = state.population.take(outputSize))
    }
}

/**
 * Creates an arbitrary generator for ``Mutator<T, G>`` instances with configurable mutation rates.
 *
 * @param T The type parameter representing the value type in the gene.
 * @param G The gene type, extending `Gene<T, G>`.
 * @param individualRate An optional `Arb<Double>` for the individual mutation rate. Defaults to a probability value.
 * @param chromosomeRate An optional `Arb<Double>` for the chromosome mutation rate. Defaults to a probability value.
 * @return An `Arb<Mutator<T, G>>` for generating `Mutator` instances with varying mutation rates.
 */
fun <T, G> arbBaseMutator(
    individualRate: Arb<Double> = arbProbability(),
    chromosomeRate: Arb<Double> = arbProbability(),
): Arb<Mutator<T, G>> where G : Gene<T, G> = arbitrary {
    object : Mutator<T, G> {
        override val individualRate = individualRate.next()
        override val chromosomeRate = chromosomeRate.next()
        override fun mutateChromosome(chromosome: Chromosome<T, G>) =
            chromosome.duplicateWithGenes(chromosome.reversed())
    }
}

/**
 * Creates an arbitrary generator for `BitFlipMutator<G>` instances with configurable mutation rates.
 *
 * @param G The gene type, extending `Gene<Boolean, G>`.
 * @param individualRate An optional `Arb<Double>` for the individual mutation rate. Defaults to a probability value.
 * @param chromosomeRate An optional `Arb<Double>` for the chromosome mutation rate. Defaults to a probability value.
 * @param geneRate An optional `Arb<Double>` for the gene mutation rate. Defaults to a probability value.
 * @return An `Arb<BitFlipMutator<G>>` for generating `BitFlipMutator` instances with configurable mutation rates.
 */
fun <G> arbBitFlipMutator(
    individualRate: Arb<Double> = arbProbability(),
    chromosomeRate: Arb<Double> = arbProbability(),
    geneRate: Arb<Double> = arbProbability(),
): Arb<BitFlipMutator<G>> where G : Gene<Boolean, G> = arbitrary {
    BitFlipMutator(individualRate.bind(), chromosomeRate.bind(), geneRate.bind())
}

/**
 * Creates an arbitrary generator for selecting a mutator with configurable mutation rates from a set of available
 * mutators.
 *
 * @param individualRate An optional `Arb<Double>` for the individual mutation rate, used for `baseMutator`.
 *                       Defaults to a probability value.
 * @param chromosomeRate An optional `Arb<Double>` for the chromosome mutation rate, used for `baseMutator`.
 *                       Defaults to a probability value.
 * @return An arbitrary generator for either a `baseMutator` or `bitFlipMutator`, selected randomly.
 */
fun arbAnyMutator(
    individualRate: Arb<Double> = arbProbability(),
    chromosomeRate: Arb<Double> = arbProbability(),
) = Arb.choice(arbBaseMutator<Int, IntGene>(individualRate, chromosomeRate))

/**
 * Generates an arbitrary generator for `Crossover<T, G>` instances, suitable for property-based testing of crossover
 * operations in evolutionary algorithms.
 *
 * ## Functionality:
 * - The generated `Crossover` instances can produce a specified number of offspring from a set number of parents.
 * - The `chromosomeRate` determines the likelihood of each chromosome being subject to crossover.
 * - The `exclusivity` flag indicates whether the crossover operation is exclusive, affecting how offspring are
 * generated.
 * - The `crossoverChromosomes` method provided in the implementation reverses the genes of each chromosome and takes
 * the first `numOffspring` chromosomes to form the offspring.
 *
 *
 * @param T The type parameter representing the value type within the gene.
 * @param G The gene type, constrained to be a subclass of `Gene<T, G>`.
 * @param numOffspring
 *  An `Arb<Int>` for generating the number of offspring to be produced by the crossover operation, defaulting to a
 *  range of 1 to 10.
 * @param numParents
 *  An `Arb<Int>` for generating the number of parents involved in the crossover, defaulting to a range of 2 to 10.
 * @param chromosomeRate
 *  An `Arb<Double>` for generating the rate at which chromosomes are selected for crossover, using [arbProbability]
 *  by default.
 * @param exclusivity
 *  An `Arb<Boolean>` for generating the exclusivity flag of the crossover operation, defaulting to a random boolean.
 *
 * @return An `Arb<Crossover<T, G>>` that produces `Crossover` instances with the specified parameters.
 */
fun <T, G> arbBaseCrossover(
    numOffspring: Arb<Int> = Arb.int(1..10),
    numParents: Arb<Int> = Arb.int(2..10),
    chromosomeRate: Arb<Double> = arbProbability(),
    exclusivity: Arb<Boolean> = Arb.boolean(),
): Arb<Crossover<T, G>> where G : Gene<T, G> = arbitrary {
    val nOffspring = numOffspring.bind()
    val nParents = numParents.bind()
    val cRate = chromosomeRate.bind()
    val exclusive = exclusivity.bind()
    object : Crossover<T, G> {
        override val numOffspring: Int = nOffspring
        override val numParents: Int = nParents
        override val chromosomeRate: Double = cRate
        override val exclusivity: Boolean = exclusive

        override fun crossoverChromosomes(chromosomes: List<Chromosome<T, G>>) =
            chromosomes.map { it.duplicateWithGenes(it.reversed()) }.take(nOffspring)
    }
}