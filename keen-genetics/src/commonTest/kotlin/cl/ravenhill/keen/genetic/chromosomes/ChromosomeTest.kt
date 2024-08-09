/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.genetic.genes.SimpleGene
import cl.ravenhill.keen.genetic.genes.arbSimpleGene
import cl.ravenhill.keen.genetics.chromosomes.Chromosome
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.utils.arbProbability
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeIn
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
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
    }
})

fun <T, G> arbChromosome(
    gene: Arb<G>,
    size: IntRange = 0..100
): Arb<Chromosome<T, G>> where G : Gene<T, G> = Arb.list(gene, size).map {
    object : Chromosome<T, G> {
        override val genes: List<G> = it
    }
}

private fun arbChromosomeWithInvalidGenes(size: IntRange = 1..100, isValid: Arb<Boolean> = Arb.constant(true)) =
    arbitrary {
        val genes = mutableListOf<SimpleGene>()
        repeat(Arb.int(size).bind()) {
            genes.add(arbSimpleGene(isValid).bind())
        }
        genes.add(arbSimpleGene(Arb.constant(false)).bind())
        object : Chromosome<Int, SimpleGene> {
            override val genes: List<SimpleGene> = genes
        }
    }

private fun <T, G> arbChromosomeAndGene(
    gene: Arb<G>,
    size: IntRange = 1..100
): Arb<Pair<Chromosome<T, G>, G>> where G : Gene<T, G> = Arb.list(gene, size).map { genes ->
    object : Chromosome<T, G> {
        override val genes: List<G> = genes
    } to genes.random()
}

private fun <T, G> arbChromosomeAndGenes(
    gene: Arb<G>,
    size: IntRange = 1..100,
    probability: Arb<Double> = arbProbability()
): Arb<Pair<Chromosome<T, G>, List<G>>> where G : Gene<T, G> = arbitrary { (random, seed) ->
    val genes = Arb.list(gene, size).bind()
    val ratio = probability.bind()
    object : Chromosome<T, G> {
        override val genes: List<G> = genes
    } to genes.filter { random.nextDouble() < ratio }
}

private fun <T, G> arbChromosomeAndNotContainedGenes(
    gene: Arb<G>,
    size: IntRange = 0..100
): Arb<Pair<Chromosome<T, G>, List<G>>> where G : Gene<T, G> = arbitrary {
    val genes = Arb.list(gene, size).bind()
    var notContained: List<G>
    do {
        notContained = Arb.list(gene, 1..100).bind().filter { it !in genes }
    } while (notContained.isEmpty())
    object : Chromosome<T, G> {
        override val genes: List<G> = genes
    } to notContained
}

private fun <T, G> arbChromosomeAndSize(
    gene: Arb<G>,
    size: IntRange = 0..100
): Arb<Pair<Chromosome<T, G>, Int>> where G : Gene<T, G> = Arb.list(gene, size).map { genes ->
    object : Chromosome<T, G> {
        override val genes: List<G> = genes
    } to genes.size
}

class SimpleChromosome(override val genes: List<SimpleGene>) : Chromosome<Int, SimpleGene>
