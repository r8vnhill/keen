/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.crossover.combination

import cl.ravenhill.keen.arbs.probability
import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.checkAll

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
        }
    }
})
