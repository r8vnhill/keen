/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.repr.arbSimpleFeature
import cl.ravenhill.keen.repr.arbSimpleRepresentation
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import io.kotest.property.checkAll

class PopulationTest : FreeSpec({
    "A Population" - {
        "can list the fitness values of its individuals" {
            checkAll(
                arbPopulationAndFitnessValues(arbIndividual(arbSimpleRepresentation(arbSimpleFeature())))
            ) { (population, fitnessValues) ->
                population.fitness shouldBe fitnessValues
            }
        }
    }
})

/**
 * Generates an arbitrary population of individuals for evolutionary algorithms.
 *
 * @param T The type of value held by the features within the individuals.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 * @param arbIndividual An [Arb] generator for `Individual` instances, defining the characteristics of each individual
 *   in the population.
 * @param size An optional [IntRange] specifying the range for the population size. Defaults to 0..100.
 * @return An [Arb] generator that produces a list of `Individual<T, F, R>` instances, representing a population.
 */
fun <T, F, R> arbPopulation(
    arbIndividual: Arb<Individual<T, F, R>>,
    size: IntRange = 0..100
) where F : Feature<T, F>, R : Representation<T, F> = Arb.list(arbIndividual, size)

/**
 * Generates an arbitrary population of individuals along with their corresponding fitness values.
 *
 * @param arbIndividual An `Arb<Individual<T, F, R>>` generator for creating the individuals.
 * @param arbSize An optional `Arb<Int>` generator for determining the size of the population. Default is `Arb.int(0..100)`.
 * @return A pair consisting of a list of individuals and their corresponding fitness values.
 */
private fun <T, F, R> arbPopulationAndFitnessValues(
    arbIndividual: Arb<Individual<T, F, R>>,
    arbSize: Arb<Int> = Arb.int(0..100)
) where F : Feature<T, F>, R : Representation<T, F> = arbSize.map { size ->
    val fitnessValues = mutableListOf<Double>()
    val population = mutableListOf<Individual<T, F, R>>()
    repeat(size) {
        val individual = arbIndividual.next()
        fitnessValues.add(individual.fitness)
        population.add(individual)
    }
    population to fitnessValues
}
