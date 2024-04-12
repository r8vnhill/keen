package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.keen.arb.datatypes.arbProbability
import cl.ravenhill.keen.arb.operators.arbSinglePointCrossover
import cl.ravenhill.keen.assertions.should.shouldBeExclusive
import cl.ravenhill.keen.assertions.should.shouldNotBeExclusive
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.flatMap
import io.kotest.property.arbitrary.map
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

        "with a custom chromosome rate" {
            checkAll(
                arbProbability().flatMap { rate ->
                    arbSinglePointCrossover<Int, IntGene>(Arb.constant(rate), null)
                        .map { rate to it }
                }) { (rate, crossover) ->
                with(crossover) {
                    numParents shouldBe 2
                    numOffspring shouldBe 2
                    shouldNotBeExclusive()
                    chromosomeRate shouldBe rate
                }
            }
        }

        "with exclusivity" {
            checkAll(arbSinglePointCrossover<Int, IntGene>(null, Arb.constant(true))) { crossover ->
                with(crossover) {
                    numParents shouldBe 2
                    numOffspring shouldBe 2
                    shouldBeExclusive()
                    chromosomeRate shouldBe 1.0
                }
            }
        }

        "without exclusivity" {
            checkAll(arbSinglePointCrossover<Int, IntGene>(null, Arb.constant(false))) { crossover ->
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