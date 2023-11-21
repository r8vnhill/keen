package cl.ravenhill.keen.util.listeners

import cl.ravenhill.keen.arbs.datatypes.mutableList
import cl.ravenhill.keen.arbs.datatypes.orderedPair
import cl.ravenhill.keen.arbs.genetic.population
import cl.ravenhill.keen.arbs.optimizer
import cl.ravenhill.keen.arbs.records.generationRecord
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.util.isNotNan
import cl.ravenhill.keen.util.listeners.records.EvolutionRecord
import cl.ravenhill.keen.util.listeners.records.IndividualRecord
import cl.ravenhill.keen.util.optimizer.FitnessMaximizer
import cl.ravenhill.keen.util.optimizer.IndividualOptimizer
import com.github.stefanbirkner.systemlambda.SystemLambda
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.assume
import io.kotest.property.checkAll
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource

@OptIn(ExperimentalTime::class, ExperimentalKotest::class)
class EvolutionListenerTest : FreeSpec({
    "An [EvolutionLister]" - {
        "should do nothing for all notifications" {
            checkAll(
                PropTestConfig(iterations = 25),
                Arb.optimizer<Int, IntGene>(),
                Arb.orderedPair(Arb.positiveInt(), Arb.positiveInt()),
                Arb.mutableList(Arb.generationRecord(), 1..25)
            ) { optimizer, (_, generation), generationRecords ->
                with(
                    object : EvolutionListener<Int, IntGene> {
                        override var optimizer: IndividualOptimizer<Int, IntGene> = optimizer
                        override var generation = generation
                        override val currentGeneration = generationRecords.first()
                        override var evolution = EvolutionRecord(
                            generationRecords
                        )

                        @ExperimentalTime
                        override var timeSource: TimeSource = TimeSource.Monotonic
                    }
                ) {
                    onEvolutionEnded() shouldBe Unit
                    onGenerationStarted(emptyList()) shouldBe Unit
                    onGenerationFinished(emptyList()) shouldBe Unit
                    onInitializationStarted() shouldBe Unit
                    onInitializationFinished() shouldBe Unit
                    onEvaluationStarted() shouldBe Unit
                    onEvaluationFinished() shouldBe Unit
                    onOffspringSelectionStarted() shouldBe Unit
                    onOffspringSelectionFinished() shouldBe Unit
                    onSurvivorSelectionStarted() shouldBe Unit
                    onSurvivorSelectionFinished() shouldBe Unit
                    onAlterationStarted() shouldBe Unit
                    onAlterationFinished() shouldBe Unit
                    onEvolutionStart() shouldBe Unit
                    onEvolutionFinished() shouldBe Unit
                }
            }
        }

        "should be able to compute the steady generations when" - {
            "the last generation have different fitness than the current" {
                checkAll(Arb.generationRecord(), Arb.generationRecord()) { last, current ->
                    assume {
                        last.population.resulting.first() shouldNotBe current.population.resulting.first()
                    }

                    EvolutionListener.computeSteadyGenerations(last, current) shouldBe 0
                }
            }

            "the last generation have the same fitness than the current" {
                checkAll(Arb.generationRecord()) { last ->
                    assume { last.population.resulting.first().fitness.isNotNan() }
                    EvolutionListener.computeSteadyGenerations(
                        last,
                        last.copy(generation = last.generation).apply {
                            population.resulting = last.population.resulting
                        }
                    ) shouldBe last.steady + 1
                }
            }
        }

        "should be able to compute a population when" - {
            "the population is empty" {
                checkAll(
                    Arb.optimizer<Nothing, NothingGene>()
                ) { optimizer ->
                    EvolutionListener.computePopulation(optimizer, emptyList()) shouldBe emptyList()
                }
            }

            "the population is not empty" {
                checkAll(
                    Arb.optimizer<Int, IntGene>(),
                    Arb.population()
                ) { optimizer, population ->
                    val sorted = optimizer.sort(population)
                    EvolutionListener.computePopulation(optimizer, population) shouldBe List(
                        population.size
                    ) { i ->
                        IndividualRecord(
                            sorted[i].genotype,
                            sorted[i].fitness
                        )
                    }
                }
            }
        }
    }

    "An [AbstractEvolutionListener]" - {
        "when created" - {
            "should start with the correct parameters" {
                with(object : AbstractEvolutionListener<Nothing, NothingGene>() {}) {
                    optimizer.shouldBeInstanceOf<FitnessMaximizer<*, *>>()
                    generation shouldBe 0
                    evolution shouldBe EvolutionRecord()
                }
            }
        }

        "can be displayed" {
            with(object : AbstractEvolutionListener<Nothing, NothingGene>() {}) {
                SystemLambda.tapSystemOut {
                    display()
                } shouldBe "${toString()}\r\n"
            }
        }
    }
})
