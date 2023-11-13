/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.limits

import cl.ravenhill.keen.arbs.genetic.nothingPopulation
import cl.ravenhill.keen.arbs.optimizer
import cl.ravenhill.keen.evolution.EvolutionResult
import cl.ravenhill.keen.evolution.Evolver
import cl.ravenhill.keen.genetic.genes.NothingGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.checkAll

class MatchLimitTest : FreeSpec({

    "A [MatchLimit]" - {
        "can be created with a predicate" {
            checkAll(
                Arb.nonNegativeInt(),
                Arb.nonNegativeInt(),
                Arb.nonNegativeInt(),
                Arb.double(),
                Arb.optimizer<Nothing, NothingGene>(),
                Arb.nothingPopulation()
            ) { i, gen, steady, fitness, optimizer, population ->
                val limit = ListenLimit { generation >= i }
                limit.invoke(object : Evolver<Nothing, NothingGene> {
                    override val generation: Int = gen
                    override val steadyGenerations: Int = steady
                    override val bestFitness: Double = fitness
                    override fun evolve(): EvolutionResult<Nothing, NothingGene> =
                        EvolutionResult(optimizer, population, generation)
                }) shouldBe (gen >= i)
            }
        }
    }
})
