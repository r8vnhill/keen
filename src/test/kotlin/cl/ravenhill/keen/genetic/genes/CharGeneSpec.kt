package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.util.nextChar
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.char.shouldBeInRange
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.char
import io.kotest.property.arbitrary.pair
import io.kotest.property.assume
import io.kotest.property.checkAll
import kotlin.random.Random


class CharGeneSpec : WordSpec({

    "Comparing" should {
        "return 0 if the genes are equal" {
            checkAll<Char> { dna ->
                CharGene(dna).compareTo(CharGene(dna)) shouldBe 0
            }
        }

        "return a negative number if the first gene is less than the second" {
            checkComparison { lo, hi ->
                CharGene(lo).compareTo(CharGene(hi)) shouldBeLessThan 0
            }
        }

        "return a positive number if the first gene is greater than the second" {
            checkComparison { lo, hi ->
                CharGene(hi).compareTo(CharGene(lo)) shouldBeGreaterThan 0
            }
        }
    }

    "Filtering" should {
        "create a new gene that fulfills the predicate" {
            checkAll<Char> {
                val gene = CharGene.create { it in 'a'..'z' }
                gene.dna shouldBeInRange 'a'..'z'
            }
        }
    }

    "Flattening" should {
        "return a list with the gene's value" {
            checkAll<Char> { dna ->
                CharGene(dna).flatten() shouldBe listOf(dna)
            }
        }
    }

    "Conversions" When {
        "Converting to Char" should {
            "return the gene's dna" {
                checkAll<Char> { dna ->
                    CharGene(dna).toChar() shouldBe dna
                }
            }
        }

        "Converting to Int" should {
            "return the gene's dna as an Int" {
                checkAll<Char> { dna ->
                    CharGene(dna).toInt() shouldBe dna.code
                }
            }
        }
    }
    "Creating a random gene" should {
        "return a gene with a random dna" {
            checkAll<Long> { seed ->
                val random = Random(seed)
                Core.random = Random(seed)
                val gene = CharGene.create()
                gene.dna shouldBe random.nextChar()
            }
        }
    }
    "Genetic operations" When {
        "Duplicating" should {
            "return a new gene with the same dna" {
                checkAll<Char, Char> { original, expected ->
                    CharGene(original).duplicate(expected) shouldBe CharGene(expected)
                }
            }
        }
        "Mutating" should {
            "return a new gene with random dna" {
                checkAll<Char, Long> { dna, seed ->
                    val rng = Random(seed)
                    Core.random = Random(seed)
                    val expected = CharGene(rng.nextChar())
                    CharGene(dna).mutate() shouldBe expected
                }
            }
        }
    }

    "Object identity" When {
        "checking equality" should {
            "return true if the dna of both genes is the same" {
                checkAll<Char> { dna ->
                    CharGene(dna) shouldBe CharGene(dna)
                }
            }

            "return false if the dna of both genes is different" {
                checkAll<Char, Char> { a, b ->
                    assume(a != b)
                    CharGene(a) shouldNotBe CharGene(b)
                }
            }
        }

        "checking hashing" should {
            "return the same hash code for equal genes" {
                checkAll<Char> { dna ->
                    CharGene(dna).hashCode() shouldBe CharGene(dna).hashCode()
                }
            }

            "return different hash codes for different genes" {
                checkAll<Char, Char> { a, b ->
                    assume(a != b)
                    CharGene(a).hashCode() shouldNotBe CharGene(b).hashCode()
                }
            }
        }
    }
})

private suspend fun checkComparison(comparison: (Char, Char) -> Unit) {
    checkAll(Arb.pair(Arb.char(), Arb.char())) { dna ->
        assume(dna.first != dna.second)
        val (lo, hi) = if (dna.first < dna.second) dna else dna.second to dna.first
        comparison(lo, hi)
    }
}