package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.arb.genetic.arbGenotype
import cl.ravenhill.keen.arb.genetic.chromosomes.arbIntChromosome
import cl.ravenhill.keen.arb.operators.arbBaseCrossover
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.exceptions.CrossoverException
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.chromosomes.numeric.IntChromosome
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.checkAll

class CrossoverTest : FreeSpec({
    "Crossing a list of parents" - {
        "should return the expected number of offspring" {
            checkAll(crossoverAndParents()) { (crossover, parents) ->
                val offspring = crossover.crossover(parents)
                offspring shouldHaveSize crossover.numOffspring
            }
        }

        "should throw an exception if" - {
            "the number of parents is different from the inputs" {
                checkAll(mismatchedCrossoverPairs()) { (crossover, parents) ->
                    shouldThrow<CompositeException> {
                        crossover.crossover(parents)
                    }.shouldHaveInfringement<CrossoverException>(
                        "The number of inputs (${parents.size}) must be equal to the number of parents " +
                                "(${crossover.numParents})"
                    )
                }
            }

            "the genotypes have different number of chromosomes" - {
                withData(
                    genericCrossover to listOf(
                        Genotype(IntChromosome(IntGene(1))),
                        Genotype(IntChromosome(IntGene(1)), IntChromosome(IntGene(1)))
                    ),
                    genericCrossover to listOf(
                        Genotype(IntChromosome(IntGene(1)), IntChromosome(IntGene(1))),
                        Genotype(IntChromosome(IntGene(1)))
                    ),
                    genericCrossover to listOf(
                        Genotype(IntChromosome(IntGene(1)), IntChromosome(IntGene(1))),
                        Genotype(IntChromosome(IntGene(1)), IntChromosome(IntGene(1)), IntChromosome(IntGene(1)))
                    )
                ) { (crossover, parents) ->
                    shouldThrow<CompositeException> {
                        crossover.crossover(parents)
                    }.shouldHaveInfringement<CrossoverException>(
                        "Genotypes must have the same number of chromosomes"
                    )
                }
            }

            "any parent genotype is empty" - {
                withData(
                    Triple(
                        genericCrossover,
                        listOf(
                            Genotype(),
                            Genotype(IntChromosome(IntGene(1)))
                        ),
                        listOf(0)
                    ),
                    Triple(
                        genericCrossover,
                        listOf(
                            Genotype(IntChromosome(IntGene(1))),
                            Genotype()
                        ),
                        listOf(1)
                    ),
                    Triple(
                        genericCrossover,
                        listOf(
                            Genotype(),
                            Genotype()
                        ),
                        listOf(0, 1)
                    )
                ) { (crossover, parents, emptyIndex) ->
                    val ex = shouldThrow<CompositeException> {
                        crossover.crossover(parents)
                    }
                    emptyIndex.forEach { index ->
                        ex.shouldHaveInfringement<CrossoverException>(
                            "The number of chromosomes in parent $index must be greater than 0"
                        )
                    }
                }
            }
        }
    }
})

private fun nonEmptyChromosome(size: Int): Arb<IntChromosome> =
    arbIntChromosome(Arb.int(size..size)).filter { it.size > 0 }

private fun genotypeList(size: Int): Arb<List<Genotype<Int, IntGene>>> =
    Arb.list(arbGenotype(nonEmptyChromosome(5)), size..size)

private fun crossoverAndParents(): Arb<Pair<Crossover<Int, IntGene>, List<Genotype<Int, IntGene>>>> =
    arbBaseCrossover<Int, IntGene>().flatMap { crossover ->
        genotypeList(crossover.numParents).map { parents -> crossover to parents }
    }

private fun mismatchedCrossoverPairs() = arbBaseCrossover<Int, IntGene>().flatMap { crossover ->
    Arb.list(arbGenotype(arbIntChromosome(Arb.int(5..5)))).filter {
        it.size != crossover.numParents
    }.map { parents -> crossover to parents }
}

private val genericCrossover: Crossover<Int, IntGene> = object : Crossover<Int, IntGene> {
    override val numOffspring: Int = 2
    override val numParents: Int = 2
    override val chromosomeRate: Double = 0.5
    override val exclusivity: Boolean = false

    override fun crossoverChromosomes(chromosomes: List<Chromosome<Int, IntGene>>) =
        throw NotImplementedError("Never called")
}