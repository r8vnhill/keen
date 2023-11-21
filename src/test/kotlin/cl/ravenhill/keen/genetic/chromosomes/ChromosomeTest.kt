/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.genetic.TestGene
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.assume
import io.kotest.property.checkAll

class ChromosomeTest : FreeSpec({
    "A [Chromosome]" - {
        "can be created with a list of genes" {
            with(Arb) {
                checkAll(list(int())) { dnaList ->
                    val genes = dnaList.map { TestGene(it) }
                    val chromosome = TestChromosome(genes)
                    assertSoftly {
                        for (i in genes.indices) {
                            chromosome[i].dna shouldBe genes[i].dna
                        }
                    }
                }
            }
        }

        "can return its size" {
            with(Arb) {
                checkAll(testChromosome(int())) { chromosome ->
                    chromosome.size shouldBe chromosome.genes.size
                }
            }
        }

        "iterator can be accessed" {
            with(Arb) {
                checkAll(testChromosome(int())) { chromosome ->
                    assertSoftly {
                        chromosome.iterator().withIndex().forEach { (i, gene) ->
                            gene.dna shouldBe chromosome[i].dna
                        }
                    }
                }
            }
        }

        "can access genes by index" {
            with(Arb) {
                checkAll(testChromosome(int())) { chromosome ->
                    assertSoftly {
                        chromosome.genes.forEachIndexed { i, gene ->
                            chromosome[i].dna shouldBe gene.dna
                        }
                    }
                }
            }
        }

        "can be flattened" {
            with(Arb) {
                checkAll(testChromosome(int())) { chromosome ->
                    chromosome.flatMap() shouldBe chromosome.genes.map { it.dna }
                }
            }
        }

        "can verify if a given gene sequence is valid when" - {
            with(Arb) {
                "the list of genes is not empty" {
                    checkAll(testChromosome(int())) { chromosome ->
                        assume { chromosome.genes.shouldNotBeEmpty() }
                        chromosome.verify().shouldBeTrue()
                    }
                }

                "the list of genes is empty" {
                    TestChromosome(listOf()).verify().shouldBeTrue()
                }
            }
        }
    }
})

private fun Arb.Companion.testChromosome(i: Arb<Int>) = arbitrary {
    TestChromosome(list(i).bind().map { TestGene(it) })
}

/**
 * Represents a chromosome containing a list of [TestGene] instances.
 *
 * Each chromosome represents a collection of genes, which together can be seen as a solution or individual within an evolutionary algorithm.
 * This is a private test implementation of the [Chromosome] interface specific to [TestGene].
 *
 * @property genes The list of genes contained within this chromosome.
 */
private class TestChromosome(override val genes: List<TestGene>) : Chromosome<Int, TestGene> {

    /**
     * Creates a new instance of [TestChromosome] with the provided genes.
     *
     * @param genes The list of genes for the new chromosome instance.
     * @return A new [TestChromosome] instance with the specified genes.
     */
    override fun withGenes(genes: List<TestGene>) = TestChromosome(genes)
}
