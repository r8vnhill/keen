package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ResetDomainListener
import cl.ravenhill.keen.arb.arbRngPair
import cl.ravenhill.keen.arb.datatypes.arbInvalidProbability
import cl.ravenhill.keen.arb.datatypes.arbProbability
import cl.ravenhill.keen.arb.genetic.chromosomes.arbIntChromosome
import cl.ravenhill.keen.arb.operators.arbCombineCrossover
import cl.ravenhill.keen.assertions.should.shouldBeExclusive
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.assertions.should.shouldNotBeExclusive
import cl.ravenhill.keen.exceptions.CrossoverConfigException
import cl.ravenhill.keen.exceptions.CrossoverException
import cl.ravenhill.keen.genetic.chromosomes.numeric.IntChromosome
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.core.spec.style.freeSpec
import io.kotest.core.spec.style.scopes.FreeSpecContainerScope
import io.kotest.matchers.collections.shouldNotHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.flatMap
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.nonPositiveInt
import io.kotest.property.assume
import io.kotest.property.checkAll

class CombineCrossoverTest : FreeSpec({
    include(`crossover construction`())
    include(`chromosome combination`())
    include(`cross chromosomes`())
})

@OptIn(ExperimentalKotest::class)
private fun `cross chromosomes`() = freeSpec {
    "When crossing chromosomes" - {
        "returns the expected offspring" {
            checkAll(
                PropTestConfig(iterations = 100, listeners = listOf(ResetDomainListener)),
                crossoverAndValidInputs(),
                arbRngPair()
            ) { (crossover, inputs), (rng1, rng2) ->
                Domain.random = rng1
                val offspring = crossover.crossoverChromosomes(inputs)
                offspring.size shouldBe 1
                Domain.random = rng2
                val combinedGenes = combineGenes(inputs, crossover.geneRate)
                offspring shouldBe listOf(inputs.first().duplicateWithGenes(combinedGenes))
            }
        }
    }
}

private fun combineGenes(inputs: List<IntChromosome>, geneRate: Double): List<IntGene> {
    val numGenes = inputs.first().genes.size
    val combinedGenes = mutableListOf<IntGene>()

    for (i in 0 until numGenes) {
        if (Domain.random.nextDouble() < geneRate) {
            var lastGene = inputs.first().genes[i]  // Default to the first if only one input
            for (chromosome in inputs) {
                lastGene = chromosome.genes[i]  // Iterate to find the last gene
            }
            combinedGenes.add(lastGene)
        } else {
            combinedGenes.add(inputs.first().genes[i])
        }
    }
    return combinedGenes
}

private fun `chromosome combination`() = freeSpec {
    "When combining chromosomes" - {
        `invalid input should throw an exception`()
        `valid input should produce offspring`()
    }
}

@OptIn(ExperimentalKotest::class)
private suspend fun FreeSpecContainerScope.`valid input should produce offspring`() {
    "returns the first chromosome if the chromosome rate is 0" {
        checkAll(
            PropTestConfig(iterations = 100),
            crossoverAndValidInputs(Arb.constant(0.0))
        ) { (crossover, inputs) ->
            val offspring = crossover.combine(inputs)
            offspring.size shouldBe inputs[0].size
            offspring.forEachIndexed { index, gene ->
                gene shouldBe inputs[0][index]
            }
        }
    }

    "combines all genes if the gene rate is 1" {
        checkAll(
            PropTestConfig(iterations = 100),
            crossoverAndValidInputs(Arb.constant(1.0))
        ) { (crossover, inputs) ->
            val offspring = crossover.combine(inputs)
            offspring.size shouldBe inputs[0].size
            offspring.map { it.value } shouldBe inputs.reversed().first().map { it.value }
        }
    }
}

private fun crossoverAndValidInputs(
    geneRate: Arb<Double> = arbProbability()
): Arb<Pair<CombineCrossover<Int, IntGene>, List<IntChromosome>>> =
    arbCombineCrossover<Int, IntGene>(geneRate = geneRate).flatMap { crossover ->
        Arb.list(arbIntChromosome(), crossover.numParents..crossover.numParents)
            .filter { chromosomes -> chromosomes.map { it.size }.distinct().size == 1 }
            .map { inputs -> crossover to inputs }
    }

private suspend fun FreeSpecContainerScope.`invalid input should throw an exception`() {
    "throws an exception if the number of inputs is not equal to the number of parents" {
        checkAll(
            arbCombineCrossover<Int, IntGene>(), Arb.list(arbIntChromosome())
        ) { crossover, inputs ->
            assume { inputs shouldNotHaveSize crossover.numParents }
            shouldThrow<CompositeException> {
                crossover.combine(inputs)
            }.shouldHaveInfringement<CrossoverException>(
                "Number of inputs (${inputs.size}) must equal the number of parents (${crossover.numParents})"
            )
        }
    }

    "throws an exception if chromosomes have different lengths" {
        checkAll(arbCombineCrossover<Int, IntGene>(), differentLengthChromosomes()) { crossover, inputs ->
            shouldThrow<CompositeException> {
                crossover.combine(inputs)
            }.shouldHaveInfringement<CrossoverException>(
                "All chromosomes must have the same length"
            )
        }
    }
}

private fun `crossover construction`() = freeSpec {
    "When constructing" - {
        `invalid configuration should throw an exception`()
        `default configuration should initialize correctly`()
        `valid configuration should initialize correctly`()
    }
}

private suspend fun FreeSpecContainerScope.`valid configuration should initialize correctly`() {
    "with custom chromosome rate defaults the rest" {
        checkAll(arbProbability()) { rate ->
            val crossover = CombineCrossover<Int, IntGene>({ genes -> genes.first() }, chromosomeRate = rate)
            with(crossover) {
                numParents shouldBe 2
                numOffspring shouldBe 1
                shouldNotBeExclusive()
                chromosomeRate shouldBe rate
                geneRate shouldBe 1.0
            }
        }
    }

    "with custom gene rate defaults the rest" {
        checkAll(arbProbability()) { rate ->
            val crossover = CombineCrossover<Int, IntGene>({ genes -> genes.first() }, geneRate = rate)
            with(crossover) {
                numParents shouldBe 2
                numOffspring shouldBe 1
                shouldNotBeExclusive()
                chromosomeRate shouldBe 1.0
                geneRate shouldBe rate
            }
        }
    }

    "with exclusivity defaults the rest" {
        with(CombineCrossover<Int, IntGene>({ genes -> genes.first() }, exclusivity = true)) {
            numParents shouldBe 2
            numOffspring shouldBe 1
            shouldBeExclusive()
            chromosomeRate shouldBe 1.0
            geneRate shouldBe 1.0
        }
    }

    "without exclusivity defaults the rest" {
        with(CombineCrossover<Int, IntGene>({ genes -> genes.first() }, exclusivity = false)) {
            numParents shouldBe 2
            numOffspring shouldBe 1
            shouldNotBeExclusive()
            chromosomeRate shouldBe 1.0
            geneRate shouldBe 1.0
        }
    }

    "with custom number of parents defaults the rest" {
        checkAll(Arb.int(2..Int.MAX_VALUE)) { numParents ->
            val crossover = CombineCrossover<Int, IntGene>({ genes -> genes.first() }, numParents = numParents)
            with(crossover) {
                numParents shouldBe numParents
                numOffspring shouldBe 1
                shouldNotBeExclusive()
                chromosomeRate shouldBe 1.0
                geneRate shouldBe 1.0
            }
        }
    }
}

private suspend fun FreeSpecContainerScope.`default configuration should initialize correctly`() {
    "with default parameters should have the following properties" {
        checkAll(arbCombineCrossover<Int, IntGene>(null, null, null, null)) { crossover ->
            with(crossover) {
                numParents shouldBe 2
                numOffspring shouldBe 1
                shouldNotBeExclusive()
                chromosomeRate shouldBe 1.0
                geneRate shouldBe 1.0
            }
        }
    }
}

private suspend fun FreeSpecContainerScope.`invalid configuration should throw an exception`() {
    `throws an exception if the chromosome rate is not in the range 0 to 1` { invalidRate ->
        CombineCrossover({ genes -> genes.first()}, chromosomeRate = invalidRate)
    }

    "throws an exception if the gene rate is not between 0 and 1" {
        checkAll(arbInvalidProbability()) { rate ->
            shouldThrow<CompositeException> {
                CombineCrossover<Int, IntGene>({ genes -> genes.first() }, geneRate = rate)
            }.shouldHaveInfringement<CrossoverConfigException>("The gene rate ($rate) must be in 0.0..1.0")
        }
    }

    "throws an exception if the number of parents is less than 2" {
        checkAll(Arb.nonPositiveInt()) { numParents ->
            shouldThrow<CompositeException> {
                CombineCrossover<Int, IntGene>({ genes -> genes.first() }, numParents = numParents)
            }.shouldHaveInfringement<CrossoverConfigException>(
                "The number of parents ($numParents) must be greater than 1"
            )
        }
    }
}

private fun differentLengthChromosomes(): Arb<List<IntChromosome>> =
    Arb.list(arbIntChromosome()).filter { chromosomes ->
        chromosomes.map { it.size }.distinct().size > 1
    }
