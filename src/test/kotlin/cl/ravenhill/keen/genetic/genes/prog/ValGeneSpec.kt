package cl.ravenhill.keen.genetic.genes.prog

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.genes.doubleGene
import cl.ravenhill.keen.genetic.genes.intGene
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.util.math.isNotNan
import cl.ravenhill.keen.util.nextDoubleOutsideOf
import cl.ravenhill.keen.util.nextIntOutsideOf
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.haveSameHashCodeAs
import io.kotest.matchers.types.shouldHaveSameHashCodeAs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.long
import io.kotest.property.assume
import io.kotest.property.checkAll
import kotlin.random.Random


class ValGeneSpec : WordSpec({
    "Creating a ValGene" should {
        "return a ValGene with an IntGene if the value is an Int" {
            checkAll(Arb.intGene()) { gene ->
                val g = ValGene(gene.toIntGene())
                g shouldBe ValGene(gene.toIntGene())
            }
        }
        "return a ValGene with a DoubleGene if the value is a Double" {
            checkAll(Arb.doubleGene()) { gene ->
                assume(gene.dna.isNotNan())
                val g = ValGene(gene.toDoubleGene())
                g shouldBe ValGene(gene.toDoubleGene())
            }
        }
    }
    "Genetic operations" When {
        "Mutating" should {
            "return a ValGene with a mutated IntGene if the value is an Int" {
                checkAll(Arb.intGene(), Arb.long()) { gene, seed ->
                    val rng = Random(seed)
                    Core.rng = Random(seed)
                    val expected = rng.nextInt(gene.range.first, gene.range.second)
                    ValGene(gene.toIntGene()).mutate() shouldBe
                            ValGene(IntGene(expected, gene.range))
                }
            }
            "return a ValGene with a mutated DoubleGene if the value is a Double" {
                checkAll(Arb.doubleGene(), Arb.long()) { gene, seed ->
                    assume(gene.dna.isNotNan())
                    val rng = Random(seed)
                    Core.rng = Random(seed)
                    val expected = rng.nextDouble(gene.range.first, gene.range.second)
                    ValGene(gene.toDoubleGene()).mutate() shouldBe
                            ValGene(DoubleGene(expected, gene.range))
                }
            }
        }
        "Duplicating" should {
            "return a ValGene with a given IntGene if the value is an Int" {
                checkAll(Arb.intGene(), Arb.intGene()) { gene1, gene2 ->
                    val g = ValGene(gene1.toIntGene())
                    g.duplicate(gene2.toIntGene()) shouldBe ValGene(gene2.toIntGene())
                }
            }
            "return a ValGene with a given DoubleGene if the value is a Double" {
                checkAll(Arb.doubleGene(), Arb.doubleGene()) { gene1, gene2 ->
                    assume(gene1.dna.isNotNan())
                    assume(gene2.dna.isNotNan())
                    val g = ValGene(gene1.toDoubleGene())
                    g.duplicate(gene2.toDoubleGene()) shouldBe ValGene(gene2.toDoubleGene())
                }
            }
        }
    }
    "Flattening" should {
        "return a list with the IntGene if the value is an Int" {
            checkAll(Arb.intGene()) { gene ->
                val g = ValGene(gene.toIntGene())
                g.flatten() shouldBe listOf(gene.toIntGene())
            }
        }
        "return a list with the DoubleGene if the value is a Double" {
            checkAll(Arb.doubleGene()) { gene ->
                assume(gene.dna.isNotNan())
                val g = ValGene(gene.toDoubleGene())
                g.flatten() shouldBe listOf(gene.toDoubleGene())
            }
        }
    }
    "Object identity" When {
        "Checking equality" should {
            "return true if the value is an Int and the IntGene is equal" {
                checkAll(Arb.intGene()) { gene ->
                    val g = ValGene(gene.toIntGene())
                    g shouldBe ValGene(gene.toIntGene())
                }
            }
            "return false if the value is an Int and the IntGene is not equal" {
                checkAll(Arb.intGene(), Arb.intGene()) { gene1, gene2 ->
                    assume(gene1.value != gene2.value)
                    val g = ValGene(gene1.toIntGene())
                    g shouldNotBe ValGene(gene2.toIntGene())
                }
            }
            "return true if the value is a Double and the DoubleGene is equal" {
                checkAll(Arb.doubleGene()) { gene ->
                    assume(gene.dna.isNotNan())
                    val g = ValGene(gene.toDoubleGene())
                    g shouldBe ValGene(gene.toDoubleGene())
                }
            }
            "return false if the value is a Double and the DoubleGene is not equal" {
                checkAll(Arb.doubleGene(), Arb.doubleGene()) { gene1, gene2 ->
                    assume(gene1.dna != gene2.dna)
                    val g = ValGene(gene1.toDoubleGene())
                    g shouldNotBe ValGene(gene2.toDoubleGene())
                }
            }
        }
        "Calculating the hash code" should {
            "return the same value if the value is an Int and the IntGene is equal" {
                checkAll(Arb.intGene()) { gene ->
                    val g = ValGene(gene.toIntGene())
                    g shouldHaveSameHashCodeAs ValGene(gene.toIntGene())
                }
            }
            "return a different value if the value is an Int and the IntGene is not equal" {
                checkAll(Arb.intGene(), Arb.intGene()) { gene1, gene2 ->
                    assume(gene1.value != gene2.value)
                    val g = ValGene(gene1.toIntGene())
                    g shouldNot haveSameHashCodeAs(ValGene(gene2.toIntGene()))
                }
            }
            "return the same value if the value is a Double and the DoubleGene is equal" {
                checkAll(Arb.doubleGene()) { gene ->
                    assume(gene.dna.isNotNan())
                    val g = ValGene(gene.toDoubleGene())
                    g shouldHaveSameHashCodeAs ValGene(gene.toDoubleGene())
                }
            }
            "return a different value if the value is a Double and the DoubleGene is not equal" {
                checkAll(Arb.doubleGene(), Arb.doubleGene()) { gene1, gene2 ->
                    assume(gene1.dna != gene2.dna)
                    val g = ValGene(gene1.toDoubleGene())
                    g shouldNot haveSameHashCodeAs(ValGene(gene2.toDoubleGene()))
                }
            }
        }
    }
    "Reducing" should {
        "return the evaluated value if the value is an Int" {
            checkAll(Arb.intGene()) { gene ->
                val g = ValGene(gene.toIntGene())
                g.reduce() shouldBe gene.value
            }
        }
        "return the evaluated value if the value is a Double" {
            checkAll(Arb.doubleGene()) { gene ->
                assume(gene.dna.isNotNan())
                val g = ValGene(gene.toDoubleGene())
                g.reduce() shouldBe gene.dna
            }
        }
    }
    "Verifying" should {
        "return true if the value is an Int and the IntGene is valid" {
            checkAll<Int> { value ->
                assume(value < Int.MAX_VALUE)
                val gene = ValGene(IntGene(value, Int.MIN_VALUE to Int.MAX_VALUE))
                gene.verify() shouldBe true
            }
        }
        "return false if the value is an Int and the IntGene is not valid" {
            checkAll(
                Arb.int(-100_000, 100_000),
                Arb.int(-100_000, 100_000),
                Arb.long()
            ) { i1, i2, seed ->
                assume(i1 != i2)
                val (start, end) = if (i1 < i2) i1 to i2 else i2 to i1
                val value = Random(seed).nextIntOutsideOf(start to end)
                val gene = ValGene(IntGene(value, start to end))
                gene.verify() shouldBe false
            }
        }
        "return true if the value is a Double and the DoubleGene is valid" {
            checkAll<Double> { value ->
                assume(value.isFinite())
                val gene =
                    ValGene(
                        DoubleGene(
                            value,
                            Double.NEGATIVE_INFINITY to Double.POSITIVE_INFINITY
                        )
                    )
                gene.verify() shouldBe true
            }
        }
        "return false if the value is a Double and the DoubleGene is not valid" {
            checkAll(
                Arb.double(-100_000.0, 100_000.0),
                Arb.double(-100_000.0, 100_000.0),
                Arb.long()
            ) { d1, d2, seed ->
                assume(d1 != d2)
                assume(d1.isFinite())
                assume(d2.isFinite())
                val (start, end) = if (d1 < d2) d1 to d2 else d2 to d1
                val value = Random(seed).nextDoubleOutsideOf(start to end)
                val gene =
                    ValGene(DoubleGene(value, start to end))
                gene.verify() shouldBe false
            }
        }
    }
})