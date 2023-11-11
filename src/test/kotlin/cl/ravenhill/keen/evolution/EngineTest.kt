/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.evolution.executors.EvaluationExecutor
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.limits.Limit
import cl.ravenhill.keen.operators.selector.Selector
import cl.ravenhill.keen.operators.selector.TournamentSelector
import cl.ravenhill.keen.util.optimizer.IndividualOptimizer
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class EngineTest : FreeSpec({

    "An evolution [Engine]" - {
        "can be created" {
            with(Engine<Nothing, NothingGene>(
                Genotype.Factory<Nothing, NothingGene>(),
                2,
                0.5,
                TournamentSelector(2),
                val offspringSelector: Selector<DNA, G>,
                val alterer: Alterer<DNA, G>,
            val limits: List<Limit>,
            val survivorSelector: Selector<DNA, G>,
            val optimizer: IndividualOptimizer<DNA, G>,
            val listeners: Listeners<DNA, G>,
            val evaluator: EvaluationExecutor<DNA, G>,
            val interceptor: EvolutionInterceptor<DNA, G>,
    }
})
