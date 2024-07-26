package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.evolution.states.GeneticEvolutionState
import cl.ravenhill.keen.evolution.states.State
import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.ranking.Ranker

@Deprecated(
    "Use GeneticEvolutionState instead",
    ReplaceWith("GeneticEvolutionState(generation, ranker, population)")
)
class EvolutionState<T, G>(
    override val generation: Int,
    override val ranker: Ranker<T, G>,
    override val population: Population<T, G>,
) : State<T, G, Individual<T, G>> by GeneticEvolutionState(generation, ranker, population)
        where G : Gene<T, G>
