package cl.ravenhill.keen.genetic.chromosomes.numerical

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.InvalidStateException
import cl.ravenhill.keen.genetic.genes.doubleGene
import cl.ravenhill.keen.util.math.isNotNan
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.assume
import io.kotest.property.checkAll
import kotlin.random.Random


class DoubleChromosomeSpec : WordSpec({
    afterAny {
        Core.random = Random.Default
    }
    "Chromosome factory" When {
        "creating a chromosome with a given size and range" should {
            "return a chromosome with the given size" {
                checkAll(Arb.int(1, 100_000), Arb.orderedDoublePair()) { size, range ->
                    makeChromosome(size, range).size shouldBe size
                }
            }
            "return a chromosome with genes in the given range" {
                checkAll(Arb.int(1, 100_000), Arb.orderedDoublePair()) { size, range ->
                    makeChromosome(size, range).genes.forEach { gene ->
                        (gene.dna < range.second) shouldBe true
                        (gene.dna >= range.first) shouldBe true
                    }
                }
            }
            "return a chromosome with NaN genes if the range is NaN" {
                checkAll(Arb.int(1, 100_000), Arb.double()) { size, bound ->
                    makeChromosome(size, Pair(Double.NaN, bound)).genes.forEach { gene ->
                        gene.dna.isNaN() shouldBe true
                    }
                    makeChromosome(size, Pair(bound, Double.NaN)).genes.forEach { gene ->
                        gene.dna.isNaN() shouldBe true
                    }
                }
            }
            "throw an exception if the size is less than 1" {
                checkAll(Arb.nonPositiveInt(), Arb.orderedDoublePair()) { size, range ->
                    shouldThrow<InvalidStateException> {
                        makeChromosome(size, range)
                    }
                }
            }
            "throw an exception if the range is infinite" {
                checkAll(Arb.int(1, 100_000), Arb.double()) { size, bound ->
                    assume(bound.isNotNan())
                    shouldThrow<InvalidStateException> {
                        makeChromosome(size, Pair(Double.NEGATIVE_INFINITY, bound))
                    }
                    shouldThrow<InvalidStateException> {
                        makeChromosome(size, Pair(bound, Double.POSITIVE_INFINITY))
                    }
                }
            }
            "throw an exception if the range is empty" {
                checkAll(Arb.int(1, 100_000), Arb.double()) { size, bound ->
                    assume(bound.isNotNan())
                    shouldThrow<InvalidStateException> {
                        makeChromosome(size, Pair(bound, bound))
                    }
                }
            }
            "throw an exception if the range is reversed" {
                checkAll(Arb.int(1, 100_000), Arb.orderedDoublePair()) { size, range ->
                    shouldThrow<InvalidStateException> {
                        makeChromosome(size, range.second to range.first)
                    }
                }
            }
        }
    }
    "Duplicating" should {
        "return a new chromosome with the given genes" {
            checkAll(Arb.doubleChromosome()) { chromosome ->
                val duplicated = chromosome.duplicate(chromosome.genes)
                duplicated shouldBe chromosome
            }
        }
    }
    "Equality" should {
        "return true if both chromosomes are the same object" {
            checkAll(Arb.doubleChromosome()) { chromosome ->
                chromosome shouldBe chromosome
            }
        }
        "return true if both chromosomes have the same genes and range" {
            checkAll(Arb.positiveInt(1000), Arb.orderedDoublePair(), Arb.long()) { size, range, seed ->
                Core.random = Random(seed)
                val chromosome1 = makeChromosome(size, range)
                Core.random = Random(seed)
                val chromosome2 = makeChromosome(size, range)
                chromosome1 shouldBe chromosome2
            }
        }
        "return false if both chromosomes have different genes" {
            checkAll(Arb.doubleChromosome(), Arb.doubleChromosome()) { chromosome1, chromosome2 ->
                assume(chromosome1.genes != chromosome2.genes)
                chromosome1 shouldNotBe chromosome2
            }
        }
        "return false if both chromosomes have different range" {
            checkAll(Arb.doubleChromosome(), Arb.doubleChromosome()) { chromosome1, chromosome2 ->
                assume(chromosome1.range != chromosome2.range)
                chromosome1 shouldNotBe chromosome2
            }
        }
    }
    "Hashing" should {
        "return the same hash code for the same chromosome" {
            TODO()
        }
        "return the same hash code for two chromosomes with the same genes and range" {
            TODO()
        }
        "return different hash codes for two chromosomes with different genes" {
            TODO()
        }
    }
    "Verifying" should {
        "return true if the chromosome is valid" {
            TODO()
        }
        "return false if the chromosome is not valid" {
            TODO()
        }
    }
})

private fun Arb.Companion.orderedDoublePair() = arbitrary {
    val first = Arb.double().next()
    val second = Arb.double().next()
    if (first < second) {
        first to second
    } else {
        second to first
    }
}

private fun Arb.Companion.doubleChromosome() = arbitrary {
    val size = Arb.int(1, 1000).bind()
    val range = Arb.orderedDoublePair().bind()
    makeChromosome(size, range)
}

private fun makeChromosome(size: Int, range: Pair<Double, Double>) =
    DoubleChromosome.Factory().apply {
        this.size = size
        this.range = range
    }.make()
