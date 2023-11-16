package cl.ravenhill.keen.evolution

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.arbs.evolution.evolutionEngine
import cl.ravenhill.keen.arbs.evolution.fitnessFunction
import cl.ravenhill.keen.arbs.genetic.intGenotypeFactory
import cl.ravenhill.keen.arbs.limits.limit
import cl.ravenhill.keen.arbs.operators.intAlterer
import cl.ravenhill.keen.arbs.optimizer
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.operators.selector.TournamentSelector
import cl.ravenhill.keen.shouldHaveInfringement
import cl.ravenhill.keen.util.optimizer.FitnessMaximizer
import io.kotest.assertions.fail
import io.kotest.assertions.throwables.shouldThrowUnit
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.nonPositiveInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.checkAll

class EngineFactoryTest : FreeSpec({
    "An evolution Engine Factory" - {
        "should have a genotype factory property that" - {
            "returns the value provided to the constructor" {
                io.kotest.property.checkAll(
                    io.kotest.property.Arb.intGenotypeFactory(),
                    io.kotest.property.Arb.fitnessFunction()
                ) { factory, fitnessFunction ->
                    val engine = cl.ravenhill.keen.evolution.Engine.Factory(fitnessFunction, factory)
                    engine.genotypeFactory shouldBe factory
                }
            }
        }

        "should have a fitness function property that" - {
            "returns the value provided to the constructor" {
                io.kotest.property.checkAll(
                    io.kotest.property.Arb.intGenotypeFactory(),
                    io.kotest.property.Arb.fitnessFunction()
                ) { factory, fitnessFunction ->
                    val engine = cl.ravenhill.keen.evolution.Engine.Factory(fitnessFunction, factory)
                    engine.fitnessFunction shouldBe fitnessFunction
                }
            }
        }

        "should have a population size property that" - {
            "starts at 50" {
                checkAll(
                    Arb.evolutionEngine(
                        Arb.fitnessFunction(),
                        Arb.intGenotypeFactory(),
                        populationSize = null
                    )
                ) { engine ->
                    engine.populationSize shouldBe 50
                }
            }

            "can be set to a positive number" - {
                checkAll(
                    Arb.evolutionEngine(Arb.fitnessFunction(), Arb.intGenotypeFactory()),
                    Arb.positiveInt()
                ) { engine, size ->
                    engine.populationSize = size
                    engine.populationSize shouldBe size
                }
            }

            "cannot be set to a non-positive number" - {
                checkAll(
                    Arb.evolutionEngine(Arb.fitnessFunction(), Arb.intGenotypeFactory()),
                    Arb.nonPositiveInt()
                ) { engine, size ->
                    shouldThrowUnit<CompositeException> {
                        engine.populationSize = size
                    }.shouldHaveInfringement<IntConstraintException>("Population size [$size] must be greater than 0")
                }
            }
        }

        "should have a limits property that" - {
            "defaults to a list with a Generation Count of 100" {
                checkAll(
                    Arb.evolutionEngine(
                        Arb.fitnessFunction(),
                        Arb.intGenotypeFactory(),
                        limits = null
                    )
                ) { engine ->
                    engine.limits shouldBe listOf(GenerationCount(100))
                }
            }

            "can be set to a non-empty list of limits" {
                checkAll(
                    Arb.evolutionEngine(Arb.fitnessFunction(), Arb.intGenotypeFactory()),
                    Arb.list(Arb.limit<Int, IntGene>(), 1..3)
                ) { engine, limits ->
                    engine.limits = limits
                    engine.limits shouldBe limits
                }
            }

            "cannot be set to an empty list" {
                io.kotest.property.checkAll(
                    io.kotest.property.Arb.evolutionEngine(
                        io.kotest.property.Arb.fitnessFunction(),
                        io.kotest.property.Arb.intGenotypeFactory()
                    )
                ) { engine ->
                    io.kotest.assertions.throwables.shouldThrowUnit<cl.ravenhill.jakt.exceptions.CompositeException> {
                        engine.limits = kotlin.collections.emptyList()
                    }
                        .shouldHaveInfringement<cl.ravenhill.jakt.exceptions.CollectionConstraintException>("Limits cannot be empty")
                }
            }
        }

        "should have an optimizer property that" - {
            "defaults to a Fitness Maximizer" {
                checkAll(
                    Arb.evolutionEngine(
                        Arb.fitnessFunction(),
                        Arb.intGenotypeFactory(),
                        optimizer = null
                    )
                ) { engine ->
                    engine.optimizer shouldBe FitnessMaximizer()
                }
            }

            "can be set to a different optimizer" {
                io.kotest.property.checkAll(
                    io.kotest.property.Arb.evolutionEngine(
                        io.kotest.property.Arb.fitnessFunction(),
                        io.kotest.property.Arb.intGenotypeFactory()
                    ),
                    io.kotest.property.Arb.optimizer<kotlin.Int, cl.ravenhill.keen.genetic.genes.numerical.IntGene>()
                ) { engine, optimizer ->
                    engine.optimizer = optimizer
                    engine.optimizer shouldBe optimizer
                }
            }
        }

        "should have an alterers list property that" - {
            "defaults to an empty list" {
                checkAll(
                    Arb.evolutionEngine(
                        Arb.fitnessFunction(),
                        Arb.intGenotypeFactory(),
                        alterers = null
                    )
                ) { engine ->
                    engine.alterers shouldBe emptyList()
                }
            }

            "can be set to a list" {
                io.kotest.property.checkAll(
                    io.kotest.property.Arb.evolutionEngine(
                        io.kotest.property.Arb.fitnessFunction(),
                        io.kotest.property.Arb.intGenotypeFactory()
                    ),
                    io.kotest.property.Arb.list(io.kotest.property.Arb.intAlterer(), 1..3)
                ) { engine, alterers ->
                    engine.alterers = alterers
                    engine.alterers shouldBe alterers
                }
            }
        }

        "should have a selector property that" - {
            "default to a tournament selection with sample size of 3" - {
                checkAll(
                    Arb.evolutionEngine(
                        Arb.fitnessFunction(),
                        Arb.intGenotypeFactory(),
                        selectors = Arb.constant(null to null)
                    )
                ) { engine ->
                    engine.selector shouldBe TournamentSelector(3)
                }
            }

            "when set should assign both the survivor and offspring selectors" - {
                fail("TODO")
            }
        }
    }
})