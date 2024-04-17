package cl.ravenhill.keen.limits

import cl.ravenhill.keen.arb.arbRanker
import cl.ravenhill.keen.arb.evolution.arbEvolutionState
import cl.ravenhill.keen.arb.genetic.arbGenotype
import cl.ravenhill.keen.arb.genetic.arbIndividual
import cl.ravenhill.keen.arb.genetic.arbPopulation
import cl.ravenhill.keen.arb.genetic.chromosomes.arbIntChromosome
import cl.ravenhill.keen.genetic.fitness
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.flatMap
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll

class TargetFitnessTest : FreeSpec({
    "When constructing" - {
        "with a predicate that returns true" {
            checkAll(
                state().flatMap { s ->
                    Arb.double(-Double.MAX_VALUE..s.population.fitness.min(), false)
                        .map { s to it }
                }
            ) { (state, threshold) ->
                val targetFitness = TargetFitness<Int, IntGene> { it >= threshold }
                targetFitness(state).shouldBeTrue()
            }
        }

        "with a predicate that returns false" {
            checkAll(
                state().flatMap { s ->
                    Arb.double(s.population.fitness.max()..Double.MAX_VALUE, false)
                        .map { s to it }
                }
            ) { (state, threshold) ->
                val targetFitness = TargetFitness<Int, IntGene> { it > threshold }
                targetFitness(state).shouldBeFalse()
            }
        }

        "with a threshold returns true when the fitness is equal to the threshold" {
            checkAll(
                state()
            ) { state ->
                val threshold = state.population.fitness.random()
                val targetFitness = TargetFitness<Int, IntGene>(threshold)
                targetFitness(state).shouldBe(state.population.fitness.any { it == threshold })
            }
        }

        "with a threshold returns false when the fitness is not equal to the threshold" {
            checkAll(
                state().flatMap { s ->
                    Arb.double(-Double.MAX_VALUE..s.population.fitness.min(), false)
                        .filter { it !in s.population.fitness }
                        .filterNot { it == -0.0 }
                        .map { s to it }
                }
            ) { (state, threshold) ->
                val targetFitness = TargetFitness<Int, IntGene>(threshold)
                targetFitness(state).shouldBeFalse()
            }
        }
    }

    "The engine starts as null" {
        checkAll(arbTargetFitness()) { targetFitness ->
            targetFitness.engine.shouldBeNull()
        }
    }
})

private fun chromosome() = arbIntChromosome()
private fun genotype() = arbGenotype(chromosome())
private fun individual() = arbIndividual(genotype(), Arb.double(-1000.0..1000.0,  includeNonFiniteEdgeCases = false))
private fun population() = arbPopulation(individual(), 1..10)
private fun state() = arbEvolutionState(population(), arbRanker())
private fun arbTargetFitness() = arbitrary {
    val threshold = Arb.double().bind()
    TargetFitness<Int, IntGene> { it >= threshold }
}