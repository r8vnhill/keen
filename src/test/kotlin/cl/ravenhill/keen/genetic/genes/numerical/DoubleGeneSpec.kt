
package cl.ravenhill.keen.genetic.genes.numerical

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.util.math.isNotNan
import cl.ravenhill.keen.util.nextDoubleOutsideOf
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldHaveSameHashCodeAs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.long
import io.kotest.property.assume
import io.kotest.property.checkAll
import kotlin.random.Random

data class DoubleGeneData(val dna: Double, val range: Pair<Double, Double>)

private val Arb.Companion.doubleRange: Arb<Pair<Double, Double>>
    get() = arbitrary { rs ->
        val min = rs.random.nextDouble()
        val max = rs.random.nextDouble()
        if (min < max) min to max else max to min
    }
val arbRange = arbitrary { rs ->
    val min = rs.random.nextDouble()
    val max = rs.random.nextDouble()
    if (min < max) min to max else max to min
}

val doubleGeneArb = arbitrary { rs ->
    val min = rs.random.nextDouble()
    val max = rs.random.nextDouble()
    val range = if (min < max) min to max else max to min
    val dna = rs.random.nextDouble(range.first, range.second)
    DoubleGeneData(dna, range)
}

class DoubleGeneSpec : WordSpec({
    "Calculating its mean" should {
        "return the mean of the two genes" {
            checkAll(doubleGeneArb, doubleGeneArb) { gData1, gData2 ->
                val gene1 = DoubleGene(gData1.dna, gData1.range)
                val gene2 = DoubleGene(gData2.dna, gData2.range)
                gene1.mean(gene2) shouldBe DoubleGene(
                    (gData1.dna + gData2.dna) / 2,
                    gData1.range
                )
            }
        }
    }
    "Comparing" should {
        "return 0 if the genes are equal" {
            checkComparison { dna1, _, range ->
                DoubleGene(dna1, range).compareTo(DoubleGene(dna1, range)) shouldBe 0
            }
        }

        "return a negative number if the first gene is less than the second" {
            checkComparison { dna1, dna2, range ->
                DoubleGene(dna1, range).compareTo(
                    DoubleGene(
                        dna2,
                        range
                    )
                ) shouldBeLessThan 0
            }
        }

        "return a positive number if the first gene is greater than the second" {
            checkComparison { dna1, dna2, range ->
                DoubleGene(dna2, range).compareTo(
                    DoubleGene(
                        dna1,
                        range
                    )
                ) shouldBeGreaterThan 0
            }
        }
    }
    "Converting to Double" should {
        "return the gene's dna" {
            checkConversion { dna, range ->
                DoubleGene(dna, range).toDouble() shouldBe dna
            }
        }
    }
    "Converting to Int" should {
        "return the gene's dna as an Int" {
            checkConversion { dna, range ->
                DoubleGene(dna, range).toInt() shouldBe dna.toInt()
            }
        }
    }
    "Flattening" should {
        "return a list with the gene's value" {
            checkAll(Arb.double(), arbRange) { dna, range ->
                DoubleGene(dna, range).flatten() shouldBe listOf(dna)
            }
        }
    }
    "Genetic operations" When {
        "duplicating" should {
            "return a new gene with the same dna and range" {
                checkAll(doubleGeneArb, Arb.long()) { gData, seed ->
                    Core.rng = Random(seed)
                    val expected =
                        Random(seed).nextDouble(gData.range.first, gData.range.second)
                    DoubleGene(gData.dna, gData.range).duplicate(expected) shouldBe
                            DoubleGene(expected, gData.range)
                }
            }
        }
        "mutating" should {
            "return a new gene with random dna" {
                checkAll(doubleGeneArb, Arb.long()) { gData, seed ->
                    val rng = Random(seed)
                    Core.rng = Random(seed)
                    val expected = rng.nextDouble(gData.range.first, gData.range.second)
                    DoubleGene(gData.dna, gData.range).mutate() shouldBe
                            DoubleGene(expected, gData.range)
                }
            }
        }
    }
    "Object identity" When {
        "checking for equality" should {
            "return true if the genes are the same object" {
                checkAll(doubleGeneArb) { gData ->
                    val gene = DoubleGene(gData.dna, gData.range)
                    gene shouldBe gene
                }
            }
            "return false if the genes are different objects" {
                checkAll(doubleGeneArb, doubleGeneArb) { gData1, gData2 ->
                    assume(gData1 != gData2)
                    val gene1 = DoubleGene(gData1.dna, gData1.range)
                    val gene2 = DoubleGene(gData2.dna, gData2.range)
                    gene1 shouldNotBe gene2
                }
            }
        }
        "checking hashing" should {
            "return the same hash code for equal genes" {
                checkAll(doubleGeneArb) { gData1 ->
                    val gene1 = DoubleGene(gData1.dna, gData1.range)
                    val gene2 = DoubleGene(gData1.dna, gData1.range)
                    gene1 shouldHaveSameHashCodeAs gene2
                }
            }
            "return different hash codes for different genes" {
                checkAll(doubleGeneArb, doubleGeneArb) { gData1, gData2 ->
                    assume(gData1 != gData2)
                    val gene1 = DoubleGene(gData1.dna, gData1.range)
                    val gene2 = DoubleGene(gData2.dna, gData2.range)
                    gene1 shouldNotBe gene2
                }
            }
        }
    }
    "Verifying a gene" should {
        "return true if the gene is within the range" {
            checkAll(doubleGeneArb) { gData ->
                DoubleGene(gData.dna, gData.range).verify() shouldBe true
            }
        }
        "return false if the gene is outside the range" {
            checkAll(Arb.doubleRange, Arb.long()) { range, seed ->
                val rng = Random(seed)
                val gene = DoubleGene(rng.nextDoubleOutsideOf(range), range)
                gene.verify() shouldBe false
            }
        }
    }
})

private fun ClosedFloatingPointRange<Double>.randomOutside(rng: Random): Double {
    return if (start == Double.NEGATIVE_INFINITY && endInclusive == Double.POSITIVE_INFINITY) {
        Double.NaN
    } else {
        val min = this.start.toDouble()
        val max = this.endInclusive.toDouble()
        val range = max - min
        val outside = if (rng.nextBoolean()) min - range else max + range
        outside
    }
}

private suspend fun checkComparison(
    comparison: (Double, Double, Pair<Double, Double>) -> Unit
) {
    checkAll(Arb.double(), Arb.double(), arbRange) { dna1, dna2, range ->
        assume(dna1 != dna2)
        assume(dna1.isNotNan() && dna2.isNotNan())
        val (lo, hi) = if (dna1 < dna2) dna1 to dna2 else dna2 to dna1
        comparison(lo, hi, range)
    }
}

private suspend fun checkConversion(
    conversion: (Double, Pair<Double, Double>) -> Unit
) {
    checkAll<Double, Double, Double> { dna, d1, d2 ->
        assume(d2 != d1)
        val range = if (d1 > d2) d2 to d1 else d1 to d2
        conversion(dna, range)
    }
}