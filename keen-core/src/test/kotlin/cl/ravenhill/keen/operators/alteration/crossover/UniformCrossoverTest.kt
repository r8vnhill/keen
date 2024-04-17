package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.ExperimentalKeen
import cl.ravenhill.keen.ResetDomainListener
import cl.ravenhill.keen.arb.arbRngPair
import cl.ravenhill.keen.arb.datatypes.arbInvalidProbability
import cl.ravenhill.keen.arb.datatypes.arbProbability
import cl.ravenhill.keen.arb.genetic.chromosomes.arbIntChromosome
import cl.ravenhill.keen.assertions.should.shouldBeExclusive
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.assertions.should.shouldNotBeExclusive
import cl.ravenhill.keen.exceptions.CrossoverConfigException
import cl.ravenhill.keen.genetic.chromosomes.numeric.IntChromosome
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.spec.style.freeSpec
import io.kotest.core.spec.style.scopes.FreeSpecContainerScope
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.flatMap
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.nonPositiveInt
import io.kotest.property.checkAll
import cl.ravenhill.keen.Domain
import io.kotest.common.ExperimentalKotest
import kotlin.random.Random

class UniformCrossoverTest : FreeSpec({
    include(`when constructing`())
    include(`when crossing chromosomes`())
})

// region : Assertions
private fun `when constructing`() = freeSpec {
    "When constructing" - {
        `invalid configuration should throw an exception`()
        `default configuration should initialize correctly`()
        `valid configuration should initialize correctly`()
    }
}

@OptIn(ExperimentalKotest::class)
private fun `when crossing chromosomes`() = freeSpec {
    "When crossing chromosomes" - {
        "returns the expected offspring" {
            checkAll(
                PropTestConfig(iterations = 100, listeners = listOf(ResetDomainListener)),
                arbCrossoverAndValidInputs(),
                arbRngPair()
            ) { (crossover, chromosomes), rngPair ->
                Domain.random = rngPair.first
                val offspring = crossover.crossoverChromosomes(chromosomes)
                offspring.first().size shouldBe chromosomes.first().size
                offspring.flatten() shouldBe cross(chromosomes, rngPair.second)
            }
        }
    }
}

private suspend fun FreeSpecContainerScope.`invalid configuration should throw an exception`() {
    "throws an exception if the chromosome rate is not between 0 and 1" {
        checkAll(arbInvalidProbability()) { rate ->
            shouldThrow<CompositeException> {
                UniformCrossover<Int, IntGene>(chromosomeRate = rate)
            }.shouldHaveInfringement<CrossoverConfigException>("The chromosome rate ($rate) must be in 0.0..1.0")
        }
    }

    "throws an exception if the number of parents is less than 2" {
        checkAll(Arb.nonPositiveInt()) { numParents ->
            shouldThrow<CompositeException> {
                UniformCrossover<Int, IntGene>(numParents = numParents)
            }.shouldHaveInfringement<CrossoverConfigException>(
                "The number of parents ($numParents) must be greater than 1"
            )
        }
    }
}

private suspend fun FreeSpecContainerScope.`default configuration should initialize correctly`() {
    "with default parameters should have the following properties" {
        val crossover = UniformCrossover<Int, IntGene>()
        with(crossover) {
            numParents shouldBe UniformCrossover.DEFAULT_NUM_PARENTS
            numOffspring shouldBe 1
            if (UniformCrossover.DEFAULT_EXCLUSIVITY) {
                shouldBeExclusive()
            } else {
                shouldNotBeExclusive()
            }
            chromosomeRate shouldBe UniformCrossover.DEFAULT_CHROMOSOME_RATE
            geneRate shouldBe 1.0
        }
    }
}

private suspend fun FreeSpecContainerScope.`valid configuration should initialize correctly`() {
    "with a custom number of parents" {
        checkAll(Arb.int(2..5)) { numParents ->
            val crossover = UniformCrossover<Int, IntGene>(numParents = numParents)
            with(crossover) {
                numParents shouldBe numParents
                numOffspring shouldBe 1
                if (UniformCrossover.DEFAULT_EXCLUSIVITY) {
                    shouldBeExclusive()
                } else {
                    shouldNotBeExclusive()
                }
                chromosomeRate shouldBe UniformCrossover.DEFAULT_CHROMOSOME_RATE
                geneRate shouldBe 1.0
            }
        }
    }

    "with a custom chromosome rate" {
        checkAll(arbProbability()) { rate ->
            val crossover = UniformCrossover<Int, IntGene>(chromosomeRate = rate)
            with(crossover) {
                numParents shouldBe UniformCrossover.DEFAULT_NUM_PARENTS
                numOffspring shouldBe 1
                if (UniformCrossover.DEFAULT_EXCLUSIVITY) {
                    shouldBeExclusive()
                } else {
                    shouldNotBeExclusive()
                }
                chromosomeRate shouldBe rate
                geneRate shouldBe 1.0
            }
        }
    }

    "with exclusivity" {
        val crossover = UniformCrossover<Int, IntGene>(exclusivity = true)
        with(crossover) {
            numParents shouldBe UniformCrossover.DEFAULT_NUM_PARENTS
            numOffspring shouldBe 1
            shouldBeExclusive()
            chromosomeRate shouldBe UniformCrossover.DEFAULT_CHROMOSOME_RATE
            geneRate shouldBe 1.0
        }
    }

    "without exclusivity" {
        val crossover = UniformCrossover<Int, IntGene>(exclusivity = false)
        with(crossover) {
            numParents shouldBe UniformCrossover.DEFAULT_NUM_PARENTS
            numOffspring shouldBe 1
            shouldNotBeExclusive()
            chromosomeRate shouldBe UniformCrossover.DEFAULT_CHROMOSOME_RATE
            geneRate shouldBe 1.0
        }
    }
}
// endregion

// region : Arb
private fun arbUniformCrossover(
    numParents: Arb<Int>? = Arb.int(2..5),
    chromosomeRate: Arb<Double>? = arbProbability(),
    exclusivity: Arb<Boolean>? = Arb.boolean()
): Arb<UniformCrossover<Int, IntGene>> = arbitrary {
    UniformCrossover(
        numParents = numParents?.bind() ?: UniformCrossover.DEFAULT_NUM_PARENTS,
        chromosomeRate = chromosomeRate?.bind() ?: UniformCrossover.DEFAULT_CHROMOSOME_RATE,
        exclusivity = exclusivity?.bind() ?: UniformCrossover.DEFAULT_EXCLUSIVITY
    )
}

private fun arbCrossoverAndValidInputs(): Arb<Pair<UniformCrossover<Int, IntGene>, List<IntChromosome>>> =
    arbUniformCrossover().flatMap { crossover ->
        Arb.list(arbIntChromosome(), crossover.numParents..crossover.numParents)
            .filter { chromosomes -> chromosomes.map { it.size }.distinct().size == 1 }
            .map { inputs -> crossover to inputs }
    }
// endregion

// region : Helpers
private fun cross(chromosomes: List<IntChromosome>, rng: Random): List<IntGene> {
    val offspring = mutableListOf<IntGene>()
    for (i in chromosomes[0].indices) {
        rng.nextDouble()
        offspring += chromosomes.random(rng)[i]
    }
    return offspring
}
// endregion