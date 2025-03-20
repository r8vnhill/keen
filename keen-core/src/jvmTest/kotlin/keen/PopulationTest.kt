/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package keen

import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.arbSimpleFeature
import cl.ravenhill.arbSimpleRepresentation
import cl.ravenhill.utils.arbIndividual
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
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
    population.toPopulation() to fitnessValues
}
