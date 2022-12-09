package cl.ravenhill.keen.genetic.genes.numerical

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.util.nextIntOutsideOf
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldHaveSameHashCodeAs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.long
import io.kotest.property.assume
import io.kotest.property.checkAll
import kotlin.random.Random

data class IntGeneData(val value: Int, val range: IntRange)

private val Arb.Companion.intGene
    get() = arbitrary { rs ->
        val r1 = rs.random.nextInt()
        val r2 = rs.random.nextInt()
        val range = if (r1 < r2) r1..r2 else r2..r1
        val value = range.random(rs.random)
        IntGeneData(value, range)
    }

private val Arb.Companion.intRange
    get() = arbitrary { rs ->
        val r1 = rs.random.nextInt(-1_000_000, 1_000_000)
        val r2 = rs.random.nextInt(-1_000_000, 1_000_000)
        if (r1 < r2) r1..r2 else r2..r1
    }

class IntGeneSpec : WordSpec({
    "Calculating its mean" should {
        "return the mean of the two genes" {
            checkAll(Arb.intGene, Arb.intGene) { gData1, gData2 ->
                val gene1 = IntGene(gData1.value, gData1.range)
                val gene2 = IntGene(gData2.value, gData2.range)
                gene1.mean(gene2) shouldBe IntGene(
                    (gData1.value and gData2.value) + ((gData1.value xor gData2.value) shr 1),
                    gData1.range
                )
            }
        }
    }
    "Comparing" should {
        "return 0 if the genes are equal" {
            checkComparison { dna, _, range ->
                IntGene(dna, range).compareTo(IntGene(dna, range)) shouldBe 0
            }
        }

        "return a negative number if the first gene is less than the second" {
            checkComparison { dna1, dna2, range ->
                IntGene(dna1, range).compareTo(IntGene(dna2, range)) shouldBeLessThan 0
            }
        }

        "return a positive number if the first gene is greater than the second" {
            checkComparison { dna1, dna2, range ->
                IntGene(dna2, range).compareTo(IntGene(dna1, range)) shouldBeGreaterThan 0
            }
        }
    }
    "Conversion" When {
        "Converting to Int" should {
            "return the gene's dna" {
                checkAll(Arb.intGene) { gData ->
                    IntGene(gData.value, gData.range).toInt() shouldBe gData.value
                }
            }
        }
        "Converting to Double" should {
            "return the gene's dna as a Double" {
                checkAll(Arb.intGene) { gData ->
                    IntGene(gData.value, gData.range).toDouble() shouldBe gData.value.toDouble()
                }
            }
        }
    }

    "Flattening" should {
        "return a list with the gene's value" {
            checkAll(Arb.intGene) { gData ->
                IntGene(gData.value, gData.range).flatten() shouldBe listOf(gData.value)
            }
        }
    }
    "Genetic operations" When {
        "Duplicating" should {
            "return a new gene with the given dna" {
                checkAll(Arb.intGene, Arb.long(-1000000L, 1000000L)) { gData, seed ->
                    val expected = gData.range.random(Random(seed))
                    IntGene(gData.value, gData.range).duplicate(expected) shouldBe
                            IntGene(expected, gData.range)
                }
            }
        }
        "Mutation" should {
            "return a gene with a random value" {
                checkAll(Arb.intGene, Arb.long()) { gData, seed ->
                    Core.rng = Random(seed)
                    val rng = Random(seed)
                    val expected = gData.range.random(rng)
                    IntGene(gData.value, gData.range).mutate() shouldBe
                            IntGene(expected, gData.range)
                }
            }
        }
    }
    "Object identity" When {
        "checking for equality" should {
            "return true if the genes are the same object" {
                checkAll(Arb.intGene) { geneData ->
                    val gene1 = IntGene(geneData.value, geneData.range)
                    val gene2 = IntGene(geneData.value, geneData.range)
                    gene1 shouldBe gene2
                }
            }
            "return false if the genes are different objects" {
                checkAll(Arb.intGene, Arb.intGene) { geneData1, geneData2 ->
                    assume(geneData1 != geneData2)
                    val gene1 = IntGene(geneData1.value, geneData1.range)
                    val gene2 = IntGene(geneData2.value, geneData2.range)
                    gene1 shouldNotBe gene2
                }
            }
        }
        "checking hashing" should {
            "return the same hash code for equal genes" {
                checkAll(Arb.intGene) { geneData ->
                    val gene1 = IntGene(geneData.value, geneData.range)
                    val gene2 = IntGene(geneData.value, geneData.range)
                    gene1 shouldHaveSameHashCodeAs gene2
                }
            }
            "return different hash codes for different genes" {
                checkAll(Arb.intGene, Arb.intGene) { geneData1, geneData2 ->
                    assume(geneData1 != geneData2)
                    val gene1 = IntGene(geneData1.value, geneData1.range)
                    val gene2 = IntGene(geneData2.value, geneData2.range)
                    gene1 shouldNotBe gene2
                }
            }
        }
    }
    "Verifying a gene" should {
        "return true if the gene is within the range and passes the filter" {
            checkAll(Arb.intGene) { geneData ->
                assume(geneData.value > 10000 || geneData.value < -10000)
                IntGene(
                    geneData.value,
                    geneData.range
                ) { it > 10000 || it < -10000 }.verify() shouldBe true
            }
        }
        "return false if the gene does not pass the filter" {
            checkAll(Arb.intGene) { geneData ->
                assume(geneData.value % 5 != 0)
                IntGene(geneData.value, geneData.range) { it % 5 == 0 }.verify() shouldBe false
            }
        }
        "return false if the gene is outside the range" {
            checkAll(Arb.intRange, Arb.long()) { range, seed ->
                IntGene(Random(seed).nextIntOutsideOf(range), range).verify() shouldBe false
            }
        }
    }
})

private suspend fun checkComparison(comparison: (Int, Int, IntRange) -> Unit) {
    checkAll(Arb.int(), Arb.int(), Arb.intRange) { dna1, dna2, range ->
        assume(dna1 != dna2)
        val (lo, hi) = if (dna1 < dna2) dna1 to dna2 else dna2 to dna1
        comparison(lo, hi, range)
    }
}