package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.arb.genetic.chromosomes.chromosomeFactory
import cl.ravenhill.keen.arb.genetic.chromosomes.doubleChromosomeFactory
import cl.ravenhill.keen.arb.genetic.genotypeFactory
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll

class EvolutionEngineFactoryTest : FreeSpec({
    "Can be created with" - {
        "a fitness function and a genotype factory" {
            checkAll(Arb.genotypeFactory(Arb.list(Arb.doubleChromosomeFactory()))) { genotypeFactory ->
                val fitnessFunction = { _: Genotype<Double, DoubleGene> -> 0.0 }
                with(EvolutionEngine.Factory(fitnessFunction, genotypeFactory)) {
                    this.genotypeFactory shouldBe genotypeFactory
                    this.fitnessFunction shouldBe fitnessFunction
                    populationSize shouldBe EvolutionEngine.Factory.DEFAULT_POPULATION_SIZE
                    survivalRate shouldBe EvolutionEngine.Factory.DEFAULT_SURVIVAL_RATE
                    parentSelector shouldBe EvolutionEngine.Factory.defaultParentSelector()
                    survivorSelector shouldBe EvolutionEngine.Factory.defaultSurvivorSelector()
                    alterers shouldBe EvolutionEngine.Factory.defaultAlterers()
                    limits shouldBe EvolutionEngine.Factory.defaultLimits()
                    ranker shouldBe EvolutionEngine.Factory.defaultRanker()
                    listeners shouldBe EvolutionEngine.Factory.defaultListeners()
                    evaluator shouldBe EvolutionEngine.Factory.defaultEvaluator()
                    interceptor shouldBe EvolutionEngine.Factory.defaultInterceptor()
                }
            }
        }
    }
})
