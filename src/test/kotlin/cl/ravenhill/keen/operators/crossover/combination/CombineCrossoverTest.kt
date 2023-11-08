/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.crossover.combination

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.arbs.probability
import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.long
import io.kotest.property.checkAll
import kotlin.random.Random

class CombineCrossoverTest : FreeSpec({

    "A [CombineCrossover] operator" - {
        "when created" - {
            "should have the given parameters" {
                checkAll(Arb.probability(), Arb.probability()) { chromosomeRate, geneRate ->
                    val combiner: (List<IntGene>) -> IntGene = { genes -> IntGene(genes.sumOf { it.dna }) }
                    val crossover = CombineCrossover(combiner, chromosomeRate, geneRate)
                    crossover.combiner shouldBe combiner
                    crossover.chromosomeRate shouldBe chromosomeRate
                    crossover.geneRate shouldBe geneRate
                }
            }
        }

        "when combining a list of chromosomes" - {
            "should always return the first chromosome if the [geneRate] is 0" {
                checkAll(Arb.probability()) { chromosomeRate ->
                    val combiner: (List<IntGene>) -> IntGene = { genes -> IntGene(genes.sumOf { it.dna }) }
                    val crossover = CombineCrossover(combiner, chromosomeRate, 0.0)
                    val chromosomes = listOf(
                        IntChromosome(IntGene(1), IntGene(2), IntGene(3)),
                        IntChromosome(IntGene(4), IntGene(5), IntGene(6))
                    )
                    val result = crossover.combine(chromosomes)
                    result shouldBe chromosomes[0].genes
                }
            }

            "should apply the combiner to all genes if [geneRate] is 1" {
                checkAll(Arb.probability()) { chromosomeRate ->
                    val combiner: (List<IntGene>) -> IntGene = { genes -> IntGene(genes.sumOf { it.dna }) }
                    val crossover = CombineCrossover(combiner, chromosomeRate, 1.0)
                    val chromosomes = listOf(
                        IntChromosome(IntGene(1), IntGene(2), IntGene(3)),
                        IntChromosome(IntGene(4), IntGene(5), IntGene(6))
                    )
                    val result = crossover.combine(chromosomes)
                    result shouldBe listOf(IntGene(5), IntGene(7), IntGene(9))
                }
            }

            "should apply the combiner to some genes if [geneRate] is between 0 and 1" {
                checkAll(Arb.probability(), Arb.probability(), Arb.long()) { chromosomeRate, geneRate, seed ->
                    val combiner: (List<IntGene>) -> IntGene = { genes -> IntGene(genes.sumOf { it.dna }) }
                    val crossover = CombineCrossover(combiner, chromosomeRate, geneRate)
                    val chromosomes = listOf(
                        IntChromosome(IntGene(1), IntGene(2), IntGene(3)),
                        IntChromosome(IntGene(4), IntGene(5), IntGene(6))
                    )
                    Core.random = Random(seed)
                    val result = crossover.combine(chromosomes)
                    val random = Random(seed)
                    val expected = List(3) { i ->
                        if (random.nextDouble() < geneRate) {
                            IntGene(chromosomes[0][i].dna + chromosomes[1][i].dna)
                        } else {
                            chromosomes[0][i]
                        }
                    }
                    result shouldBe expected
                }
            }
        }

        "when crossing over chromosomes" - {
            "should combine the genes into a single chromosome" {
                checkAll(Arb.probability(), Arb.probability(), Arb.long()) { chromosomeRate, geneRate, seed ->
                    val combiner: (List<IntGene>) -> IntGene = { genes -> IntGene(genes.sumOf { it.dna }) }
                    val crossover = CombineCrossover(combiner, chromosomeRate, geneRate)
                    val chromosomes = listOf(
                        IntChromosome(IntGene(1), IntGene(2), IntGene(3)),
                        IntChromosome(IntGene(4), IntGene(5), IntGene(6))
                    )
                    Core.random = Random(seed)
                    val result = crossover.crossoverChromosomes(chromosomes)
                    val random = Random(seed)
                    val expected = List(3) { i ->
                        if (random.nextDouble() < geneRate) {
                            IntGene(chromosomes[0][i].dna + chromosomes[1][i].dna)
                        } else {
                            chromosomes[0][i]
                        }
                    }
                    result shouldBe listOf(chromosomes[0].withGenes(expected))
                }
            }
        }
    }
})
