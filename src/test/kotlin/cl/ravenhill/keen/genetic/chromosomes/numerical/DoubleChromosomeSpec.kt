package cl.ravenhill.keen.genetic.chromosomes.numerical

import cl.ravenhill.keen.*
import cl.ravenhill.keen.util.math.isNotNan
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldHaveSameHashCodeAs
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
                    `create a new chromosome using it's factory`(
                        size,
                        range
                    ).size shouldBe size
                }
            }
            "return a chromosome with genes in the given range" {
                checkAll(Arb.int(1, 10_000), Arb.orderedDoublePair()) { size, range ->
                    `create a new chromosome using it's factory`(
                        size,
                        range
                    ).genes.forEach { gene ->
                        (gene.dna < range.second) shouldBe true
                        (gene.dna >= range.first) shouldBe true
                    }
                }
            }
            "throw an exception if the range has NaN bounds" {
                checkAll(Arb.int(1, 100_000), Arb.double()) { size, bound ->
                    shouldThrow<EnforcementException> {
                        `create a new chromosome using it's factory`(
                            size,
                            Pair(Double.NaN, bound)
                        )
                    }.violations.first() shouldBeOfClass PairRequirementException::class
                    shouldThrow<EnforcementException> {
                        `create a new chromosome using it's factory`(
                            size,
                            Pair(bound, Double.NaN)
                        )
                    }.violations.first() shouldBeOfClass PairRequirementException::class
                }
            }
            "throw an exception if the size is less than 1" {
                checkAll(Arb.nonPositiveInt(), Arb.orderedDoublePair()) { size, range ->
                    shouldThrow<EnforcementException> {
                        `create a new chromosome using it's factory`(size, range)
                    }.violations.first() shouldBeOfClass IntRequirementException::class
                }
            }
            "throw an exception if the range is infinite" {
                checkAll(Arb.int(1, 100_000), Arb.double()) { size, bound ->
                    assume(bound.isNotNan())
                    shouldThrow<EnforcementException> {
                        `create a new chromosome using it's factory`(
                            size,
                            Pair(Double.NEGATIVE_INFINITY, bound)
                        )
                    }.violations.first() shouldBeOfClass PairRequirementException::class
                    shouldThrow<EnforcementException> {
                        `create a new chromosome using it's factory`(
                            size,
                            Pair(bound, Double.POSITIVE_INFINITY)
                        )
                    }.violations.first() shouldBeOfClass PairRequirementException::class
                }
            }
            "throw an exception if the range is empty" {
                checkAll(Arb.int(1, 100_000), Arb.double()) { size, bound ->
                    assume(bound.isNotNan())
                    shouldThrow<EnforcementException> {
                        `create a new chromosome using it's factory`(
                            size,
                            Pair(bound, bound)
                        )
                    }.violations.first() shouldBeOfClass PairRequirementException::class
                }
            }
            "throw an exception if the range is reversed" {
                checkAll(Arb.int(1, 100_000), Arb.orderedDoublePair()) { size, range ->
                    shouldThrow<EnforcementException> {
                        `create a new chromosome using it's factory`(
                            size,
                            range.second to range.first
                        )
                    }.violations.first() shouldBeOfClass PairRequirementException::class
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
            checkAll(
                Arb.positiveInt(1000),
                Arb.orderedDoublePair(),
                Arb.long()
            ) { size, range, seed ->
                Core.random = Random(seed)
                val chromosome1 =
                    `create a new chromosome using it's factory`(size, range)
                Core.random = Random(seed)
                val chromosome2 =
                    `create a new chromosome using it's factory`(size, range)
                chromosome1 shouldBe chromosome2
            }
        }
        "return false if both chromosomes have different genes" {
            checkAll(
                Arb.doubleChromosome(),
                Arb.doubleChromosome()
            ) { chromosome1, chromosome2 ->
                assume(chromosome1.genes != chromosome2.genes)
                chromosome1 shouldNotBe chromosome2
            }
        }
        "return false if both chromosomes have different range" {
            checkAll(
                Arb.doubleChromosome(),
                Arb.doubleChromosome()
            ) { chromosome1, chromosome2 ->
                assume(chromosome1.range != chromosome2.range)
                chromosome1 shouldNotBe chromosome2
            }
        }
    }
    "Hashing" should {
        "return the same hash code for the same chromosome" {
            checkAll(Arb.doubleChromosome()) { chromosome ->
                chromosome shouldHaveSameHashCodeAs chromosome
            }
        }
        "return the same hash code for two chromosomes with the same genes and range" {
            checkAll(
                Arb.positiveInt(1000),
                Arb.orderedDoublePair(),
                Arb.long()
            ) { size, range, seed ->
                Core.random = Random(seed)
                val chromosome1 =
                    `create a new chromosome using it's factory`(size, range)
                Core.random = Random(seed)
                val chromosome2 =
                    `create a new chromosome using it's factory`(size, range)
                chromosome1 shouldHaveSameHashCodeAs chromosome2
            }
        }
        "return different hash codes for two chromosomes with different genes" {
            checkAll(
                Arb.doubleChromosome(),
                Arb.doubleChromosome()
            ) { chromosome1, chromosome2 ->
                assume(chromosome1.genes != chromosome2.genes)
                chromosome1 shouldNotBe chromosome2
            }
        }
        "return different hash codes for two chromosomes with different range" {
            checkAll(
                Arb.doubleChromosome(),
                Arb.doubleChromosome()
            ) { chromosome1, chromosome2 ->
                assume(chromosome1.range != chromosome2.range)
                chromosome1 shouldNotBe chromosome2
            }
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
    `create a new chromosome using it's factory`(size, range)
}

private fun `create a new chromosome using it's factory`(
    size: Int,
    range: Pair<Double, Double>
) =
    DoubleChromosome.Factory().apply {
        this.size = size
        this.range = range
    }.make()
