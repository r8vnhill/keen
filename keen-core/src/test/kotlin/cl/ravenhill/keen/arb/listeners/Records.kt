/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb.listeners

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.records.EvolutionRecord
import cl.ravenhill.keen.listeners.records.GenerationRecord
import cl.ravenhill.keen.listeners.records.IndividualRecord
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.nonNegativeInt
import kotlin.time.TestTimeSource
import kotlin.time.TimeMark

/**
 * Generates an arbitrary [IndividualRecord] for property-based testing.
 *
 * This function creates instances of [IndividualRecord] with a configurable genotype and fitness value, making it ideal
 * for testing scenarios that involve individuals in evolutionary algorithms. The genotype and fitness value are
 * determined by the provided arbitraries, allowing for extensive customization.
 *
 * ## Usage:
 * This arbitrary generator can be used in property-based testing frameworks like Kotest to create diverse instances of
 * [IndividualRecord], facilitating comprehensive testing across a range of genetic configurations and fitness values.
 *
 * ### Example:
 * Generating an [IndividualRecord] with a specific genotype and fitness range:
 * ```kotlin
 * val individualRecordArb = Arb.individualRecord(
 *     genotype = Arb.someGenotypeArb(), // An arbitrary generator for genotypes
 *     fitness = Arb.double(0.0..100.0) // Fitness values from 0.0 to 100.0
 * )
 * val individualRecord = individualRecordArb.bind()
 * // Resulting individualRecord will have a genotype from someGenotypeArb and fitness between 0.0 and 100.0
 * ```
 *
 * This function is particularly useful in scenarios where individual records of varying genotypes and fitness
 * values are needed to robustly test evolutionary algorithms and related functionalities.
 *
 * @param T The type representing the genetic data or information.
 * @param G The type of [Gene] associated with the genetic data.
 * @param genotype An [Arb]<[Genotype]<[T], [G]>> that generates genotypes for the individual records.
 * @param fitness An optional [Arb]<[Double]> that generates fitness values for the individual records.
 *   Defaults to generating any double value that is not NaN (Not a Number).
 *
 * @return An [Arb] that generates instances of [IndividualRecord] with specified genotypes and fitness values.
 */
fun <T, G> Arb.Companion.individualRecord(
    genotype: Arb<Genotype<T, G>>,
    fitness: Arb<Double> = Arb.double().filterNot { it.isNaN() },
) where G : Gene<T, G> =
    arbitrary {
        IndividualRecord(genotype.bind(), fitness.bind())
    }

fun <T, G> Arb.Companion.populationRecord(
    individual: Arb<IndividualRecord<T, G>>,
    size: Arb<Int> = int(1..50),
    parents: Arb<List<IndividualRecord<T, G>>>? = size.map {
        list(individual, it..it).next()
    },
    offspring: Arb<List<IndividualRecord<T, G>>>? = size.map {
        list(individual, it..it).next()
    },
) where G : Gene<T, G> = arbitrary {
    GenerationRecord.PopulationRecord(
        parents = parents?.bind() ?: emptyList(),
        offspring = offspring?.bind() ?: emptyList(),
    )
}

/**
 * Generates an arbitrary [GenerationRecord] for property-based testing in genetic algorithms.
 *
 * This function creates instances of [GenerationRecord], representing a record of a specific generation within an
 * evolutionary algorithm. Each [GenerationRecord] includes various details such as the generation number, the
 * population of offspring, and timing information. The generation number, population size, and the individuals in the
 * population are determined by the provided arbitraries, allowing for flexibility and customization.
 *
 * ## Usage:
 * This arbitrary generator can be used in property-based testing frameworks like Kotest to simulate and test
 * different scenarios within a generation of an evolutionary algorithm.
 *
 * ### Example:
 * Generating a [GenerationRecord] with specific parameters:
 * ```kotlin
 * val generationRecordArb = Arb.generationRecord(
 *     individual = Arb.individualRecord(/* ... */), // An arbitrary generator for individual records
 *     generation = Arb.nonNegativeInt(), // Non-negative integers for generation numbers
 *     size = Arb.int(1..50) // Population size from 1 to 50
 * )
 * val generationRecord = generationRecordArb.bind()
 * // Resulting generationRecord will have a specific generation number and a population of offspring
 * ```
 *
 * This function is particularly useful in scenarios where detailed records of different generations in an
 * evolutionary algorithm need to be tested or analyzed. It allows for the generation of comprehensive records
 * that encapsulate key aspects of a generation's state.
 *
 * @param T The type representing the genetic data or information.
 * @param G The type of [Gene] associated with the genetic data.
 * @param individual An [Arb]<[IndividualRecord]<[T], [G]>> for generating individual records in the population.
 * @param generation An optional [Arb]<[Int]> for generating the generation number. Defaults to non-negative integers.
 * @param size An optional [Arb]<[Int]> for specifying the population size. Defaults to a range of 1 to 50.
 *
 * @return An [Arb] that generates instances of [GenerationRecord] with specified parameters for generation number and
 *         population.
 */
fun <T, G> Arb.Companion.generationRecord(
    generation: Arb<Int> = nonNegativeInt(),
    population: Arb<GenerationRecord.PopulationRecord<T, G>>? = null,
    startTime: TimeMark? = TestTimeSource().markNow()
) where G : Gene<T, G> = arbitrary {
    GenerationRecord<T, G>(generation.bind()).apply {
        startTime?.let { this.startTime = it }
        population?.let {
            val populationRecord = it.bind()
            this.population.parents = populationRecord.parents
            this.population.offspring = populationRecord.offspring
        }
    }
}

/**
 * Generates an arbitrary [EvolutionRecord] for property-based testing in genetic algorithms.
 *
 * This function creates instances of [EvolutionRecord], which represent the entire record of an evolutionary
 * process, including records of each generation. It allows for the customization of generation records and the
 * starting time, providing a comprehensive view of the evolutionary algorithm's progress and states.
 *
 * @param T The type representing the genetic data or information.
 * @param G The type of [Gene] associated with the genetic data.
 * @param generation An optional [Arb] that generates [GenerationRecord] instances. If not provided, no generation
 *                   records are added initially to the evolution record.
 * @param startTime An optional [TimeMark] representing the start time of the evolution process. Defaults to the
 *                  current time from a [TestTimeSource].
 *
 * @return An [Arb] that generates [EvolutionRecord] instances with specified generation records and starting time.
 */
fun <T, G> arbEvolutionRecord(
    generation: Arb<GenerationRecord<T, G>>? = null,
    startTime: TimeMark? = TestTimeSource().markNow()
) where G : Gene<T, G> = arbitrary {
    val evolutionRecord = EvolutionRecord<T, G>()
    startTime?.let { evolutionRecord.startTime = it }
    generation?.let { evolutionRecord.generations.add(it.bind()) }
    evolutionRecord
}
