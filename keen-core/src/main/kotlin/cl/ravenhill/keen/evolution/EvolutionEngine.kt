package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.evolution.config.AlterationConfig
import cl.ravenhill.keen.evolution.config.EvolutionConfig
import cl.ravenhill.keen.evolution.config.PopulationConfig
import cl.ravenhill.keen.evolution.config.SelectionConfig
import cl.ravenhill.keen.evolution.engines.Evolver
import cl.ravenhill.keen.evolution.engines.GeneticAlgorithm
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.Gene

@Deprecated(
    "Use GeneticAlgorithm instead",
    ReplaceWith("GeneticAlgorithm(populationConfig, selectionConfig, alterationConfig, evolutionConfig)")
)
class EvolutionEngine<T, G>(
    populationConfig: PopulationConfig<T, G>,
    selectionConfig: SelectionConfig<T, G>,
    alterationConfig: AlterationConfig<T, G>,
    evolutionConfig: EvolutionConfig<T, G>
) : Evolver<T, G, Individual<T, G>> by GeneticAlgorithm(
    populationConfig,
    selectionConfig,
    alterationConfig,
    evolutionConfig
)
        where G : Gene<T, G>
