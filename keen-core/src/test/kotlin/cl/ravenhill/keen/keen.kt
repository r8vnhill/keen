package cl.ravenhill.keen

import cl.ravenhill.keen.evolution.EvolutionEngine
import cl.ravenhill.keen.evolution.config.AlterationConfig
import cl.ravenhill.keen.evolution.config.EvolutionConfig
import cl.ravenhill.keen.evolution.config.PopulationConfig
import cl.ravenhill.keen.evolution.config.SelectionConfig
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary

fun arbEngine(
    populationConfig: Arb<PopulationConfig<Double, DoubleGene>>,
    selectionConfig: Arb<SelectionConfig<Double, DoubleGene>>,
    alterationConfig: Arb<AlterationConfig<Double, DoubleGene>>,
    evolutionConfig: Arb<EvolutionConfig<Double, DoubleGene>>
): Arb<EvolutionEngine<Double, DoubleGene>> = arbitrary {
    EvolutionEngine(
        populationConfig.bind(),
        selectionConfig.bind(),
        alterationConfig.bind(),
        evolutionConfig.bind()
    )
}
