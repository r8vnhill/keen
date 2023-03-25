package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.EnforcementException
import cl.ravenhill.keen.IntRequirementException
import cl.ravenhill.keen.intChromosomePair
import cl.ravenhill.keen.operators.crossover.pointbased.SinglePointCrossover
import cl.ravenhill.keen.probability
import cl.ravenhill.keen.shouldBeOfClass
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.long
import io.kotest.property.assume
import io.kotest.property.checkAll
import kotlin.math.min
import kotlin.random.Random


class SinglePointCrossoverTest : FreeSpec({
    beforeAny {
        Core.random = Random.Default
    }
    "Converting to" - {
        "String should" - {
            "Return the class name and the probability" {
                checkAll(Arb.probability()) { probability ->
                    SinglePointCrossover<Any>(probability).toString() shouldBe
                            "SinglePointCrossover { probability: $probability }"
                }
            }
        }
    }
    "Creating a Crossover operator should" - {
        "throw an exception if the probability is not between 0 and 1" {
            checkAll(Arb.double()) { probability ->
                assume(probability < 0 || probability > 1)
                shouldThrow<EnforcementException> {
                    SinglePointCrossover<Any>(probability)
                }.violations.first() shouldBeOfClass IntRequirementException::class
            }
        }
    }
    "Crossing two Chromosomes" - {
        "at a Given Point should" - {
            "swap all the genes if the cut point is 0" {
                checkAll(
                    Arb.intChromosomePair(),
                    Arb.probability(),
                    Arb.long()
                ) { (c1, c2), probability, seed ->
                    Core.random = Random(seed)
                    val crossover = SinglePointCrossover<Int>(probability)
                    val crossed = crossover.crossoverAt(0, c1.genes to c2.genes)
                    crossed.first shouldBe c2.genes
                    crossed.second shouldBe c1.genes
                }
            }
            "swap no genes if the cut point is the size of the chromosome" {
                checkAll(
                    Arb.intChromosomePair(),
                    Arb.probability(),
                    Arb.long()
                ) { (c1, c2), probability, seed ->
                    Core.random = Random(seed)
                    val crossover = SinglePointCrossover<Int>(probability)
                    val crossed = crossover.crossoverAt(c1.genes.size, c1.genes to c2.genes)
                    crossed.first shouldBe c1.genes
                    crossed.second shouldBe c2.genes
                }
            }
            "swap all the genes after the cut point" {
                checkAll(
                    Arb.intChromosomePair(),
                    Arb.probability(),
                    Arb.long()
                ) { (c1, c2), probability, seed ->
                    Core.random = Random(seed)
                    val cutPoint = Core.random.nextInt(min(c1.genes.size, c2.genes.size))
                    val crossover = SinglePointCrossover<Int>(probability)
                    val crossed = crossover.crossoverAt(cutPoint, c1.genes to c2.genes)
                    crossed.first.take(cutPoint) shouldBe c1.genes.take(cutPoint)
                    crossed.first.drop(cutPoint) shouldBe c2.genes.drop(cutPoint)
                    crossed.second.take(cutPoint) shouldBe c2.genes.take(cutPoint)
                    crossed.second.drop(cutPoint) shouldBe c1.genes.drop(cutPoint)
                }
            }
        }
    }
})