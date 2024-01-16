/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.arb.genetic.chromosomes.intChromosome
import cl.ravenhill.keen.arb.genetic.chromosomes.nothingChromosome
import cl.ravenhill.keen.arb.genetic.genotype
import cl.ravenhill.keen.arb.operators.baseCrossover
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.exceptions.CrossoverInvocationException
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.genetic.genes.NothingGene
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
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import io.kotest.property.assume
import io.kotest.property.checkAll

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

            "crosses the expected number of chromosomes" {
                checkAll(
                    PropTestConfig(minSuccess = 800, maxFailure = 200),
                    Arb.validCrossover(Arb.intChromosome(Arb.int(10..10)))
                ) { (crossover, genotypes) ->
                    val result = crossover.crossover(genotypes)
                    val expected = (crossover.chromosomeRate * genotypes.first().size).toInt()
                    result.crosses shouldBeInRange expected - 1..expected + 1
                }
            }
        }
    }
})

private fun <T, G> Arb.Companion.validCrossover(chromosome: Arb<Chromosome<T, G>>) where G : Gene<T, G> = arbitrary {
    val size = int(1..10).bind()
    val crossover = baseCrossover<T, G>().bind()
    val genotypes = list(genotype(chromosome, constant(size)), crossover.numParents..crossover.numParents).bind()
    crossover to genotypes
}