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
import io.kotest.property.arbitrary.long
import io.kotest.property.checkAll

class AverageCrossoverTest : FreeSpec({

    "An [AverageCrossover]" - {
        "when created" - {
            "without explicit [chromosomeRate] defaults to 1.0" {
                checkAll(Arb.probability()) { geneRate ->
                    val crossover = AverageCrossover<Int, IntGene>(geneRate = geneRate)
                    crossover.chromosomeRate shouldBe 1.0
                    crossover.geneRate shouldBe geneRate
                }
            }

            "without explicit [geneRate] defaults to 1.0" {
                checkAll(Arb.probability()) { chromosomeRate ->
                    val crossover = AverageCrossover<Int, IntGene>(chromosomeRate = chromosomeRate)
                    crossover.chromosomeRate shouldBe chromosomeRate
                    crossover.geneRate shouldBe 1.0
                }
            }
        }

        "when combining calculates the average of the genes" {
            val crossover = AverageCrossover<Int, IntGene>()
            val chromosomes = listOf(
                IntChromosome(IntGene(1), IntGene(2), IntGene(3)),
                IntChromosome(IntGene(4), IntGene(5), IntGene(6))
            )
            val result = crossover.combine(chromosomes)
            result shouldBe listOf(IntGene(2), IntGene(3), IntGene(4))
        }
    }
})
