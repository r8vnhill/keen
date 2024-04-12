package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.keen.assertions.should.shouldNotBeExclusive
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class SinglePointCrossoverTest : FreeSpec({
    "A Single Point Crossover Operator can be constructed" - {
        "with default parameters" {
            with(SinglePointCrossover<Int, IntGene>()) {
                chromosomeRate shouldBe 1.0
                shouldNotBeExclusive()
                numParents shouldBe 2
                numOffspring shouldBe 2
            }
        }
    }
})