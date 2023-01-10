package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.*
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.checkAll


class GenotypeSpec : WordSpec({
    "Genotype Factory" When {
        "adding a chromosome factory" should {
            "initialize a new list if it has not been initialized" {
                checkAll(Arb.intChromosomeFactory()) { arbFactory ->
                    val factory = Genotype.Factory<Int>()
                    shouldThrow<UninitializedPropertyAccessException> {
                        factory.chromosomes
                    }
                    factory.chromosome { arbFactory }
                    factory.chromosomes.size shouldBe 1
                }
            }
            "add a new chromosome factory to the list" {
                checkAll(Arb.list(Arb.intChromosomeFactory(), 1..100)) { arbFactories ->
                    val factory = Genotype.Factory<Int>()
                    arbFactories.forEach { factory.chromosome { it } }
                    factory.chromosomes.size shouldBe arbFactories.size
                }
            }
        }
        "making a genotype" should {
            "return a genotype with the given chromosome factories" {
                checkAll(Arb.list(Arb.intChromosomeFactory(), 1..100)) { arbFactories ->
                    val factory = Genotype.Factory<Int>()
                    arbFactories.forEach { factory.chromosome { it } }
                    val genotype = factory.make()
                    genotype.chromosomes.size shouldBe arbFactories.size
                }
            }
            "throw an exception if  the chromosome factories are not initialized" {
                val factory = Genotype.Factory<Int>()
                shouldThrow<UnfulfilledContractException> {
                    factory.make()
                }.violations.first() shouldBeOfClass UnfulfilledClauseException::class
            }
        }
    }
    "Accessing by index" should {
        "return the chromosome at the given index" {
            checkAll(Arb.genotype(Arb.intChromosomeFactory())) { genotype ->
                genotype.chromosomes.forEachIndexed { index, chromosome ->
                    genotype[index] shouldBe chromosome
                }
            }
        }
        "throw an exception if the index is out of bounds" {
            checkAll(
                Arb.genotype(Arb.intChromosomeFactory()),
                Arb.intOutsideRange(0..100)
            ) { genotype, index ->
                shouldThrow<UnfulfilledContractException> {
                    genotype[index]
                }.violations.first() shouldBeOfClass IntClauseException::class
            }
        }
    }
    "Convert to sequence" should {
        "return a sequence with the chromosomes of the genotype" {
            checkAll(Arb.genotype(Arb.intChromosomeFactory())) { genotype ->
                genotype.sequence().forEachIndexed { index, chromosome ->
                    genotype.chromosomes[index] shouldBe chromosome
                }
            }
        }
    }
    "Duplicating" should {
        "return a new genotype with the given chromosomes" {
            checkAll(
                Arb.genotype(Arb.intChromosomeFactory()),
                Arb.genotype(Arb.intChromosomeFactory())
            ) { genotype1, genotype2 ->
                val new = genotype1.duplicate(genotype2.chromosomes)
                new.chromosomes.size shouldBe genotype2.chromosomes.size
                (new.chromosomes zip genotype2.chromosomes).forEach { (c1, c2) ->
                    c1 shouldBe c2
                }
            }
        }
    }
    "Flattening" should {
        "return a flat list of the underlying genes" {
            checkAll(Arb.list(Arb.intChromosomeFactory(), 1..100)) { arbFactories ->
                val factory = Genotype.Factory<Int>()
                arbFactories.forEach { factory.chromosome { it } }
                val genotype = factory.make()
                genotype.flatten().size shouldBe genotype.chromosomes.sumOf { it.size }
                genotype.flatten() shouldBe genotype.chromosomes.flatMap { it.flatten() }
            }
        }
    }
    "Size" should {
        "be equal to the number of chromosomes on the genotype" {
            checkAll(Arb.genotype(Arb.intChromosomeFactory())) { genotype ->
                genotype.size shouldBe genotype.chromosomes.size
            }
        }
    }
    "Verifying" should {
        "return true if the genotype is valid" {
            checkAll(
                Arb.genotype(Arb.intChromosomeFactory())
            ) { genotype ->
                genotype.verify() shouldBe true
            }
        }
    }
})

/**
 * Generates an [Arb]itrary [Int] value outside the given ``intRange``.
 *
 * Behaviour when the given range encompasses the whole [Int] range is undefined.
 */
private fun Arb.Companion.intOutsideRange(intRange: IntRange) = arbitrary {
    if (intRange.first == Int.MIN_VALUE) {
        int(intRange.last + 1, Int.MAX_VALUE).bind()
    } else {
        int(Int.MIN_VALUE, intRange.first - 1).bind()
    }
}

/**
 * Generates a new [Arb]itrary [Genotype] using a given arbitrary [Chromosome] factory.
 */
private fun <T> Arb.Companion.genotype(chromosome: Arb<Chromosome.Factory<T>>) =
    arbitrary {
        val chromosomes = Arb.list(chromosome, 1..1000).bind()
        Genotype.Factory<T>().apply {
            chromosomes.forEach {
                chromosome { it }
            }
        }.make()
    }

private fun Arb.Companion.intChromosomeFactory() = arbitrary {
    IntChromosome.Factory().apply {
        size = Arb.positiveInt(100).bind()
        range = orderedIntPair().bind()
    }
}