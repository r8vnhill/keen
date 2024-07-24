package cl.ravenhill.keen.operators.alteration.crossover

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.ConstraintException
import cl.ravenhill.keen.arb.datatypes.arbProbability
import cl.ravenhill.keen.arb.genetic.chromosomes.arbIntChromosome
import cl.ravenhill.keen.arb.genetic.genes.arbIntGene
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.exceptions.CrossoverException
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.chromosomes.numeric.IntChromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.shouldNot
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.assume
import io.kotest.property.checkAll

class PermutationCrossoverTest : FreeSpec({
    "A PermutationCrossover instance" - {
        "when crossing chromosomes" - {
            "should throw an exception if the chromosomes are not permutations" {
                checkAll(
                    arbAnonymousPermutationCrossover<Int, IntGene>(),
                    arbChromosomeListWithDuplicates()
                ) { crossover, chromosomes ->
                    // collect the index of all chromosomes that are not permutations
                    val nonPermutationIndices = chromosomes.indices.filterNot {
                        chromosomes[it].genes.toSet().size == chromosomes[it].genes.size
                    }
                    val exception = shouldThrow<CompositeException> {
                        crossover.crossoverChromosomes(chromosomes)
                    }
                    for (index in nonPermutationIndices) {
                        exception.shouldHaveInfringement<CrossoverException>("Chromosome $index is not a permutation")
                    }
                }
            }

            "should throw an exception if the chromosomes have different elements" {
                checkAll(
                    arbAnonymousPermutationCrossover<Int, IntGene>(),
                    Arb.list(arbIntChromosome(), 2..100)
                ) { crossover, chromosomes ->
                    assume {
                        chromosomes shouldNot haveSameElements()
                    }
                    val exception = shouldThrow<CompositeException> {
                        crossover.crossoverChromosomes(chromosomes)
                    }
                    exception.shouldHaveInfringement<ConstraintException>(
                        "All chromosomes must have the same elements in any order"
                    )
                }
            }
        }
    }
})

fun haveSameElements(): Matcher<List<IntChromosome>> = object : Matcher<List<IntChromosome>> {
    override fun test(value: List<IntChromosome>): MatcherResult {
        val first = value.first()
        val allSameElements = value.all { it.genes.toSet() == first.genes.toSet() }
        return MatcherResult(
            allSameElements,
            { "All chromosomes must have the same elements in any order" },
            { "Chromosomes must have different elements" }
        )
    }
}

fun <T, G> arbAnonymousPermutationCrossover(
    numOffspring: Arb<Int> = Arb.positiveInt(10),
    numParents: Arb<Int> = Arb.positiveInt(10),
    chromosomeRate: Arb<Double> = arbProbability(),
    exclusivity: Arb<Boolean> = Arb.boolean()
): Arb<PermutationCrossover<T, G>> where G : Gene<T, G> = arbitrary {
    val numOffspringBound = numOffspring.bind()
    val numParentsBound = numParents.bind()
    val chromosomeRateBound = chromosomeRate.bind()
    val exclusivityBound = exclusivity.bind()

    object : PermutationCrossover<T, G> {
        override fun permuteChromosomes(chromosomes: List<Chromosome<T, G>>) = chromosomes.map {
            it.genes.reversed()
        }

        override val numOffspring = numOffspringBound
        override val numParents = numParentsBound
        override val chromosomeRate = chromosomeRateBound
        override val exclusivity = exclusivityBound
    }
}

fun arbChromosomeListWithDuplicates(): Arb<List<IntChromosome>> = arbitrary {
    val chromosomeWithDuplicates: IntChromosome = arbChromosomeWithDuplicates().bind()
    val chromosomes: List<IntChromosome> = Arb.list(arbIntChromosome()).bind()
    (listOf(chromosomeWithDuplicates) + chromosomes).shuffled()
}

fun arbChromosomeWithDuplicates(): Arb<IntChromosome> = arbitrary {
    val repeatedElements = Arb.list(Arb.constant(arbIntGene().bind()), 2..100).bind()
    val genes = Arb.list(arbIntGene()).bind()
    IntChromosome((genes + repeatedElements).shuffled())
}
