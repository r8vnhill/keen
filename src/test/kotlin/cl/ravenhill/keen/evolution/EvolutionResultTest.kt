/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution

import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.arbs.genetic.population
import cl.ravenhill.keen.arbs.optimizer
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.shouldHaveInfringement
import cl.ravenhill.keen.util.optimizer.FitnessMaximizer
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.checkAll

class EvolutionResultTest : FreeSpec({

    "An [EvolutionResult]" - {
        "can be created with an optimizer, a population, and a generation" {
            checkAll(
                Arb.optimizer<Int, IntGene>(),
                Arb.population(),
                Arb.nonNegativeInt()
            ) { optimizer, population, generation ->
                val result = EvolutionResult(optimizer, population, generation)
                result.optimizer shouldBe optimizer
                result.population shouldBe population
                result.generation shouldBe generation
            }
        }

        "when getting the best individual" - {
            "returns the best individual in the population" {
                checkAll(
                    Arb.population(size = 1..50),
                    Arb.nonNegativeInt()
                ) { population, generation ->
                    val result = EvolutionResult(FitnessMaximizer(), population, generation)
                    result.best shouldBe population.maxByOrNull { it.fitness }
                }
            }

            "throw an exception if the population is empty" {
                checkAll(
                    Arb.optimizer<Int, IntGene>(),
                    Arb.nonNegativeInt()
                ) { optimizer, generation ->
                    val result = EvolutionResult(optimizer, emptyList(), generation)
                    shouldThrow<CompositeException> {
                        result.best
                    }.shouldHaveInfringement<CollectionConstraintException>(
                        "Cannot get the best individual of an empty population"
                    )
                }
            }
        }

        "can map the population to a new population" {
            checkAll(
                Arb.optimizer<Int, IntGene>(),
                Arb.population(),
                Arb.nonNegativeInt()
            ) { optimizer, population, generation ->
                val result = EvolutionResult(optimizer, population, generation)
                val newPopulation = result.map { it.copy(fitness = 0.0) }
                newPopulation.population shouldBe population.map { it.copy(fitness = 0.0) }
            }
        }
    }
})
