/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.util.listeners

import cl.ravenhill.keen.arbs.genetic.nothingPopulation
import cl.ravenhill.keen.arbs.listeners.evolutionPrinter
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.util.listeners.records.GenerationRecord
import com.github.stefanbirkner.systemlambda.SystemLambda
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeBlank
import io.kotest.property.Arb
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.checkAll
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class EvolutionPrinterTest : FreeSpec({

    "An [EvolutionPrinter]" - {
        "on generation started" - {
            "should create a new generation record" {
                checkAll(
                    Arb.evolutionPrinter<Nothing, NothingGene>(),
                    Arb.nonNegativeInt(),
                    Arb.nothingPopulation()
                ) { printer, generation, population ->
                    printer.onGenerationStarted(population)
                    printer.currentGeneration.generation shouldBe
                          GenerationRecord<Nothing, NothingGene>(generation).generation
                }
            }
        }

        "on generation finished" - {
            "should add the current generation to the list of processed generations" {
                checkAll(
                    Arb.evolutionPrinter<Nothing, NothingGene>(),
                    Arb.nonNegativeInt(),
                    Arb.nothingPopulation()
                ) { printer, generation, population ->
                    SystemLambda.tapSystemOut {
                        printer.onGenerationStarted(population)
                        printer.onGenerationFinished(population)
                        printer.evolution.generations.last().generation shouldBe generation
                    }
                }
            }

            "print the results of the current generation if the generation number is a multiple of the interval" {
                checkAll(
                    Arb.evolutionPrinter<Nothing, NothingGene>(),
                    Arb.nonNegativeInt(),
                    Arb.nothingPopulation()
                ) { printer, generation, population ->
                    if (generation % printer.every == 0) {
                        SystemLambda.tapSystemOut {
                            printer.onGenerationStarted(population)
                            printer.onGenerationFinished(population)
                        }.trim() shouldBe printer.toString().trim()
                    } else {
                        SystemLambda.tapSystemOut {
                            printer.onGenerationStarted(population)
                            printer.onGenerationFinished(population)
                        }.shouldBeBlank()
                    }
                }
            }
        }
    }
})
