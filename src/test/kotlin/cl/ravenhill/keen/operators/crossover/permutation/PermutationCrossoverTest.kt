package cl.ravenhill.keen.operators.crossover.permutation

import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.arbs.datatypes.compose
import cl.ravenhill.keen.arbs.datatypes.matrix
import cl.ravenhill.keen.arbs.datatypes.probability
import cl.ravenhill.keen.arbs.genetic.intGene
import cl.ravenhill.keen.arbs.operators.dummyPermutationCrossover
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.shouldHaveInfringement
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.fail
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.long
import io.kotest.property.assume
import io.kotest.property.checkAll
import kotlin.random.Random

@OptIn(ExperimentalKotest::class)
class PermutationCrossoverTest : FreeSpec({

    "A [PermutationCrossover]" - {
        "should default" - {
            "number of parents to 2 when not explicitly set" {
                checkAll(
                    Arb.int(2..10),
                    Arb.boolean(),
                    Arb.probability()
                ) { numOffspring, exclusivity, chromosomeRate ->
                    with(object : AbstractPermutationCrossover<Nothing, NothingGene>(
                        numOffspring = numOffspring,
                        exclusivity = exclusivity,
                        chromosomeRate = chromosomeRate
                    ) {
                        override fun performPermutationCrossover(chromosomes: List<Chromosome<Nothing, NothingGene>>) =
                            emptyList<List<NothingGene>>()
                    }) {
                        numParents shouldBe 2
                        this.numOffspring shouldBe numOffspring
                        this.exclusivity shouldBe exclusivity
                        this.chromosomeRate shouldBe chromosomeRate
                    }
                }
            }

            "number of offspring to 2 when not explicitly set" {
                checkAll(
                    Arb.int(2..10),
                    Arb.boolean(),
                    Arb.probability()
                ) { numParents, exclusivity, chromosomeRate ->
                    with(object : AbstractPermutationCrossover<Nothing, NothingGene>(
                        numParents = numParents,
                        exclusivity = exclusivity,
                        chromosomeRate = chromosomeRate
                    ) {
                        override fun performPermutationCrossover(chromosomes: List<Chromosome<Nothing, NothingGene>>) =
                            emptyList<List<NothingGene>>()
                    }) {
                        this.numParents shouldBe numParents
                        numOffspring shouldBe 2
                        this.exclusivity shouldBe exclusivity
                        this.chromosomeRate shouldBe chromosomeRate
                    }
                }
            }

            "exclusivity to false when not explicitly set" {
                checkAll(
                    Arb.int(2..10),
                    Arb.int(2..10),
                    Arb.probability()
                ) { numParents, numOffspring, chromosomeRate ->
                    with(object : AbstractPermutationCrossover<Nothing, NothingGene>(
                        numParents = numParents,
                        numOffspring = numOffspring,
                        chromosomeRate = chromosomeRate
                    ) {
                        override fun performPermutationCrossover(chromosomes: List<Chromosome<Nothing, NothingGene>>) =
                            emptyList<List<NothingGene>>()
                    }) {
                        this.numParents shouldBe numParents
                        this.numOffspring shouldBe numOffspring
                        exclusivity.shouldBeFalse()
                        this.chromosomeRate shouldBe chromosomeRate
                    }
                }
            }

            "chromosome rate to 1.0 when not explicitly set" {
                checkAll(
                    Arb.int(2..10),
                    Arb.int(2..10),
                    Arb.boolean()
                ) { numParents, numOffspring, exclusivity ->
                    with(object : AbstractPermutationCrossover<Nothing, NothingGene>(
                        numParents = numParents,
                        numOffspring = numOffspring,
                        exclusivity = exclusivity
                    ) {
                        override fun performPermutationCrossover(chromosomes: List<Chromosome<Nothing, NothingGene>>) =
                            emptyList<List<NothingGene>>()
                    }) {
                        this.numParents shouldBe numParents
                        this.numOffspring shouldBe numOffspring
                        this.exclusivity shouldBe exclusivity
                        chromosomeRate shouldBe 1.0
                    }
                }
            }
        }

        "when applied to a list of chromosomes" - {
            "should return the same list if the chromosome rate is 0.0" {
                checkAll(
                    Arb.dummyPermutationCrossover<Int, IntGene>(chromosomeRate = Arb.constant(0.0)),
                    Arb.matrix(Arb.intGene(), Arb.constant(2), Arb.int(3..10))
                ) { crossover, geneLists ->
                    assume {
                        for (genes in geneLists) {
                            genes.distinct() shouldHaveSize genes.size
                        }
                    }
                    val chromosomes = geneLists.map { IntChromosome(it) }
                    crossover.crossoverChromosomes(chromosomes) shouldBe chromosomes
                }
            }

            "should always perform a crossover if the chromosome rate is 1.0" {
                checkAll(
                    Arb.matrix(Arb.intGene(), Arb.int(2..5), Arb.int(2..5))
                        .compose {
                            Arb.dummyPermutationCrossover<Int, IntGene>(
                                numParents = Arb.constant(it.size),
                                chromosomeRate = Arb.constant(1.0)
                            )
                        }
                ) { (geneLists, crossover) ->
                    assume {
                        for (genes in geneLists) {
                            genes.distinct() shouldHaveSize genes.size
                        }
                    }
                    val chromosomes = geneLists.map { IntChromosome(it) }
                    val crossed = crossover.crossoverChromosomes(chromosomes)
                    crossed.map { it.genes } shouldBe List(crossover.numOffspring) {
                        chromosomes[0].genes
                    }
                }
            }

            "should perform a crossover according to a given probability" {
                checkAll(
                    Arb.matrix(Arb.intGene(), Arb.int(2..5), Arb.int(2..5))
                        .compose {
                            Arb.dummyPermutationCrossover<Int, IntGene>(numParents = Arb.constant(it.size))
                        },
                    Arb.long()
                ) { (geneLists, crossover), seed ->
                    assume {
                        for (genes in geneLists) {
                            genes.distinct() shouldHaveSize genes.size
                        }
                    }
                    Core.random = Random(seed)
                    val random = Random(seed)
                    val chromosomes = geneLists.map { IntChromosome(it) }
                    val crossed = crossover.crossoverChromosomes(chromosomes)
                    crossed.map { it.genes } shouldBe if (random.nextDouble() <= crossover.chromosomeRate) {
                        List(crossover.numOffspring) { chromosomes[0].genes }
                    } else {
                        geneLists
                    }
                }
            }
        }
    }
})
