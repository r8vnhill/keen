package cl.ravenhill.keen.core

import cl.ravenhill.keen.core.genes.BoolGene
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.haveSameHashCodeAs
import io.kotest.matchers.types.shouldHaveSameHashCodeAs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.element
import io.kotest.property.checkAll
import java.util.Random
import kotlin.random.asKotlinRandom


class BoolGeneSpec : WordSpec({

    "Converting to Boolean" should {
        "return true if the gene is True" {
            BoolGene.True.toBool() shouldBe true
        }

        "return false if the gene is False" {
            BoolGene.False.toBool() shouldBe false
        }
    }

    "Converting to Int" should {
        "return 1 if the gene is True" {
            BoolGene.True.toInt() shouldBe 1
        }

        "return 0 if the gene is False" {
            BoolGene.False.toInt() shouldBe 0
        }
    }

    "Mutating" should {
        "return a random gene with a given seed" {
            checkAll<Long> { seed ->
                val gene = BoolGene.True
                KeenCore.generator = Random(seed).asKotlinRandom()
                val mutated = gene.mutate()
                KeenCore.generator = Random(seed).asKotlinRandom()
                mutated.toBool() shouldBe KeenCore.generator.nextBoolean()
            }
        }
    }

    "Creating a new gene" should {
        "return a True gene if the dna is true" {
            checkNewGenes(true, BoolGene.True)
        }

        "return a False gene if the dna is false" {
            checkNewGenes(false, BoolGene.False)
        }
    }

    "A true gene" When {
        "obtaining it's dna" should {
            "return true" {
                BoolGene.True.dna shouldBe true
            }
        }

        "checking equality" should {
            "be equal to another true gene" {
                BoolGene.True shouldBe BoolGene.True
            }

            "be different to a false gene" {
                BoolGene.True shouldNotBe BoolGene.False
            }
        }

        "obtaining it's hash code" should {
            "be equal to another true gene" {
                BoolGene.True shouldHaveSameHashCodeAs BoolGene.True
            }

            "be different to a false gene" {
                BoolGene.True.hashCode() shouldNot haveSameHashCodeAs(BoolGene.False.hashCode())
            }
        }
    }

    "A false gene" When {
        "obtaining it's dna" should {
            "return false" {
                BoolGene.False.dna shouldBe false
            }
        }

        "checking equality" should {
            "be equal to another false gene" {
                BoolGene.False shouldBe BoolGene.False
            }

            "be different to a true gene" {
                BoolGene.False shouldNotBe BoolGene.True
            }
        }

        "obtaining it's hash code" should {
            "be equal to another false gene" {
                BoolGene.False shouldHaveSameHashCodeAs BoolGene.False
            }

            "be different to a true gene" {
                BoolGene.False.hashCode() shouldNot haveSameHashCodeAs(BoolGene.True.hashCode())
            }
        }
    }
})

suspend fun checkNewGenes(dna: Boolean, gene: BoolGene) =
    checkAll(Arb.element(BoolGene.True, BoolGene.False)) { g ->
        val newGene = g.new(dna)
        newGene shouldBe gene
    }
