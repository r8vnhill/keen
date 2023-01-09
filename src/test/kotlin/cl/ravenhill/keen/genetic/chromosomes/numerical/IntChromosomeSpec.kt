package cl.ravenhill.keen.genetic.chromosomes.numerical

import cl.ravenhill.keen.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.nonPositiveInt
import io.kotest.property.checkAll
import kotlin.random.Random
import kotlin.reflect.KClass


class IntChromosomeSpec : WordSpec({
    afterAny {
        Core.random = Random.Default
    }
    "Chromosome factory" When {
        "creating a chromosome with a given size and range" should {
            "return a chromosome with the given size" {
                checkAll(Arb.int(1, 100_000), Arb.orderedIntPair()) { size, range ->
                    `create a new chromosome using it's factory`(
                        size,
                        range
                    ).size shouldBe size
                }
            }
            "return a chromosome with genes in the given range" {
                checkAll(Arb.int(1, 100_000), Arb.orderedIntPair()) { size, range ->
                    `create a new chromosome using it's factory`(
                        size,
                        range
                    ).genes.forEach { gene ->
                        (gene.dna < range.second) shouldBe true
                        (gene.dna >= range.first) shouldBe true
                    }
                }
            }
            "throw an exception if the size is less than 1" {
                checkAll(Arb.nonPositiveInt(), Arb.orderedIntPair()) { size, range ->
                    shouldThrow<UnfulfilledContractException> {
                        `create a new chromosome using it's factory`(size, range)
                    }.violations.first() shouldBeOfClass IntConstraintException::class
                }
            }
            "throw an exception if the range is not ordered" {
                checkAll(Arb.int(1, 100_000), Arb.reversedPair()) { size, range ->
                    shouldThrow<UnfulfilledContractException> {
                        `create a new chromosome using it's factory`(size, range)
                    }.violations.first() shouldBeOfClass PairConstraintException::class
                }
            }
        }
    }
})

/**
 * Creates a new chromosome using a chromosome factory.
 */
private fun `create a new chromosome using it's factory`(
    /** The size of the chromosome */
    size: Int,
    /** The range of the genes     */
    range: Pair<Int, Int>
) = IntChromosome.Factory().apply { this.size = size; this.range = range }.make()

/**
 * Generates [Arb]itrary [Pair]s of [Int]s where the first element is less than or equal
 * to the second.
 *
 * __Usage:__
 * ```kotlin
 * checkAll(Arb.orderedIntPair()) { (a, b) ->
 *    a <= b shouldBe true
 *    a + b shouldBe b + a
 *    a * b shouldBe b * a
 *    a - b shouldBe -(b - a)
 *    a / b shouldBe 0
 *    a % b shouldBe a
 *    a * b + a - b / a % b shouldBe a * b
 * }
 * ```
 */
private fun Arb.Companion.orderedIntPair() = arbitrary {
    val first = int().bind()
    val second = int().bind().let { if (it == first) it + 1 else it }
    if (first < second) first to second else second to first
}

private fun Arb.Companion.reversedPair() = arbitrary {
    val first = int().bind()
    val second = int().bind().let { if (it == first) it + 1 else it }
    if (first < second) second to first else first to second
}