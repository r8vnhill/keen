package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.operators.alteration.Alterer
import kotlin.random.Random

interface Mutator<T, G> : Alterer<T, G, Genotype<T, G>> where G : Gene<T, G> {

    val individualRate: Double

    val chromosomeRate: Double

    override suspend fun <S : EvolutionState<T, G, Genotype<T, G>>> invoke(
        state: S,
        outputSize: Int,
        buildState: (List<Individual<T, G, Genotype<T, G>>>) -> S,
        random: Random
    ): Result<S> = TODO()
}
