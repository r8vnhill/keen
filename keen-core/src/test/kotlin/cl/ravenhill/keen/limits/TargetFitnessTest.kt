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
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
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
    }
})

private fun chromosome() = arbIntChromosome()
private fun genotype() = arbGenotype(chromosome())
private fun individual() = arbIndividual(genotype())
private fun population() = arbPopulation(individual(), 1..10)
private fun state() = arbEvolutionState(population(), arbRanker())
