package cl.ravenhill.keen.util.listeners

import cl.ravenhill.keen.arbs.genetic.intPopulation
import cl.ravenhill.keen.arbs.listeners.evolutionSummary
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.util.listeners.records.GenerationRecord
import cl.ravenhill.keen.util.listeners.records.IndividualRecord
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.checkAll
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class EvolutionSummaryTest : FreeSpec({

    "An Evolution Summary listener" - {
        "should assign the current generation on generation started" {
            checkAll(
                Arb.evolutionSummary<Int, IntGene>(),
                Arb.nonNegativeInt(),
                Arb.intPopulation()
            ) { listener, generation, population ->
                listener.onGenerationStarted(generation, population)
                listener.currentGeneration shouldBe GenerationRecord(generation).apply {
                    this.population.initial = List(population.size) {
                        IndividualRecord("${population[it].genotype}", population[it].fitness)
                    }
                }
            }
        }
    }
})
