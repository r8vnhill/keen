package cl.ravenhill.keen.util.listeners

import cl.ravenhill.keen.arbs.genetic.population
import cl.ravenhill.keen.arbs.genetic.nothingPopulation
import cl.ravenhill.keen.arbs.listeners.evolutionPlotter
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.util.listeners.records.GenerationRecord
import cl.ravenhill.keen.util.listeners.records.IndividualRecord
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.checkAll

class EvolutionPlotterTest : FreeSpec({

    "An [EvolutionPlotter]" - {
        "can add a new generation when the generation starts" {
            checkAll(
                Arb.evolutionPlotter<Nothing, NothingGene>(),
                Arb.nonNegativeInt(),
                Arb.nothingPopulation()
            ) { plotter, generation, population ->
                plotter.onGenerationStarted(population)
                plotter.currentGeneration shouldBe GenerationRecord(generation)
            }
        }

        "can update the result when a generation finishes" - {
            checkAll(
                Arb.evolutionPlotter<Int, IntGene>(),
                Arb.population(),
            ) { plotter, population ->
                plotter.onGenerationStarted(population)
                plotter.onGenerationFinished(population)
                val sorted = plotter.optimizer.sort(population)
                val resulting = List(sorted.size) {
                    IndividualRecord(sorted[it].genotype, sorted[it].fitness)
                }
                plotter.evolution.generations.last().population.resulting shouldBe resulting
            }
        }
    }
})
