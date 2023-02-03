package cl.ravenhill.keen.genetic.chromosomes.numerical

import cl.ravenhill.keen.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.nonPositiveInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.assume
import io.kotest.property.checkAll
import kotlin.random.Random


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
                    shouldThrow<EnforcementException> {
                        `create a new chromosome using it's factory`(size, range)
                    }.violations.first() shouldBeOfClass IntRequirementException::class
                }
            }
            "throw an exception if the range is not ordered" {
                checkAll(Arb.int(1, 100_000), Arb.reversedPair()) { size, range ->
                    shouldThrow<EnforcementException> {
                        `create a new chromosome using it's factory`(size, range)
                    }.violations.first() shouldBeOfClass PairRequirementException::class
                }
            }
        }
    }
    "Duplicating" should {
        "return a new chromosome with the given genes" {
            checkAll(Arb.intChromosome(), Arb.intChromosome()) { chromosome, other ->
                val newChromosome = chromosome.duplicate(other.genes)
                newChromosome shouldNotBeSameInstanceAs other
                newChromosome.genes shouldBe other.genes
            }
        }
    }
    "Equality" should {
        "be true for chromosomes with the same genes and range" {
            checkAll(Arb.intChromosome(), Arb.intChromosome()) { chromosome, other ->
                val newChromosome = chromosome.duplicate(other.genes)
                newChromosome shouldNotBeSameInstanceAs other
                newChromosome shouldBe other
            }
        }
        "be false for chromosomes with different genes" {
            checkAll(Arb.intChromosome(), Arb.intChromosome()) { chromosome, other ->
                assume(chromosome.genes != other.genes)
                chromosome shouldNotBeSameInstanceAs other
                chromosome shouldNotBe other
            }
        }
        "be false for chromosomes with different range" {
            checkAll(Arb.intChromosome(), Arb.intChromosome()) { chromosome, other ->
                assume(chromosome.range != other.range)
                chromosome shouldNotBeSameInstanceAs other
                chromosome shouldNotBe other
            }
        }
        "be null safe" {
            checkAll(Arb.intChromosome()) { chromosome ->
                chromosome shouldNotBe null
            }
        }
    }
    "Hashing" should {
        "be true for chromosomes with the same genes and range" {
            checkAll(Arb.intChromosome(), Arb.intChromosome()) { chromosome, other ->
                val newChromosome = chromosome.duplicate(other.genes)
                newChromosome shouldNotBeSameInstanceAs other
                newChromosome.hashCode() shouldBe other.hashCode()
            }
        }
        "be false for chromosomes with different genes" {
            checkAll(Arb.intChromosome(), Arb.intChromosome()) { chromosome, other ->
                assume(chromosome.genes != other.genes)
                chromosome shouldNotBeSameInstanceAs other
                chromosome.hashCode() shouldNotBe other.hashCode()
            }
        }
        "be false for chromosomes with different range" {
            checkAll(Arb.intChromosome(), Arb.intChromosome()) { chromosome, other ->
                assume(chromosome.range != other.range)
                chromosome shouldNotBeSameInstanceAs other
                chromosome.hashCode() shouldNotBe other.hashCode()
            }
        }
    }
})

fun Arb.Companion.intChromosome(maxSize: Int = 10_000) = arbitrary {
    val size = Arb.positiveInt(maxSize).bind()
    val range = Arb.orderedIntPair().bind()
    `create a new chromosome using it's factory`(size, range)
}

/**
 * Creates a new chromosome using a chromosome factory.
 */
private fun `create a new chromosome using it's factory`(
    /** The size of the chromosome */
    size: Int,
    /** The range of the genes     */
    range: Pair<Int, Int>
) = IntChromosome.Factory().apply { this.size = size; this.range = range }.make()

private fun Arb.Companion.reversedPair() = arbitrary {
    val first = int().bind()
    val second = int().bind().let { if (it == first) it + 1 else it }
    if (first < second) second to first else first to second
}