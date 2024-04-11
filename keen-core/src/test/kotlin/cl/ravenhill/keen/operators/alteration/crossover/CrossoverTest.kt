package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.arb.genetic.arbGenotype
import cl.ravenhill.keen.arb.genetic.chromosomes.arbIntChromosome
import cl.ravenhill.keen.arb.operators.arbBaseCrossover
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.exceptions.CrossoverException
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.chromosomes.numeric.IntChromosome
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
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