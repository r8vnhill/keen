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
            with(
                Engine<Nothing, NothingGene>(
                    Genotype.Factory<Nothing, NothingGene>(),
                    2,
                    0.5,
                    TournamentSelector(2),
                    TournamentSelector(2),
                    RandomMutator(0.5),
                    listOf(GenerationCount(10)),
                    TournamentSelector(2),
                    FitnessMinimizer(),
                    listOf(),
                    SequentialEvaluator { 0.0 },
                    EvolutionInterceptor.identity()
                )
            ) {

            }
        }
    }
})
