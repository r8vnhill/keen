/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetics.chromosomes

import cl.ravenhill.keen.exceptions.InvalidIndexException
import cl.ravenhill.keen.genetics.genes.SimpleGene
import cl.ravenhill.keen.genetics.genes.arbSimpleGene
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.matchers.shouldBeLeft
import cl.ravenhill.matchers.shouldBeRight
import cl.ravenhill.matchers.shouldContainExceptionOfType
import cl.ravenhill.utils.arbProbability
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeIn
import io.kotest.matchers.ints.shouldNotBeInRange
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import io.kotest.property.assume
import io.kotest.property.checkAll

@OptIn(ExperimentalKotest::class)
class ChromosomeTest : FreeSpec({
    "A Chromosome" - {
        "should have a size property that is equal to the size of the list of genes" {
            checkAll(arbChromosomeAndSize(arbSimpleGene())) { (chromosome, size) ->
                chromosome.size shouldBe size
            }
        }

        "when testing for emptiness" - {
            "should return true if the chromosome is empty" {
                SimpleChromosome(emptyList()).shouldBeEmpty()
            }

            "should return false if the chromosome is not empty" {
                checkAll(arbChromosome(arbSimpleGene(), 1..100)) { chromosome ->
                    chromosome.isEmpty() shouldBe false
                }
            }
        }

        "when iterating over its genes" - {
            "should return the same genes as the list" {
                checkAll(arbChromosome(arbSimpleGene())) { chromosome ->
                    chromosome.iterator().asSequence().toList() shouldBe chromosome.genes
                }
            }

            "should return the same genes as the list using forEach" {
                checkAll(arbChromosome(arbSimpleGene())) { chromosome ->
                    val genes = mutableListOf<SimpleGene>()
                    chromosome.forEach { genes.add(it) }
                    genes shouldBe chromosome.genes
                }
            }
        }

        "when testing for containment" - {
            "should return true if the gene is in the chromosome" {
                checkAll(
                    PropTestConfig(iterations = 250),
                    arbChromosomeAndGene(arbSimpleGene(), 1..25)
                ) { (chromosome, gene) ->
                    (gene in chromosome).shouldBeTrue()
                }
            }

            "should return false if the gene is not in the chromosome" {
                checkAll(
                    PropTestConfig(iterations = 250),
                    Arb.list(arbSimpleGene(), 1..25), arbSimpleGene()
                ) { genes, notContained ->
                    assume { notContained shouldNotBeIn genes }
                    SimpleChromosome(genes).contains(notContained).shouldBeFalse()
                }
            }

            "should return true if a subset of genes is in the chromosome" {
                checkAll(arbChromosomeAndGenes(arbSimpleGene())) { (chromosome, subset) ->
                    chromosome.containsAll(subset).shouldBeTrue()
                }
            }

            "should return false if a subset of genes is not in the chromosome" {
                checkAll(arbChromosomeAndNotContainedGenes(arbSimpleGene())) { (chromosome, notContained) ->
                    chromosome.containsAll(notContained).shouldBeFalse()
                }
            }
        }

        "can be flattened" {
            checkAll(arbChromosome(arbSimpleGene())) { chromosome ->
                chromosome.flatten() shouldBe chromosome.genes.map { it.value }
            }
        }

        "when verifying" - {
            "should return true if all genes are valid" {
                checkAll(arbChromosome(arbSimpleGene())) { chromosome ->
                    chromosome.verify().shouldBeTrue()
                }
            }

            "should return false if any gene is invalid" {
                checkAll(arbChromosomeWithInvalidGenes()) { chromosome ->
                    chromosome.verify().shouldBeFalse()
                }
            }
        }

        "when folding" - {
            "should correctly accumulate results from left to right" {
                checkAll(Arb.list(Arb.int())) { values ->
                    val chromosome = SimpleChromosome(values.map { SimpleGene(it) })

                    // Fold to calculate the sum of gene values
                    val sum = chromosome.fold(0) { acc, value -> acc + value }
                    val expectedSum = values.sum()
                    sum shouldBe expectedSum

                    // Fold to concatenate gene values as a string
                    val concatenated = chromosome.fold("") { acc, value -> acc + value.toString() }
                    val expectedConcat = values.joinToString("")
                    concatenated shouldBe expectedConcat
                }
            }

            "should correctly accumulate results from right to left" {
                checkAll(Arb.list(Arb.int())) { values ->
                    val chromosome = SimpleChromosome(values.map { SimpleGene(it) })

                    // FoldRight to calculate the sum of gene values
                    val sum = chromosome.foldRight(0) { value, acc -> acc + value }
                    val expectedSum = values.sum()
                    sum shouldBe expectedSum

                    // FoldRight to build a string with gene values in reverse order
                    val reversedString = chromosome.foldRight("") { value, acc -> acc + value.toString() }
                    val expectedReverse = values.reversed().joinToString("")
                    reversedString shouldBe expectedReverse
                }
            }
        }

        "when accessing genes by index" - {
            "should return a Left value if the index is out of bounds" {
                checkAll(arbChromosome(arbSimpleGene()), Arb.int()) { chromosome, index ->
                    assume { index shouldNotBeInRange chromosome.indices }
                    chromosome[index]
                        .shouldBeLeft()
                        .shouldContainExceptionOfType<InvalidIndexException>(
                            "Index ($index) must be within the bounds of the chromosome [0, ${chromosome.size - 1}]"
                        )
                }
            }

            "should return a Right value if the index is within bounds" {
                checkAll(arbChromosomeAndGeneAtIndex(arbSimpleGene())) { (chromosome, gene, index) ->
                    chromosome[index]
                        .shouldBeRight()
                        .getOrNull()
                        .shouldNotBeNull()
                        .shouldBe(gene)
                }
            }
        }
    }
})

fun <T, G> arbChromosome(
    gene: Arb<G>,
    size: IntRange = 0..10,
): Arb<Chromosome<T, G>> where G : Gene<T, G> = Arb.list(gene, size).map {
    object : Chromosome<T, G> {
        override val genes: List<G> = it
        override fun copyWithGenes(newGenes: List<G>): Chromosome<T, G> {
            TODO("Not yet implemented")
        }

        override fun toString() = "Chromosome(genes=$genes)"
    }
}

fun arbChromosomeWithInvalidGenes(
    size: IntRange = 1..100,
    isValid: Arb<Boolean> = Arb.constant(true),
): Arb<Chromosome<Int, SimpleGene>> =
    arbitrary {
        val genes = mutableListOf<SimpleGene>()
        repeat(Arb.int(size).bind()) {
            genes.add(arbSimpleGene(isValid).bind())
        }
        genes.add(arbSimpleGene(Arb.constant(false)).bind())
        object : Chromosome<Int, SimpleGene> {
            override val genes: List<SimpleGene> = genes
            override fun copyWithGenes(newGenes: List<SimpleGene>): Chromosome<Int, SimpleGene> {
                TODO("Not yet implemented")
            }
        }
    }

private fun <T, G> arbChromosomeAndGene(
    gene: Arb<G>,
    size: IntRange = 1..100,
): Arb<Pair<Chromosome<T, G>, G>> where G : Gene<T, G> = Arb.list(gene, size).map { genes ->
    object : Chromosome<T, G> {
        override val genes: List<G> = genes
        override fun copyWithGenes(newGenes: List<G>): Chromosome<T, G> {
            TODO("Not yet implemented")
        }
    } to genes.random()
}

/**
 * Generates an arbitrary `Chromosome` along with a specific gene at a random index within the chromosome.
 *
 * @param gene An [Arb] generator for producing individual genes.
 * @param size An optional `IntRange` specifying the possible sizes of the chromosomes. The default is 1 to 100.
 * @return An [Arb] generator that produces triples containing a chromosome, a randomly selected gene from that
 *   chromosome, and the index of the gene.
 */
private fun <T, G> arbChromosomeAndGeneAtIndex(
    gene: Arb<G>,
    size: IntRange = 1..100,
): Arb<Triple<Chromosome<T, G>, G, Int>> where G : Gene<T, G> = Arb.list(gene, size).map { genes ->
    Arb.int(genes.indices).map { index ->
        Triple(object : Chromosome<T, G> {
            override val genes: List<G> = genes
            override fun copyWithGenes(newGenes: List<G>): Chromosome<T, G> {
                TODO("Not yet implemented")
            }
        }, genes[index], index)
    }.next()
}

private fun <T, G> arbChromosomeAndGenes(
    gene: Arb<G>,
    size: IntRange = 1..100,
    probability: Arb<Double> = arbProbability(),
): Arb<Pair<Chromosome<T, G>, List<G>>> where G : Gene<T, G> = arbitrary { (random, seed) ->
    val genes = Arb.list(gene, size).bind()
    val ratio = probability.bind()
    object : Chromosome<T, G> {
        override val genes: List<G> = genes
        override fun copyWithGenes(newGenes: List<G>): Chromosome<T, G> {
            TODO("Not yet implemented")
        }
    } to genes.filter { random.nextDouble() < ratio }
}

private fun arbChromosomeAndNotContainedGenes(
    gene: Arb<SimpleGene>,
    size: IntRange = 0..100,
): Arb<Pair<SimpleChromosome, List<SimpleGene>>> = arbitrary {
    val genes = Arb.list(gene, size).bind()
    var notContained: List<SimpleGene>
    do {
        notContained = Arb.list(gene, 1..100).bind().filter { it !in genes }
    } while (notContained.isEmpty())
    SimpleChromosome(genes) to notContained
}

private fun <T, G> arbChromosomeAndSize(
    gene: Arb<G>,
    size: IntRange = 0..100,
): Arb<Pair<Chromosome<T, G>, Int>> where G : Gene<T, G> = Arb.list(gene, size).map { genes ->
    object : Chromosome<T, G> {
        override val genes: List<G> = genes
        override fun copyWithGenes(newGenes: List<G>): Chromosome<T, G> {
            TODO("Not yet implemented")
        }
    } to genes.size
}

class SimpleChromosome(override val genes: List<SimpleGene>) : Chromosome<Int, SimpleGene> {
    override fun copyWithGenes(newGenes: List<SimpleGene>): Chromosome<Int, SimpleGene> {
        TODO("Not yet implemented")
    }
}
