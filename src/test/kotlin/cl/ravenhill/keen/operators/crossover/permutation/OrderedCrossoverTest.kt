package cl.ravenhill.keen.operators.crossover.permutation

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.arbs.datatypes.probability
import cl.ravenhill.keen.arbs.operators.orderedCrossover
import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.checkAll
import kotlin.random.Random

class OrderedCrossoverTest : FreeSpec({

    val parents = listOf(
        IntGene(1),
        IntGene(2),
        IntGene(3),
        IntGene(4),
        IntGene(5),
        IntGene(6),
        IntGene(7),
        IntGene(8),
        IntGene(9)
    ) to listOf(
        IntGene(5),
        IntGene(7),
        IntGene(4),
        IntGene(9),
        IntGene(1),
        IntGene(3),
        IntGene(6),
        IntGene(2),
        IntGene(8)
    )

    "An Ordered Crossover operator" - {
        "default settings" - {
            "should set chromosome rate to 1.0" {
                checkAll(Arb.probability()) { probability ->
                    val crossover = OrderedCrossover<Nothing, NothingGene>(probability)
                    crossover.chromosomeRate shouldBe 1.0
                    crossover.probability shouldBe probability
                }
            }
        }

        "crossover operation" - {
            "when applied to two gene lists" - {
                "should correctly crossover genes at specified points" {
                    checkAll(Arb.orderedCrossover<Int, IntGene>()) { crossover ->
                        crossover.crossoverGenes(parents, 3..5) shouldBe listOf(
                            IntGene(7),
                            IntGene(9),
                            IntGene(1),
                            IntGene(4),
                            IntGene(5),
                            IntGene(6),
                            IntGene(3),
                            IntGene(2),
                            IntGene(8)
                        )
                    }
                }

                "in edge cases" - {
                    "should return the first parent if the crossover region is the complete range" {
                        checkAll(Arb.orderedCrossover<Int, IntGene>()) { crossover ->
                            crossover.crossoverGenes(parents, 0..8) shouldBe parents.first
                        }
                    }

                    "should return the second parent if the crossover region is empty" {
                        checkAll(Arb.orderedCrossover<Int, IntGene>()) { crossover ->
                            crossover.crossoverGenes(parents, 0..<1) shouldBe parents.second
                        }
                    }
                }
            }

            "when applied to a pair of chromosomes" - {
                "should correctly cross genes with a random crossing region" {
                    val crossover = OrderedCrossover<Int, IntGene>(1.0, 1.0)
                    Core.random = Random(11)
                    crossover.crossoverChromosomes(
                        listOf(
                            IntChromosome(parents.first),
                            IntChromosome(parents.second)
                        )
                    ) shouldBe listOf(
                        IntChromosome(
                            listOf(
                                IntGene(5),
                                IntGene(7),
                                IntGene(3),
                                IntGene(4),
                                IntGene(9),
                                IntGene(1),
                                IntGene(6),
                                IntGene(2),
                                IntGene(8)
                            )
                        ),
                        IntChromosome(
                            listOf(
                                IntGene(1),
                                IntGene(2),
                                IntGene(4),
                                IntGene(9),
                                IntGene(3),
                                IntGene(5),
                                IntGene(6),
                                IntGene(7),
                                IntGene(8)
                            )
                        )
                    )
                }
            }
        }
    }
})
