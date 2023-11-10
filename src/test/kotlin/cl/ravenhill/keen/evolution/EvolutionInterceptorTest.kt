/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution

import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.util.optimizer.FitnessMinimizer
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class EvolutionInterceptorTest : FreeSpec({

    "An [EvolutionInterceptor]" - {
        "can be created with before and after functions" {
            val before = { _: EvolutionState<Int, IntGene> -> EvolutionState<Int, IntGene>(emptyList(), 0) }
            val after = { _: EvolutionResult<Int, IntGene> ->
                EvolutionResult<Int, IntGene>(
                    FitnessMinimizer(),
                    emptyList(),
                    0
                )
            }
            val interceptor = EvolutionInterceptor(before, after)
            interceptor.before shouldBe before
            interceptor.after shouldBe after
        }
    }

    "An identity [EvolutionInterceptor]" - {
        "can be created with the identity function" {
            val interceptor = EvolutionInterceptor.identity<Int, IntGene>()
            val result = EvolutionResult<Int, IntGene>(FitnessMinimizer(), emptyList(), 0)
            interceptor.before(result) shouldBe result
            interceptor.after(result) shouldBe result
        }
    }

    "An [EvolutionInterceptor] with an after function" - {
        "can be created with the after function" {
            val optimizer = FitnessMinimizer<Int, IntGene>()
            val after = { _: EvolutionResult<Int, IntGene> ->
                EvolutionResult(optimizer, emptyList(), 0)
            }
            val interceptor = EvolutionInterceptor.after(after)
            val result = EvolutionResult(optimizer, emptyList(), 0)
            interceptor.before(result) shouldBe result
            interceptor.after(result) shouldBe after(result)
        }
    }
})
