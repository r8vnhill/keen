package cl.ravenhill.keen.evolution

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.arb.genetic.chromosomes.arbDoubleChromosomeFactory
import cl.ravenhill.keen.arb.genetic.arbGenotypeFactory
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.evolution.engines.GeneticAlgorithm
import cl.ravenhill.keen.exceptions.EngineException
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.checkAll

class EvolutionEngineFactoryTest : FreeSpec({
    "Can be created with" - {
        "a fitness function and a genotype factory" {
            checkAll(arbGenotypeFactory(Arb.list(arbDoubleChromosomeFactory()))) { genotypeFactory ->
                val fitnessFunction = { _: Genotype<Double, DoubleGene> -> 0.0 }
                with(GeneticAlgorithm.Factory(fitnessFunction, genotypeFactory)) {
                    this.genotypeFactory shouldBe genotypeFactory
                    this.fitnessFunction shouldBe fitnessFunction
                    populationSize shouldBe GeneticAlgorithm.Factory.DEFAULT_POPULATION_SIZE
                    survivalRate shouldBe GeneticAlgorithm.Factory.DEFAULT_SURVIVAL_RATE
                    parentSelector shouldBe GeneticAlgorithm.Factory.defaultParentSelector()
                    survivorSelector shouldBe GeneticAlgorithm.Factory.defaultSurvivorSelector()
                    alterers shouldBe GeneticAlgorithm.Factory.defaultAlterers()
                    limits shouldBe GeneticAlgorithm.Factory.defaultLimits()
                    ranker shouldBe GeneticAlgorithm.Factory.defaultRanker()
                    listeners shouldBe GeneticAlgorithm.Factory.defaultListeners()
                    evaluator shouldBe GeneticAlgorithm.Factory.defaultEvaluator()
                    interceptor shouldBe GeneticAlgorithm.Factory.defaultInterceptor()
                }
            }
        }
    }

    "Setting the population size" - {
        "to a positive value mutates the property" {
            val arbGenotypeFactory = arbGenotypeFactory(Arb.list(arbDoubleChromosomeFactory()))
            checkAll(Arb.positiveInt(), arbGenotypeFactory) { size, genotypeFactory ->
                GeneticAlgorithm.Factory({ _: Genotype<Double, DoubleGene> -> 0.0 }, genotypeFactory).apply {
                    populationSize = size
                }
            }
        }

        "to a negative value throws an exception" {
            val arbGenotypeFactory = arbGenotypeFactory(Arb.list(arbDoubleChromosomeFactory()))
            checkAll(Arb.negativeInt(), arbGenotypeFactory) { size, genotypeFactory ->
                shouldThrow<CompositeException> {
                    GeneticAlgorithm.Factory({ _: Genotype<Double, DoubleGene> -> 0.0 }, genotypeFactory).apply {
                        populationSize = size
                    }
                }.shouldHaveInfringement<EngineException>("Population size ($size) must be positive.")
            }
        }

        "to zero throws an exception" {
            checkAll(arbGenotypeFactory(Arb.list(arbDoubleChromosomeFactory()))) { genotypeFactory ->
                shouldThrow<CompositeException> {
                    GeneticAlgorithm.Factory({ _: Genotype<Double, DoubleGene> -> 0.0 }, genotypeFactory).apply {
                        populationSize = 0
                    }
                }.shouldHaveInfringement<EngineException>("Population size (0) must be positive.")
            }
        }
    }

    "Setting the survival rate" - {
        "to a value between 0 and 1 mutates the property" {
            val arbGenotypeFactory = arbGenotypeFactory(Arb.list(arbDoubleChromosomeFactory()))
            checkAll(
                Arb.double(0.0..1.0, false).filterNot { it == 0.0 || it == 1.0 },
                arbGenotypeFactory
            ) { rate, genotypeFactory ->
                GeneticAlgorithm.Factory({ _: Genotype<Double, DoubleGene> -> 0.0 }, genotypeFactory).apply {
                    survivalRate = rate
                }
            }
        }

        "to a value less than 0 throws an exception" {
            val arbGenotypeFactory = arbGenotypeFactory(Arb.list(arbDoubleChromosomeFactory()))
            checkAll(
                Arb.negativeDouble(includeNonFiniteEdgeCases = false),
                arbGenotypeFactory
            ) { rate, genotypeFactory ->
                shouldThrow<CompositeException> {
                    GeneticAlgorithm.Factory({ _: Genotype<Double, DoubleGene> -> 0.0 }, genotypeFactory).apply {
                        survivalRate = rate
                    }
                }.shouldHaveInfringement<EngineException>("Survival rate ($rate) must be between 0 and 1.")
            }
        }

        "to a value greater than 1 throws an exception" {
            val arbGenotypeFactory = arbGenotypeFactory(Arb.list(arbDoubleChromosomeFactory()))
            checkAll(
                Arb.double(1.0..Double.MAX_VALUE, false).filterNot { it == 1.0 },
                arbGenotypeFactory
            ) { rate, genotypeFactory ->
                shouldThrow<CompositeException> {
                    GeneticAlgorithm.Factory({ _: Genotype<Double, DoubleGene> -> 0.0 }, genotypeFactory).apply {
                        survivalRate = rate
                    }
                }.shouldHaveInfringement<EngineException>("Survival rate ($rate) must be between 0 and 1.")
            }
        }
    }
})
