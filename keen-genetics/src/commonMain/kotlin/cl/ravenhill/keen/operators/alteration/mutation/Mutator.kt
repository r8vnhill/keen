/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.alteration.mutation

import arrow.core.Either
import cl.ravenhill.keen.Individual
import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.exceptions.OperatorInvocationException
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.operators.alteration.Alterer

interface Mutator<T, G> : Alterer<T, G, Genotype<T, G>> where G : Gene<T, G> {

    val individualRate: Double

    val chromosomeRate: Double

    override suspend fun <S> invoke(
        state: S,
        outputSize: Int,
        buildState: (List<Individual<T, G, Genotype<T, G>>>) -> S
    ): Either<OperatorInvocationException, S> where S : EvolutionState<T, G, Genotype<T, G>, S> {
        TODO()
    }
}
