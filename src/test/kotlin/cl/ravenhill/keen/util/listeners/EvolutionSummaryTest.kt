package cl.ravenhill.keen.util.listeners

import cl.ravenhill.keen.arbs.genetic.intPopulation
import cl.ravenhill.keen.arbs.listeners.evolutionSummary
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.util.isNotNan
import cl.ravenhill.keen.util.listeners.records.GenerationRecord
import cl.ravenhill.keen.util.listeners.records.IndividualRecord
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.assume
import io.kotest.property.checkAll
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class EvolutionSummaryTest : FreeSpec({

    "An Evolution Summary listener" - {
        "on generation started" - {
            "should assign the current generation" {
                checkAll(
                    Arb.evolutionSummary<Int, IntGene>(),
                    Arb.nonNegativeInt(),
                    Arb.intPopulation()
                ) { listener, generation, population ->
                    listener.onGenerationStarted(generation, population)
                    listener.currentGeneration shouldBe GenerationRecord<Int, IntGene>(1).apply {
                        this.population.initial = List(population.size) {
                            IndividualRecord(population[it].genotype, population[it].fitness)
                        }
                    }
                }
            }
        }

        "on generation finished" - {
            "should update the resulting population" {
                checkAll(
                    Arb.evolutionSummary<Int, IntGene>(),
                    Arb.intPopulation(),
                    Arb.intPopulation()
                ) { listener, initialPopulation, resultingPopulation ->
                    listener.onGenerationStarted(0, initialPopulation)
                    listener.onGenerationFinished(resultingPopulation)
                    listener.currentGeneration.population.resulting = List(resultingPopulation.size) {
                        IndividualRecord(resultingPopulation[it].genotype, resultingPopulation[it].fitness)
                    }
                }
            }

            "should compute the steady generations" {
                checkAll(
                    Arb.evolutionSummary<Int, IntGene>(),
                    Arb.nonNegativeInt(),
                    Arb.intPopulation()
                ) { listener, generation, resultingPopulation ->
                    assume {
                        resultingPopulation.any { it.fitness.isNotNan() }.shouldBeTrue()
                    }
                    listener.onGenerationStarted(generation, resultingPopulation)
                    listener.onGenerationFinished(resultingPopulation)
                    listener.onGenerationStarted(generation, resultingPopulation)
                    listener.onGenerationFinished(resultingPopulation)
                    listener.evolution.generations.last().steady shouldBe 1
                }
            }
        }
    }
})
