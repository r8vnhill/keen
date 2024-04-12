package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.keen.arb.operators.arbSinglePointCrossover
import cl.ravenhill.keen.assertions.should.shouldNotBeExclusive
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

class SinglePointCrossoverTest : FreeSpec({
    "A Single Point Crossover Operator can be constructed" - {
        "with default parameters" {
            checkAll(arbSinglePointCrossover<Int, IntGene>(null, null)) { crossover ->
                with(crossover) {
                    numParents shouldBe 2
                    numOffspring shouldBe 2
                    shouldNotBeExclusive()
                    chromosomeRate shouldBe 1.0
                }
            }
        }
    }
})