/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.repr.arbSimpleFeature
import cl.ravenhill.keen.repr.arbSimpleRepresentation
import cl.ravenhill.keen.utils.arbNonNaNDouble
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll

class PopulationTest : FreeSpec({
    "A Population of Individuals" - {
        "should have a fitness property that is equal to the list of fitness values of the individuals" {
            checkAll(
                arbPopulationAndFitness(arbIndividual(arbSimpleRepresentation(arbSimpleFeature(Arb.double()))))
            ) { (population, fitness) ->
                val populationFitness = population.fitness
                populationFitness shouldBe fitness
            }
        }
    }
})

private fun <T, F, R> arbPopulationAndFitness(
    individual: Arb<Individual<T, F, R>>
): Arb<Pair<List<Individual<T, F, R>>, List<Double>>> where F : Feature<T, F>, R : Representation<T, F> = arbitrary {
    val size = Arb.int(0..100).bind()
    val fitness = mutableListOf<Double>()
    val individuals = mutableListOf<Individual<T, F, R>>()
    repeat(size) {
        val boundIndividual = individual.bind()
        individuals.add(boundIndividual)
        fitness.add(boundIndividual.fitness)
    }
    individuals to fitness
}

fun <T, F, R> arbPopulation(individual: Arb<Individual<T, F, R>>): Arb<Population<T, F, R>>
        where F : Feature<T, F>, R : Representation<T, F> = Arb.list(individual)
