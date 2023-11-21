/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.crossover.pointbased

import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.arbs.datatypes.compose
import cl.ravenhill.keen.arbs.datatypes.intWith
import cl.ravenhill.keen.arbs.datatypes.matrix
import cl.ravenhill.keen.arbs.datatypes.probability
import cl.ravenhill.keen.arbs.genetic.intGene
import cl.ravenhill.keen.arbs.operators.singlePointCrossover
import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.shouldHaveInfringement
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.checkAll
import org.junit.jupiter.api.fail
import kotlin.math.min
import kotlin.random.Random

class SinglePointCrossoverTest : FreeSpec({

    "A [SinglePointCrossover] operator" - {
        "when created" - {
            "without an explicit [chromosomeRate] defaults to 1.0" {
                checkAll(Arb.boolean()) { exclusivity ->
                    val crossover = SinglePointCrossover<Nothing, NothingGene>(exclusivity = exclusivity)
                    crossover.chromosomeRate shouldBe 1.0
                    crossover.exclusivity shouldBe exclusivity
                }
            }

            "without an explicit [exclusivity] defaults to false" {
                checkAll(Arb.probability()) { chromosomeRate ->
                    val crossover = SinglePointCrossover<Nothing, NothingGene>(chromosomeRate)
                    crossover.exclusivity.shouldBeFalse()
                    crossover.chromosomeRate shouldBe chromosomeRate
                }
            }
        }

        "when applied to two lists of genes" - {
            "returns a list with two new lists of genes crossed over at the given point" {
                checkAll(
                    Arb.singlePointCrossover<Int, IntGene>(),
                    Arb.intWith(
                        Arb.matrix(Arb.intGene(), Arb.constant(2), Arb.int(3..10))
                    ) { Arb.int(0..it.size) }
                ) { crossover, (cutPoint, parents) ->
                    val (parent1, parent2) = parents
                    val (child1, child2) = crossover.crossoverAt(cutPoint, parent1 to parent2)
                    child1.size shouldBe parent1.size
                    child2.size shouldBe parent2.size
                    child1 shouldBe List(parent1.size) { if (it < cutPoint) parent1[it] else parent2[it] }
                    child2 shouldBe List(parent2.size) { if (it < cutPoint) parent2[it] else parent1[it] }
                }
            }

            "returns the same lists if the picked index is the first or last index" {
                checkAll(
                    Arb.singlePointCrossover<Int, IntGene>(),
                    Arb.matrix(Arb.intGene(), Arb.constant(2), Arb.int(3..10)),
                ) { crossover, parents ->
                    val (parent1, parent2) = parents
                    val (child1, child2) = crossover.crossoverAt(0, parent1 to parent2)
                    child1 shouldBe parent2
                    child2 shouldBe parent1
                    val (child3, child4) = crossover.crossoverAt(parent1.size, parent1 to parent2)
                    child3 shouldBe parent1
                    child4 shouldBe parent2
                }
            }

            "should throw an exception if " - {
                "the index is negative " {
                    checkAll(
                        Arb.singlePointCrossover<Int, IntGene>(),
                        Arb.matrix(Arb.intGene(), Arb.constant(2), Arb.int(3..10)),
                        Arb.negativeInt()
                    ) { crossover, parents, cutPoint ->
                        shouldThrow<CompositeException> {
                            crossover.crossoverAt(cutPoint, parents[0] to parents[1])
                        }.shouldHaveInfringement<IntConstraintException>(
                            "The index must be in the range [0, ${min(parents[0].size, parents[1].size)}]."
                        )
                    }
                }

                "the index is greater than the size of the parents" {
                    checkAll(
                        Arb.singlePointCrossover<Int, IntGene>(),
                        Arb.matrix(
                            Arb.intGene(),
                            Arb.constant(2),
                            Arb.int(3..10)
                        ).compose { Arb.int(it[0].size + 1..Int.MAX_VALUE) }
                    ) { crossover, (parents, cutPoint) ->
                        shouldThrow<CompositeException> {
                            crossover.crossoverAt(cutPoint, parents[0] to parents[1])
                        }.shouldHaveInfringement<IntConstraintException>(
                            "The index must be in the range [0, ${min(parents[0].size, parents[1].size)}]."
                        )
                    }
                }
            }
        }

        "when crossing a list of chromosomes" - {
            "returns the same list if the chromosome rate is 0.0" {
                checkAll(
                    Arb.singlePointCrossover<Int, IntGene>(Arb.constant(0.0)),
                    Arb.matrix(Arb.intGene(), Arb.constant(2), Arb.int(3..10)),
                ) { crossover, genes ->
                    val chromosomes = genes.map { IntChromosome(it) }
                    val crossed = crossover.crossoverChromosomes(chromosomes)
                    crossed shouldBe chromosomes
                }
            }

            "always perform a crossover if the chromosome rate is 1.0" {
                val chromosomes = listOf(
                    IntChromosome(IntGene(1), IntGene(2), IntGene(3), IntGene(4)),
                    IntChromosome(IntGene(5), IntGene(6), IntGene(7), IntGene(8))
                )
                Core.random = Random(0)
                val crossover = SinglePointCrossover<Int, IntGene>(1.0)
                val crossed = crossover.crossoverChromosomes(chromosomes)
                crossed shouldBe listOf(
                    IntChromosome(IntGene(1), IntGene(2), IntGene(3), IntGene(8)),
                    IntChromosome(IntGene(5), IntGene(6), IntGene(7), IntGene(4))
                )
            }

            "perform a crossover according to a given probability" {
                checkAll(Arb.probability()) { chromosomeRate ->
                    val chromosomes = listOf(
                        IntChromosome(IntGene(1), IntGene(2), IntGene(3), IntGene(4)),
                        IntChromosome(IntGene(5), IntGene(6), IntGene(7), IntGene(8))
                    )
                    Core.random = Random(0)
                    val crossover = SinglePointCrossover<Int, IntGene>(chromosomeRate)
                    val crossed = crossover.crossoverChromosomes(chromosomes)
                    crossed shouldBe if (Random(0).nextDouble() <= chromosomeRate) {
                        listOf(
                            IntChromosome(IntGene(1), IntGene(2), IntGene(3), IntGene(8)),
                            IntChromosome(IntGene(5), IntGene(6), IntGene(7), IntGene(4))
                        )
                    } else {
                        chromosomes
                    }
                }
            }

            "should throw an exception if the number of chromosomes is not 2" {
                checkAll(
                    Arb.singlePointCrossover<Int, IntGene>(),
                    Arb.matrix(Arb.intGene(), Arb.int(3..10), Arb.int(3..10))
                ) { crossover, chromosomes ->
                    shouldThrow<CompositeException> {
                        crossover.crossoverChromosomes(chromosomes.map { IntChromosome(it) })
                    }.shouldHaveInfringement<CollectionConstraintException>(
                        "The number of chromosomes to be crossed over must be 2"
                    )
                }
            }
        }
    }
})
