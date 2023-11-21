/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.assertions.engine

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.evolution.EvolutionEngine
import cl.ravenhill.keen.evolution.EvolutionState
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import io.kotest.core.spec.style.scopes.FreeSpecContainerScope
import io.kotest.matchers.shouldBe
import kotlin.random.Random


suspend fun FreeSpecContainerScope.`check Engine single step evolution`() {
    "can evolve a population from a given state when" - {
        "the state is empty" - {
            "should return a new state with the expected size" {
                val state = EvolutionState.empty<Int, IntGene>()
                val engine = EvolutionEngine.Factory<Int, IntGene>({ 1.0 }, Genotype.Factory()).make()
                val result = engine.evolve(state)
                result.size shouldBe engine.populationSize
            }

            "should return a new state with the expected individuals" {
                Core.random = Random(11)
                val state = EvolutionState.empty<Int, IntGene>()
                val engine = EvolutionEngine.Factory<Int, IntGene>({ 1.0 }, Genotype.Factory()).apply {
                    populationSize = 4
                }.make()
                val result = engine.evolve(state)
                result shouldBe EvolutionState(
                    1, Individual<Int, IntGene>(Genotype(), 1.0), Individual(Genotype(), 1.0),
                    Individual(Genotype(), 1.0), Individual(Genotype(), 1.0)
                )
            }
        }
    }
}