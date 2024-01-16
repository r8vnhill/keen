/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ResetDomainListener
import cl.ravenhill.keen.arb.genetic.chromosomes.intChromosome
import cl.ravenhill.keen.arb.genetic.chromosomes.nothingChromosome
import cl.ravenhill.keen.arb.genetic.genotype
import cl.ravenhill.keen.arb.operators.baseCrossover
import cl.ravenhill.keen.arb.randomContext
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.exceptions.CrossoverInvocationException
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import cl.ravenhill.keen.utils.indices
import cl.ravenhill.keen.utils.transpose
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.withClue
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldNotHaveSize
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import io.kotest.property.assume
import io.kotest.property.checkAll
import kotlin.random.Random

@OptIn(ExperimentalKotest::class)
class CrossoverTest : FreeSpec({

    "Crossover" - {
        "when crossing genotypes" - {
            "fails if arguments doesn't match number of parents" {
                checkAll(
                    Arb.baseCrossover<Nothing, NothingGene>(),
                    Arb.list(Arb.genotype(Arb.nothingChromosome()))
                ) { crossover, genotypes ->
                    assume {
                        genotypes shouldNotHaveSize crossover.numParents
                    }
                    withClue("Number of parents: ${crossover.numParents}; Number of genotypes: ${genotypes.size}") {
                        shouldThrow<CompositeException> {
                            crossover.crossover(genotypes)
                        }.shouldHaveInfringement<CrossoverInvocationException>(
                            "The number of genotypes (${genotypes.size}) doesn't match the number of parents" +
                                  " (${crossover.numParents})"
                        )
                    }
                }
            }

            "fails if the number of chromosomes is not the same for all genotypes" {
                checkAll(
                    Arb.baseCrossover<Nothing, NothingGene>().map { crossover ->
                        crossover to Arb.list(
                            Arb.genotype(Arb.nothingChromosome()),
                            crossover.numParents..crossover.numParents
                        ).next()
                    }
                ) { (crossover, genotypes) ->
                    assume {
                        genotypes.map { it.chromosomes.size }.distinct().size shouldBeGreaterThan 1
                    }
                    withClue("Number of chromosomes: ${genotypes.map { it.chromosomes.size }}") {
                        shouldThrow<CompositeException> {
                            crossover.crossover(genotypes)
                        }.shouldHaveInfringement<CrossoverInvocationException>(
                            "The number of chromosomes in each genotype must be the same"
                        )
                    }
                }
            }

            "crosses the expected number of chromosomes when" - {
                "the chromosome rate is 0.0" {
                    checkAll(Arb.validCrossover(Arb.intChromosome(), Arb.constant(0.0))) { (crossover, genotypes) ->
                        val result = crossover.crossover(genotypes)
                        result.crosses shouldBe 0
                    }
                }

                "the chromosome rate is 1.0" {
                    checkAll(Arb.validCrossover(Arb.intChromosome(), Arb.constant(1.0))) { (crossover, genotypes) ->
                        val result = crossover.crossover(genotypes)
                        result.crosses shouldBe genotypes.first().size
                    }
                }

                "the chromosome rate is arbitrary" {
                    checkAll(
                        PropTestConfig(minSuccess = 800, maxFailure = 200),
                        Arb.validCrossover(Arb.intChromosome())
                    ) { (crossover, genotypes) ->
                        val result = crossover.crossover(genotypes)
                        val expected = (crossover.chromosomeRate * genotypes.first().size).toInt()
                        result.crosses shouldBeInRange expected - 1..expected + 1
                    }
                }
            }

            "crosses the expected chromosomes" {
                checkAll(
                    PropTestConfig(listeners = listOf(ResetDomainListener)),
                    Arb.validCrossover(Arb.intChromosome()),
                    Arb.randomContext()
                ) { (crossover, genotypes), (seed, rng) ->
                    Domain.random = rng
                    val result = crossover.crossover(genotypes)
                    Domain.random = Random(seed)
                    val expected = crossover(crossover, genotypes)
                    result shouldBe expected
                }
            }
        }
    }
})

/**
 * Generates a valid crossover pair, consisting of a Crossover instance and a list of Genotype objects,
 * suitable for testing crossover functionality. This function is an extension of the `Arb` companion object.
 *
 * @param T The type of the value that the [Gene] represents.
 * @param G The type of the [Gene], constrained to be a subclass of `Gene<T, G>`.
 * @param chromosome An [Arb]<[Chromosome]<[T], [G]>> instance representing the arbitrary chromosome generator.
 * @return A Pair of a [Crossover] instance and a list of [Genotype] objects.
 */
private fun <T, G> Arb.Companion.validCrossover(
    chromosome: Arb<Chromosome<T, G>>,
    chromosomeRate: Arb<Double> = double(0.0, 1.0),
) where G : Gene<T, G> = arbitrary {
    val size = int(1..10).bind()
    val crossover = baseCrossover<T, G>(chromosomeRate).bind()
    val genotypes = list(genotype(chromosome, constant(size)), crossover.numParents..crossover.numParents).bind()
    crossover to genotypes
}

/**
 * A recursive implementation of the crossover function, used as a model to test [Crossover.crossover].
 *
 * This function differs from the original `Crossover.crossover` by employing a recursive approach to process chromosome
 * recombination, in contrast to the iterative approach in the original function.
 *
 * @param cx The Crossover object containing the crossover logic.
 * @param genotypes A list of Genotype instances to be crossed over.
 * @return A `GenotypeCrossoverResult` object representing the result of the crossover operation.
 * @param T The type of the value that the Gene represents.
 * @param G The type of the Gene, constrained to be a subclass of `Gene<T, G>`.
 */
private fun <T, G> crossover(
    cx: Crossover<T, G>,
    genotypes: List<Genotype<T, G>>,
): GenotypeCrossoverResult<T, G> where G : Gene<T, G> {
    val size = genotypes.first().size
    val chromosomeIndices = Domain.random.indices(pickProbability = cx.chromosomeRate, end = size)
    val chromosomes = chromosomeIndices.map { index -> genotypes.map { it[index] } }

    fun recursiveCrossover(
        chromosomesList: List<List<Chromosome<T, G>>>,
        acc: List<List<Chromosome<T, G>>> = listOf(),
    ): List<List<Chromosome<T, G>>> {
        if (chromosomesList.isEmpty()) return acc

        val head = chromosomesList.first()
        val tail = chromosomesList.drop(1)

        val crossed = cx.crossoverChromosomes(head)
        return recursiveCrossover(tail, acc + listOf(crossed))
    }

    val offspringChromosomes = recursiveCrossover(chromosomes).transpose()

    return GenotypeCrossoverResult(
        offspringChromosomes.map {
            var i = 0
            Genotype(genotypes[0].mapIndexed { index, chromosome ->
                if (index in chromosomeIndices) {
                    it[i++]
                } else {
                    chromosome
                }
            })
        }, chromosomes.size
    )
}
