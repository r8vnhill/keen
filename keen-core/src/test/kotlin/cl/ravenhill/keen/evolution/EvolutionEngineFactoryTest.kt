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
                val factory = EvolutionEngine.Factory(fitnessFunction, genotypeFactory)
                factory.genotypeFactory shouldBe genotypeFactory
                factory.fitnessFunction shouldBe fitnessFunction
            }
        }
    }
})
