package cl.ravenhill.keen.operators.crossover.permutation

import cl.ravenhill.keen.arbs.datatypes.probability
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.NothingGene
import io.kotest.assertions.fail
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class PermutationCrossoverTest : FreeSpec({

    "A [PermutationCrossover]" - {
        "when created" - {
            "without an explicit number of parents defaults to 2" {
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
                        override fun doCrossover(chromosomes: List<Chromosome<Nothing, NothingGene>>) =
                            listOf(listOf(NothingGene), listOf(NothingGene))
                    }) {
                        numParents shouldBe 2
                        this.numOffspring shouldBe numOffspring
                        this.exclusivity shouldBe exclusivity
                        this.chromosomeRate shouldBe chromosomeRate
                    }
                }
            }

            "without an explicit number of offspring defaults to 2" {
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
                        override fun doCrossover(chromosomes: List<Chromosome<Nothing, NothingGene>>) =
                            listOf(listOf(NothingGene), listOf(NothingGene))
                    }) {
                        this.numParents shouldBe numParents
                        numOffspring shouldBe 2
                        this.exclusivity shouldBe exclusivity
                        this.chromosomeRate shouldBe chromosomeRate
                    }
                }
            }

            "without an explicit [exclusivity] defaults to false" {
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
                        override fun doCrossover(chromosomes: List<Chromosome<Nothing, NothingGene>>) =
                            listOf(listOf(NothingGene), listOf(NothingGene))
                    }) {
                        this.numParents shouldBe numParents
                        this.numOffspring shouldBe numOffspring
                        exclusivity.shouldBeFalse()
                        this.chromosomeRate shouldBe chromosomeRate
                    }
                }
            }

            "without an explicit [chromosomeRate] defaults to 1.0" {
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
                        override fun doCrossover(chromosomes: List<Chromosome<Nothing, NothingGene>>) =
                            listOf(listOf(NothingGene), listOf(NothingGene))
                    }) {
                        this.numParents shouldBe numParents
                        this.numOffspring shouldBe numOffspring
                        this.exclusivity shouldBe exclusivity
                        chromosomeRate shouldBe 1.0
                    }
                }
            }
        }
    }
})