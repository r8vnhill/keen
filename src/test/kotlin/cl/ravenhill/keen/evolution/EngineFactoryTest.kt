package cl.ravenhill.keen.evolution

import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.DoubleConstraintException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.arbs.datatypes.mutableList
import cl.ravenhill.keen.arbs.datatypes.probability
import cl.ravenhill.keen.arbs.evolution.evolutionEngineFactory
import cl.ravenhill.keen.arbs.evolution.fitnessFunction
import cl.ravenhill.keen.arbs.genetic.intGenotypeFactory
import cl.ravenhill.keen.arbs.limits.limit
import cl.ravenhill.keen.arbs.listeners.evolutionListener
import cl.ravenhill.keen.arbs.operators.intAlterer
import cl.ravenhill.keen.arbs.operators.selector
import cl.ravenhill.keen.arbs.optimizer
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.limits.GenerationCount
import cl.ravenhill.keen.operators.CompositeAlterer
import cl.ravenhill.keen.operators.selector.TournamentSelector
import cl.ravenhill.keen.shouldHaveInfringement
import cl.ravenhill.keen.shouldNotBeInRange
import cl.ravenhill.keen.util.optimizer.FitnessMaximizer
import io.kotest.assertions.throwables.shouldThrowUnit
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.assume
import io.kotest.property.checkAll

class EngineFactoryTest : FreeSpec({

    "An evolution Engine Factory" - {
        "should have a genotype factory property that" - {
            "returns the value provided to the constructor" {
                checkAll(
                    Arb.intGenotypeFactory(), Arb.fitnessFunction()
                ) { factory, fitnessFunction ->
                    val engine = EvolutionEngine.Factory(fitnessFunction, factory)
                    engine.genotypeFactory shouldBe factory
                }
            }
        }

        "should have a fitness function property that" - {
            "returns the value provided to the constructor" {
                checkAll(
                    Arb.intGenotypeFactory(), Arb.fitnessFunction()
                ) { factory, fitnessFunction ->
                    val engine = EvolutionEngine.Factory(fitnessFunction, factory)
                    engine.fitnessFunction shouldBe fitnessFunction
                }
            }
        }

        "should have a population size property that" - {
            "starts at 50" {
                checkAll(
                    Arb.evolutionEngineFactory(
                        Arb.fitnessFunction(), Arb.intGenotypeFactory(), populationSize = null
                    )
                ) { engine ->
                    engine.populationSize shouldBe 50
                }
            }

            "can be set to a positive number" - {
                checkAll(
                    Arb.evolutionEngineFactory(Arb.fitnessFunction(), Arb.intGenotypeFactory()), Arb.positiveInt()
                ) { engine, size ->
                    engine.populationSize = size
                    engine.populationSize shouldBe size
                }
            }

            "cannot be set to a non-positive number" - {
                checkAll(
                    Arb.evolutionEngineFactory(Arb.fitnessFunction(), Arb.intGenotypeFactory()), Arb.nonPositiveInt()
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
                    Arb.evolutionEngineFactory(
                        Arb.fitnessFunction(), Arb.intGenotypeFactory(), limits = null
                    )
                ) { engine ->
                    engine.limits shouldBe listOf(GenerationCount(100))
                }
            }

            "can be set to a non-empty list of limits" {
                checkAll(
                    Arb.evolutionEngineFactory(Arb.fitnessFunction(), Arb.intGenotypeFactory()),
                    Arb.list(Arb.limit<Int, IntGene>(), 1..3)
                ) { engine, limits ->
                    engine.limits = limits
                    engine.limits shouldBe limits
                }
            }

            "cannot be set to an empty list" {
                checkAll(
                    Arb.evolutionEngineFactory(
                        Arb.fitnessFunction(), Arb.intGenotypeFactory()
                    )
                ) { engine ->
                    shouldThrowUnit<CompositeException> {
                        engine.limits = emptyList()
                    }.shouldHaveInfringement<CollectionConstraintException>("Limits cannot be empty")
                }
            }
        }

        "should have an optimizer property that" - {
            "defaults to a Fitness Maximizer" {
                checkAll(
                    Arb.evolutionEngineFactory(
                        Arb.fitnessFunction(), Arb.intGenotypeFactory(), optimizer = null
                    )
                ) { engine ->
                    engine.optimizer shouldBe FitnessMaximizer()
                }
            }

            "can be set to a different optimizer" {
                checkAll(
                    Arb.evolutionEngineFactory(
                        Arb.fitnessFunction(), Arb.intGenotypeFactory()
                    ), Arb.optimizer<Int, IntGene>()
                ) { engine, optimizer ->
                    engine.optimizer = optimizer
                    engine.optimizer shouldBe optimizer
                }
            }
        }

        "should have an alterers list property that" - {
            "defaults to an empty list" {
                checkAll(
                    Arb.evolutionEngineFactory(
                        Arb.fitnessFunction(), Arb.intGenotypeFactory(), alterers = null
                    )
                ) { engine ->
                    engine.alterers shouldBe emptyList()
                }
            }

            "can be set to a list" {
                checkAll(
                    Arb.evolutionEngineFactory(
                        Arb.fitnessFunction(), Arb.intGenotypeFactory()
                    ), Arb.list(Arb.intAlterer(), 1..3)
                ) { engine, alterers ->
                    engine.alterers = alterers
                    engine.alterers shouldBe alterers
                }
            }
        }

        "should have a selector property that" - {
            "default to a tournament selection with sample size of 3" - {
                checkAll(
                    Arb.evolutionEngineFactory(
                        Arb.fitnessFunction(), Arb.intGenotypeFactory(), selectors = Arb.constant(null to null)
                    )
                ) { engine ->
                    engine.selector shouldBe TournamentSelector(3)
                }
            }

            "when set should assign both the survivor and offspring selectors" - {
                checkAll(
                    Arb.evolutionEngineFactory(
                        Arb.fitnessFunction(),
                        Arb.intGenotypeFactory(),
                    ),
                    Arb.selector<Int, IntGene>(),
                ) { engine, selector ->
                    engine.selector = selector
                    engine.selector shouldBe selector
                    engine.offspringSelector shouldBe selector
                    engine.survivorSelector shouldBe selector
                }
            }
        }

        "should have a survivor selector property that" - {
            "defaults to a Tournament Selector with sample size of 3" {
                checkAll(
                    Arb.evolutionEngineFactory(
                        Arb.fitnessFunction(),
                        Arb.intGenotypeFactory(),
                        selectors = Arb.pair(Arb.constant(null), Arb.constant(null))
                    )
                ) { engine ->
                    engine.survivorSelector shouldBe TournamentSelector(3)
                }
            }

            "can be set to another selector" {
                checkAll(
                    Arb.evolutionEngineFactory(
                        Arb.fitnessFunction(),
                        Arb.intGenotypeFactory(),
                    ), Arb.selector<Int, IntGene>()
                ) { engine, selector ->
                    engine.survivorSelector = selector
                    engine.survivorSelector shouldBe selector
                }
            }
        }

        "should have an offspring selector that" - {
            "defaults to a Tournament Selector with a sample size of 3" {
                checkAll(
                    Arb.evolutionEngineFactory(
                        Arb.fitnessFunction(),
                        Arb.intGenotypeFactory(),
                        selectors = Arb.pair(Arb.constant(null), Arb.constant(null))
                    )
                ) { engine ->
                    engine.offspringSelector shouldBe TournamentSelector(3)
                }
            }

            "can be set to another selector" {
                checkAll(
                    Arb.evolutionEngineFactory(
                        Arb.fitnessFunction(),
                        Arb.intGenotypeFactory(),
                    ), Arb.selector<Int, IntGene>()
                ) { engine, selector ->
                    engine.offspringSelector = selector
                    engine.offspringSelector shouldBe selector
                }
            }
        }

        "should have a survival rate property that" - {
            "defaults to 0.4" - {
                checkAll(
                    Arb.evolutionEngineFactory(
                        Arb.fitnessFunction(),
                        Arb.intGenotypeFactory(), survivalRate = null
                    )
                ) { engine ->
                    engine.survivalRate shouldBe 0.4
                }
            }

            "can be set to a value between 0 and 1" - {
                checkAll(
                    Arb.evolutionEngineFactory(
                        Arb.fitnessFunction(),
                        Arb.intGenotypeFactory(),
                    ), Arb.probability()
                ) { engine, rate ->
                    engine.survivalRate = rate
                    engine.survivalRate shouldBe rate
                }
            }

            "cannot be set to a value outside [0, 1]" {
                checkAll(
                    Arb.evolutionEngineFactory(
                        Arb.fitnessFunction(),
                        Arb.intGenotypeFactory(),
                    ), Arb.double()
                ) { engine, rate ->
                    assume {
                        rate shouldNotBeInRange 0.0..1.0
                    }
                    shouldThrowUnit<CompositeException> {
                        engine.survivalRate = rate
                    }.shouldHaveInfringement<DoubleConstraintException>(
                        "Survival rate [$rate] must be a valid probability"
                    )
                }
            }
        }

        "should have a listeners property that" - {
            "defaults to an empty list" {
                checkAll(
                    Arb.evolutionEngineFactory(
                        Arb.fitnessFunction(),
                        Arb.intGenotypeFactory(),
                        listeners = null
                    )
                ) {
                    it.listeners.shouldBeEmpty()
                }
            }

            "can be set to a list of listeners" {

                checkAll(
                    Arb.evolutionEngineFactory(
                        Arb.fitnessFunction(),
                        Arb.intGenotypeFactory()
                    ), Arb.mutableList(Arb.evolutionListener<Int, IntGene>(), 1..3)
                ) { engine, listeners ->
                    engine.listeners = listeners
                    engine.listeners shouldBe listeners
                }
            }
        }

        "should be able to create an evolution Engine" {
            checkAll(
                Arb.evolutionEngineFactory(
                    Arb.fitnessFunction(),
                    Arb.intGenotypeFactory()
                )
            ) { factory ->
                val engine = factory.make()
                with(engine) {
                    genotypeFactory shouldBe factory.genotypeFactory
                    populationSize shouldBe factory.populationSize
                    survivalRate shouldBe factory.survivalRate
                    offspringSelector shouldBe factory.offspringSelector
                    alterer shouldBe CompositeAlterer(factory.alterers)
                    limits shouldBe factory.limits
                    survivorSelector shouldBe factory.survivorSelector
                    optimizer shouldBe factory.optimizer
                    listeners shouldBe factory.listeners
                    interceptor shouldBe factory.interceptor
                }
            }
        }
    }
})