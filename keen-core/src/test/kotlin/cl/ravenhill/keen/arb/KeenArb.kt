package cl.ravenhill.keen.arb

import cl.ravenhill.keen.arb.evolution.alterationConfig
import cl.ravenhill.keen.arb.evolution.populationConfig
import cl.ravenhill.keen.arb.evolution.selectionConfig
import cl.ravenhill.keen.arb.genetic.chromosomes.doubleChromosomeFactory
import cl.ravenhill.keen.arb.genetic.genotypeFactory
import cl.ravenhill.keen.arb.operators.alterer
import cl.ravenhill.keen.arb.operators.rouletteWheelSelector
import cl.ravenhill.keen.arb.operators.tournamentSelector
import cl.ravenhill.keen.evolution.config.AlterationConfig
import cl.ravenhill.keen.evolution.config.SelectionConfig
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import cl.ravenhill.keen.operators.alteration.Alterer
import cl.ravenhill.keen.operators.selection.RouletteWheelSelector
import cl.ravenhill.keen.operators.selection.TournamentSelector
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list

object KeenArb {

    fun probability(): Arb<Double> = Arb.double(0.0..1.0, includeNonFiniteEdgeCases = false)
    fun arbTournamentSelector(): Arb<TournamentSelector<Double, DoubleGene>> =
        KeenArb.tournamentSelector<Double, DoubleGene>()

    fun arbRouletteWheelSelector(): Arb<RouletteWheelSelector<Double, DoubleGene>> = Arb.rouletteWheelSelector()
    fun selectionConfig(): Arb<SelectionConfig<Double, DoubleGene>> = Arb.selectionConfig(
        probability(),
        arbTournamentSelector(),
        arbRouletteWheelSelector()
    )

    fun arbAlterers(): Arb<List<Alterer<Double, DoubleGene>>> = Arb.list(Arb.alterer())
    fun alterationConfig(): Arb<AlterationConfig<Double, DoubleGene>> = Arb.alterationConfig(arbAlterers())
}