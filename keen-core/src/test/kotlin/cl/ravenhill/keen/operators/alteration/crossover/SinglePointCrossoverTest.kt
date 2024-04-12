package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.arb.datatypes.arbProbability
import cl.ravenhill.keen.arb.genetic.genes.arbIntGene
import cl.ravenhill.keen.arb.operators.arbSinglePointCrossover
import cl.ravenhill.keen.assertions.should.shouldBeExclusive
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.assertions.should.shouldNotBeExclusive
import cl.ravenhill.keen.exceptions.CrossoverConfigException
import cl.ravenhill.keen.exceptions.CrossoverException
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.flatMap
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll

class SinglePointCrossoverTest : FreeSpec({
    "Can be constructed" - {
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

    "Throws an exception if the chromosome rate is invalid" {
        checkAll(Arb.double().filter { it < 0.0 || it > 1.0 }, Arb.boolean()) { rate, exclusivity ->
            shouldThrow<CompositeException> {
                SinglePointCrossover<Int, IntGene>(rate, exclusivity)
            }.shouldHaveInfringement<CrossoverConfigException>("The chromosome rate must be in the range [0.0, 1.0].")
        }
    }

    "When crossing at a given point" - {
        "throws an exception if the index is out of bounds" {
            checkAll(arbSinglePointCrossover<Int, IntGene>(), genesAndInvalidIndex()) { crossover, (gs1, gs2, index) ->
                shouldThrow<CompositeException> {
                    crossover.crossoverAt(index, gs1 to gs2)
                }.shouldHaveInfringement<CrossoverException>(
                    "The crossover point must be in the range [0, ${gs1.size}]."
                )
            }
        }

        "returns the correct offspring" {
            checkAll(arbSinglePointCrossover<Int, IntGene>(), genesAndValidIndex()) { crossover, (gs1, gs2, index) ->
                val (offspring1, offspring2) = crossover.crossoverAt(index, gs1 to gs2)
                offspring1 shouldBe gs1.take(index) + gs2.drop(index)
                offspring2 shouldBe gs2.take(index) + gs1.drop(index)
            }
        }
    }
})

private fun genesAndInvalidIndex(): Arb<Triple<List<IntGene>, List<IntGene>, Int>> =
    Arb.list(arbIntGene()).flatMap { gs1 ->
        Arb.list(arbIntGene(), gs1.size..gs1.size).flatMap {
            Arb.int()
                .filter { it < 0 || it > gs1.size }
                .map { index -> Triple(gs1, it, index) }
        }
    }

private fun genesAndValidIndex(): Arb<Triple<List<IntGene>, List<IntGene>, Int>> =
    Arb.list(arbIntGene()).flatMap { gs1 ->
        Arb.list(arbIntGene(), gs1.size..gs1.size).flatMap {
            Arb.int(0, gs1.size).map { index -> Triple(gs1, it, index) }
        }
    }

