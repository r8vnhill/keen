package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ResetDomainListener
import cl.ravenhill.keen.arb.arbRngPair
import cl.ravenhill.keen.arb.datatypes.arbProbability
import cl.ravenhill.keen.arb.genetic.chromosomes.arbIntChromosome
import cl.ravenhill.keen.arb.genetic.genes.arbIntGene
import cl.ravenhill.keen.arb.operators.arbSinglePointCrossover
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
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.*
import io.kotest.property.checkAll
import kotlin.random.Random

@OptIn(ExperimentalKotest::class)
class SinglePointCrossoverTest : FreeSpec({
    "Can be constructed" - {
        "with default parameters" {
            checkAll(arbSinglePointCrossover<Int, IntGene>(null, null)) { crossover ->
                with(crossover) {
                    numParents shouldBe 2
                    numOffspring shouldBe 2
                    shouldNotBeExclusive()
                    chromosomeRate shouldBe 1.0
                }
            }
        }

        "with a custom chromosome rate" {
            checkAll(
                arbProbability().flatMap { rate ->
                    arbSinglePointCrossover<Int, IntGene>(Arb.constant(rate), null)
                        .map { rate to it }
                }) { (rate, crossover) ->
                with(crossover) {
                    numParents shouldBe 2
                    numOffspring shouldBe 2
                    shouldNotBeExclusive()
                    chromosomeRate shouldBe rate
                }
            }
        }

        "with exclusivity" {
            checkAll(arbSinglePointCrossover<Int, IntGene>(null, Arb.constant(true))) { crossover ->
                with(crossover) {
                    numParents shouldBe 2
                    numOffspring shouldBe 2
                    shouldBeExclusive()
                    chromosomeRate shouldBe 1.0
                }
            }
        }

        "without exclusivity" {
            checkAll(arbSinglePointCrossover<Int, IntGene>(null, Arb.constant(false))) { crossover ->
                with(crossover) {
                    numParents shouldBe 2
                    numOffspring shouldBe 2
                    shouldNotBeExclusive()
                    chromosomeRate shouldBe 1.0
                }
            }
        }
    }

    "Throws an exception if the chromosome rate is invalid" {
        checkAll(Arb.double().filter { it < 0.0 || it > 1.0 }, Arb.boolean()) { rate, exclusivity ->
            shouldThrow<CompositeException> {
                SinglePointCrossover<Int, IntGene>(rate, exclusivity)
            }.shouldHaveInfringement<CrossoverConfigException>("The chromosome rate must be in the range [0.0, 1.0].")
        }
    }

    "When crossing at a given point" - {
        "throws an exception if the index is out of bounds" {
            checkAll(arbSinglePointCrossover<Int, IntGene>(), genesAndInvalidIndex()) { crossover, (gs1, gs2, index) ->
                shouldThrow<CompositeException> {
                    crossover.crossoverAt(index, gs1 to gs2)
                }.shouldHaveInfringement<CrossoverException>(
                    "The crossover point must be in the range [0, ${gs1.size}]."
                )
            }
        }

        "throws an exception if the chromosomes have different sizes" {
            checkAll(differentSizeGenes(), Arb.int(0, 10)) { (gs1, gs2), index ->
                shouldThrow<CompositeException> {
                    SinglePointCrossover<Int, IntGene>().crossoverAt(index, gs1 to gs2)
                }.shouldHaveInfringement<CrossoverException>("Parents must have the same size")
            }
        }

        "returns the correct offspring" {
            checkAll(arbSinglePointCrossover<Int, IntGene>(), genesAndValidIndex()) { crossover, (gs1, gs2, index) ->
                val (offspring1, offspring2) = crossover.crossoverAt(index, gs1 to gs2)
                offspring1 shouldBe gs1.slice(0..<index) + gs2.slice(index..<gs2.size)
                offspring2 shouldBe gs2.slice(0..<index) + gs1.slice(index..<gs1.size)
            }
        }
    }

    "When crossing at a random point" - {
        "throws an exception if the number of parents is not 2" {
            checkAll(
                arbSinglePointCrossover<Int, IntGene>(),
                Arb.list(arbIntChromosome(), 1..10).filter { it.size != 2 }
            ) { crossover, chromosomes ->
                shouldThrow<CompositeException> {
                    crossover.crossoverChromosomes(chromosomes)
                }.shouldHaveInfringement<CrossoverException>("The number of parent chromosomes must be 2")
            }
        }

        "throws an exception if the chromosomes have different sizes" {
            checkAll(differentSizeGenes()) { (gs1, gs2) ->
                shouldThrow<CompositeException> {
                    SinglePointCrossover<Int, IntGene>().crossoverChromosomes(
                        listOf(IntChromosome(gs1), IntChromosome(gs2))
                    )
                }.shouldHaveInfringement<CrossoverException>("Both parents must have the same size")
            }
        }

        "throws an exception if the chromosomes are empty" {
            shouldThrow<CompositeException> {
                SinglePointCrossover<Int, IntGene>().crossoverChromosomes(
                    listOf(IntChromosome(emptyList()), IntChromosome(emptyList()))
                )
            }.shouldHaveInfringement<CrossoverException>("Chromosomes must not be empty")
        }

        "performs no crossover if the chromosome rate is 0" {
            checkAll(
                arbSinglePointCrossover<Int, IntGene>(Arb.constant(0.0)),
                sameSizeChromosomePair().filter { it.first.isNotEmpty() }
            ) { crossover, (gs1, gs2) ->
                val (offspring1, offspring2) = crossover.crossoverChromosomes(
                    listOf(IntChromosome(gs1), IntChromosome(gs2))
                )
                offspring1 shouldBe gs1
                offspring2 shouldBe gs2
            }
        }

        "performs a crossover if the chromosome rate is 1" {
            checkAll(
                PropTestConfig(listeners = listOf(ResetDomainListener)),
                arbSinglePointCrossover<Int, IntGene>(Arb.constant(1.0)),
                arbRngPair(),
                sameSizeChromosomePair().filter { it.first.isNotEmpty() }
            ) { crossover, (r1, r2), (gs1, gs2) ->
                Domain.random = r1
                val (offspring1, offspring2) = crossover.crossoverChromosomes(
                    listOf(IntChromosome(gs1), IntChromosome(gs2))
                )
                val index = r2.apply { nextDouble() }.nextInt(0, gs1.size)
                offspring1 shouldBe gs1.slice(0..<index) + gs2.slice(index..<gs2.size)
                offspring2 shouldBe gs2.slice(0..<index) + gs1.slice(index..<gs1.size)
            }
        }
    }
})

// Common function to generate triples of gene lists and an index
private fun genesWithIndex(indexGenerator: (Int) -> Arb<Int>): Arb<Triple<List<IntGene>, List<IntGene>, Int>> =
    Arb.list(arbIntGene()).flatMap { gs1 ->
        Arb.list(arbIntGene(), gs1.size..gs1.size).flatMap { gs2 ->
            indexGenerator(gs1.size).map { index -> Triple(gs1, gs2, index) }
        }
    }

// Generate triples with an invalid index (either < 0 or > size of the list)
private fun genesAndInvalidIndex(): Arb<Triple<List<IntGene>, List<IntGene>, Int>> =
    genesWithIndex { size -> Arb.int().filter { it < 0 || it > size } }

// Generate triples with a valid index (within the range including the size)
private fun genesAndValidIndex(): Arb<Triple<List<IntGene>, List<IntGene>, Int>> =
    genesWithIndex { size -> Arb.int(0, size) }

// Generate pairs of gene lists with different sizes
private fun differentSizeGenes(): Arb<Pair<List<IntGene>, List<IntGene>>> =
    Arb.list(arbIntGene()).flatMap { gs1 ->
        Arb.list(arbIntGene())
            .filter { it.size != gs1.size }
            .map { gs2 -> gs1 to gs2 }
    }

// Generate pairs of gene lists with the same size
private fun sameSizeChromosomePair(): Arb<Pair<List<IntGene>, List<IntGene>>> =
    Arb.list(arbIntGene()).flatMap { gs1 ->
        Arb.list(arbIntGene(), gs1.size..gs1.size).map { gs2 -> gs1 to gs2 }
    }
