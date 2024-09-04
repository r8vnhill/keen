/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.states

import cl.ravenhill.arbPopulation
import cl.ravenhill.arbRanker
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.arbSimpleGenotype
import cl.ravenhill.keen.genetic.chromosomes.arbChromosome
import cl.ravenhill.keen.genetic.genes.SimpleGene
import cl.ravenhill.keen.genetic.genes.arbSimpleGene
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.utils.arbIndividual
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.bind
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.checkAll


class GeneticEvolutionStateTest : FreeSpec({
    "A GeneticEvolutionState" - {
        "can be created" {
            val individualArb = arbIndividual(arbSimpleGenotype(arbChromosome(arbSimpleGene())))
            val populationArb = arbPopulation(individualArb)
            checkAll(
                populationArb,
                arbRanker<_, _, Genotype<Int, SimpleGene>>(),
                Arb.nonNegativeInt()
            ) { population, ranker, generation ->
                GeneticEvolutionState(population, ranker, generation)
            }
        }

        "can be copied" {
            val individualArb = arbIndividual(arbSimpleGenotype(arbChromosome(arbSimpleGene())))
            val populationArb = arbPopulation(individualArb)
            checkAll(
                arbGeneticEvolutionState(populationArb),
                populationArb,
                arbRanker<_, _, Genotype<Int, SimpleGene>>(),
                Arb.nonNegativeInt()
            ) { state, population, ranker, generation ->
                val copy = state.makeCopy(population, ranker, generation)
                copy.population shouldBe population
                copy.ranker shouldBe ranker
                copy.generation shouldBe generation
            }
        }

        "can be created empty" {
            checkAll(arbRanker<_, _, Genotype<Int, SimpleGene>>()) { ranker ->
                val state = GeneticEvolutionState.empty(ranker)
                state.population shouldBe emptyList()
                state.ranker shouldBe ranker
                state.generation shouldBe 0
            }
        }
    }
})

/**
 * Generates an arbitrary [GeneticEvolutionState] for use in property-based testing.
 *
 * @param populationArb An `Arb<Population<T, G, Genotype<T, G>>>` for generating the population.
 * @param rankerArb An optional `Arb<IndividualRanker<T, G, Genotype<T, G>>>` for generating the ranker. Defaults to
 *   randomly selecting between `FitnessMaxRanker` and `FitnessMinRanker`.
 * @param generationArb An optional `Arb<Int>` for generating the generation number. Defaults to generating non-negative
 *   integers.
 * @return An `Arb<GeneticEvolutionState<T, G>>` for generating random instances of `GeneticEvolutionState`.
 */
fun <T, G> arbGeneticEvolutionState(
    populationArb: Arb<Population<T, G, Genotype<T, G>>>,
    rankerArb: Arb<IndividualRanker<T, G, Genotype<T, G>>> = arbRanker(),
    generationArb: Arb<Int> = Arb.nonNegativeInt()
) where G : Gene<T, G> = Arb.bind(
    populationArb,
    rankerArb,
    generationArb
) { population, ranker, generation ->
    GeneticEvolutionState(population, ranker, generation)
}
