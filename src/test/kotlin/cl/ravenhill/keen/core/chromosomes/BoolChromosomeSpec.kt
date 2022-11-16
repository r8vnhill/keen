package cl.ravenhill.keen.core.chromosomes

import cl.ravenhill.keen.core.KeenCore
import cl.ravenhill.keen.core.genes.BoolGene
import cl.ravenhill.keen.signals.configuration.ChromosomeConfigurationException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.haveSameHashCodeAs
import io.kotest.matchers.types.shouldHaveSameHashCodeAs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.assume
import io.kotest.property.checkAll
import java.util.*

class BoolChromosomeSpec : WordSpec({
    "A BoolChromosome" When {
        "building" should {
            "be created with a given size" {
                checkAll(arbitraryChromosome()) { chromosomeData ->
                    assume(chromosomeData.truesProbability in 0.0..1.0)
                    val chromosome = chromosomeData.toChromosome()
                    chromosome.size shouldBe chromosomeData.size
                }
            }

            "be created with a given size and trues probability" {
                checkAll(
                    arbitraryChromosome(),
                    Arb.long()
                ) { chromosomeData, seed ->
                    assume(chromosomeData.truesProbability in 0.0..1.0)
                    val generator = Random(seed)
                    KeenCore.generator = Random(seed)
                    val values = List(chromosomeData.size) {
                        if (generator.nextDouble() < chromosomeData.truesProbability) {
                            BoolGene.True
                        } else {
                            BoolGene.False
                        }
                    }
                    val chromosome = chromosomeData.toChromosome()
                    chromosome.genes shouldBe values
                }
            }

            "throw an exception if the size is not positive" {
                checkAll(
                    arbitraryChromosome(Arb.nonPositiveInt())
                ) { chromosomeData ->
                    shouldThrow<ChromosomeConfigurationException> {
                        chromosomeData.toChromosome()
                    }.message shouldContain "The size of a chromosome must be positive."
                }
            }

            "throw an exception if the trues probability is not between 0 and 1" {
                checkAll(arbitraryChromosome(probabilityGenerator = Arb.double())) { chromosomeData ->
                    assume(chromosomeData.truesProbability < 0.0 || chromosomeData.truesProbability > 1.0)
                    shouldThrow<ChromosomeConfigurationException> {
                        chromosomeData.toChromosome()
                    }.message shouldContain
                            "The probability of a gene being true must be between 0.0 and 1.0."
                }
            }
        }

        "verifying" should {
            "be valid if it has at least one gene" {
                checkAll(arbitraryChromosome()) { chromosomeData ->
                    assume(chromosomeData.truesProbability in 0.0..1.0)
                    val chromosome = chromosomeData.toChromosome()
                    chromosome.verify() shouldBe true
                }
            }
        }
    }

    "comparing equality" should {
        "be equal if they have the same genes" {
            checkAll(
                Arb.positiveInt(100),
                Arb.double(0.0, 1.0),
                Arb.long()
            ) { size, truesProbability, seed ->
                assume(truesProbability in 0.0..1.0)
                val (chromosome1, chromosome2) = buildChromosomes(size, truesProbability, seed)
                chromosome1 shouldBe chromosome2
            }
        }

        "be different if they don't have the same genes" {
            checkAll(
                arbitraryChromosome(),
                arbitraryChromosome()
            ) { chromosomeData1, chromosomeData2 ->
                assume(chromosomeData1.truesProbability in 0.0..1.0)
                assume(chromosomeData2.truesProbability in 0.0..1.0)
                assume(chromosomeData1.toChromosome().genes != chromosomeData2.toChromosome().genes)
                chromosomeData1.toChromosome() shouldNotBe chromosomeData2.toChromosome()
            }
        }
    }

    "obtaining the hash code" should {
        "be the same if they have the same genes" {
            checkAll(
                Arb.positiveInt(100),
                Arb.double(0.0, 1.0),
                Arb.long()
            ) { size, truesProbability, seed ->
                assume(truesProbability in 0.0..1.0)
                val (chromosome1, chromosome2) = buildChromosomes(size, truesProbability, seed)
                chromosome1 shouldHaveSameHashCodeAs chromosome2
            }
        }

        "be different if they don't have the same genes" {
            checkAll(
                arbitraryChromosome(),
                arbitraryChromosome()
            ) { chromosomeData1, chromosomeData2 ->
                assume(chromosomeData1.truesProbability in 0.0..1.0)
                assume(chromosomeData2.truesProbability in 0.0..1.0)
                assume(chromosomeData1.toChromosome().genes != chromosomeData2.toChromosome().genes)
                chromosomeData1.toChromosome() shouldNot haveSameHashCodeAs(chromosomeData2.toChromosome())
            }
        }
    }

    "counting bits" should {
        "be consistent" {
            checkAll(
                Arb.positiveInt(100),
                Arb.double(0.0, 1.0),
                Arb.long()
            ) { size, truesProbability, seed ->
                assume(truesProbability in 0.0..1.0)
                val chromosome = buildChromosomes(size, truesProbability, seed).first
                chromosome.trues() shouldBe chromosome.genes.count { it == BoolGene.True }
            }
        }
    }

    "copying" should {
        "be the same if they have the same genes" {
            checkAll(arbitraryChromosome()) {
                assume(it.truesProbability in 0.0..1.0)
                val chromosome = it.toChromosome()
                chromosome.copy(chromosome.genes) shouldBe chromosome
            }

        }
    }

    "getting a gene" should {
        "return the gene at the given index" {
            checkAll(
                arbitraryChromosome(),
                Arb.long()
            ) { chromosomeData, seed ->
                val index = Random(seed).nextInt(chromosomeData.size)
                assume(chromosomeData.truesProbability in 0.0..1.0)
                val chromosome = chromosomeData.toChromosome()
                chromosome[index] shouldBe chromosome.genes[index]
            }
        }
    }

    "BoolChromosome.Builder" When {
        "converting to String" should {
            "return a string representation of the chromosome" {
                checkAll(
                    Arb.positiveInt(100),
                    Arb.double(0.0, 1.0)
                ) { size, truesProbability ->
                    assume(truesProbability in 0.0..1.0)
                    BoolChromosome.Builder(size, truesProbability).toString() shouldBe
                            "BoolChromosome.Builder { " +
                            "size: $size, " +
                            "truesProbability: $truesProbability }"
                }
            }
        }
    }
})

private data class BoolChromosomeSpecData(
    val size: Int,
    val truesProbability: Double
)

private fun arbitraryChromosome(
    sizeGenerator: Arb<Int> = Arb.positiveInt(100),
    probabilityGenerator: Arb<Double> = Arb.double(0.0..1.0)
) = Arb.bind(
    sizeGenerator,
    probabilityGenerator
) { size, truesProbability ->
    BoolChromosomeSpecData(
        size = size,
        truesProbability = truesProbability
    )
}

private fun BoolChromosomeSpecData.toChromosome() =
    BoolChromosome.Builder(size, truesProbability).build()

private fun buildChromosomes(
    size: Int,
    truesProbability: Double,
    seed: Long
): Pair<BoolChromosome, BoolChromosome> {
    KeenCore.generator = Random(seed)
    val chromosome = BoolChromosome.Builder(size, truesProbability).build()
    KeenCore.generator = Random(seed)
    val other = BoolChromosome.Builder(size, truesProbability).build()
    return Pair(chromosome, other)
}
