package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.arb.datatypes.arbProbability
import cl.ravenhill.keen.arb.genetic.chromosomes.arbIntChromosome
import cl.ravenhill.keen.assertions.should.shouldBeExclusive
import cl.ravenhill.keen.assertions.should.shouldNotBeExclusive
import cl.ravenhill.keen.genetic.chromosomes.numeric.IntChromosome
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.spec.style.freeSpec
import io.kotest.core.spec.style.scopes.FreeSpecContainerScope
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.flatMap
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll
import kotlin.random.Random

class AverageCrossoverTest : FreeSpec({
    include(`crossover construction`())
    include(`when crossing chromosomes`())
})

private fun `crossover construction`() = freeSpec {
    "When constructing" - {
        `throws an exception if the chromosome rate is not in the range 0 to 1`(::AverageCrossover)
        `with default parameters should have the following properties`()
        `valid configuration should initialize correctly`()
    }
}

@OptIn(ExperimentalKotest::class)
private fun `when crossing chromosomes`() = freeSpec {
    "When crossing chromosomes" - {
        "returns the expected offspring" - {
            "with gene rate 0.0" {
                checkAll(
                    PropTestConfig(iterations = 100),
                    arbCrossoverAndValidInputs(Arb.constant(0.0))
                ) { (crossover, chromosomes) ->
                    val offspring = crossover.crossoverChromosomes(chromosomes)
                    offspring.first().flatten() shouldBe chromosomes[0].flatten()
                }
            }

            "with gene rate 1.0" {
                checkAll(
                    arbCrossoverAndValidInputs(Arb.constant(1.0))
                ) { (crossover, chromosomes) ->
                    TODO()
                }
            }
        }
    }
}

private suspend fun FreeSpecContainerScope.`with default parameters should have the following properties`() {
    "with default parameters should have the following properties" {
        with(AverageCrossover<Int, IntGene>()) {
            numParents shouldBe AverageCrossover.DEFAULT_NUM_PARENTS
            numOffspring shouldBe 1
            if (AverageCrossover.DEFAULT_EXCLUSIVITY) {
                shouldBeExclusive()
            } else {
                shouldNotBeExclusive()
            }
            chromosomeRate shouldBe AverageCrossover.DEFAULT_CHROMOSOME_RATE
            geneRate shouldBe AverageCrossover.DEFAULT_GENE_RATE
        }
    }
}

private suspend fun FreeSpecContainerScope.`valid configuration should initialize correctly`() {
    "with a custom number of parents" {
        checkAll(Arb.int(2..5)) { numParents ->
            val crossover = AverageCrossover<Int, IntGene>(numParents = numParents)
            with(crossover) {
                numParents shouldBe numParents
                numOffspring shouldBe 1
                if (AverageCrossover.DEFAULT_EXCLUSIVITY) {
                    shouldBeExclusive()
                } else {
                    shouldNotBeExclusive()
                }
                chromosomeRate shouldBe AverageCrossover.DEFAULT_CHROMOSOME_RATE
                geneRate shouldBe 1.0
            }
        }
    }

    "with a custom chromosome rate" {
        checkAll(arbProbability()) { rate ->
            val crossover = AverageCrossover<Int, IntGene>(chromosomeRate = rate)
            with(crossover) {
                numParents shouldBe AverageCrossover.DEFAULT_NUM_PARENTS
                numOffspring shouldBe 1
                if (AverageCrossover.DEFAULT_EXCLUSIVITY) {
                    shouldBeExclusive()
                } else {
                    shouldNotBeExclusive()
                }
                chromosomeRate shouldBe rate
                geneRate shouldBe 1.0
            }
        }
    }

    "with custom gene rate" {
        checkAll(arbProbability()) { rate ->
            val crossover = AverageCrossover<Int, IntGene>(geneRate = rate)
            with(crossover) {
                numParents shouldBe AverageCrossover.DEFAULT_NUM_PARENTS
                numOffspring shouldBe 1
                if (AverageCrossover.DEFAULT_EXCLUSIVITY) {
                    shouldBeExclusive()
                } else {
                    shouldNotBeExclusive()
                }
                chromosomeRate shouldBe AverageCrossover.DEFAULT_CHROMOSOME_RATE
                geneRate shouldBe rate
            }
        }
    }

    "with exclusivity" {
        val crossover = AverageCrossover<Int, IntGene>(exclusivity = true)
        with(crossover) {
            numParents shouldBe AverageCrossover.DEFAULT_NUM_PARENTS
            numOffspring shouldBe 1
            shouldBeExclusive()
            chromosomeRate shouldBe AverageCrossover.DEFAULT_CHROMOSOME_RATE
            geneRate shouldBe 1.0
        }
    }

    "without exclusivity" {
        val crossover = AverageCrossover<Int, IntGene>(exclusivity = false)
        with(crossover) {
            numParents shouldBe AverageCrossover.DEFAULT_NUM_PARENTS
            numOffspring shouldBe 1
            shouldNotBeExclusive()
            chromosomeRate shouldBe AverageCrossover.DEFAULT_CHROMOSOME_RATE
            geneRate shouldBe 1.0
        }
    }
}

private fun arbCrossoverAndValidInputs(
    geneRate: Arb<Double> = arbProbability(),
): Arb<Pair<AverageCrossover<Int, IntGene>, List<IntChromosome>>> =
    arbAverageCrossover(geneRate).flatMap { crossover ->
        Arb.list(arbIntChromosome(), crossover.numParents..crossover.numParents)
            .filter { chromosomes -> chromosomes.map { it.size }.distinct().size == 1 }
            .map { inputs -> crossover to inputs }
    }

fun arbAverageCrossover(
    geneRate: Arb<Double> = arbProbability(),
): Arb<AverageCrossover<Int, IntGene>> = arbitrary {
    AverageCrossover(arbProbability().bind(), geneRate.bind(), Arb.int(2..5).bind(), Arb.boolean().bind())
}

private fun cross(chromosomes: List<IntChromosome>, geneRate: Double, rng: Random): List<Int> {
    val genes = chromosomes.flatten()
    return List(chromosomes[0].size) { i ->
        if (rng.nextDouble() < geneRate) {
            genes[i].average(genes.drop(chromosomes.size)).toInt()
        } else {
            chromosomes[0][i].toInt()
        }
    }
}