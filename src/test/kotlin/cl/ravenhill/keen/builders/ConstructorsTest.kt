/*
 *  Copyright (c) 2023, Ignacio Slater M.
 *  2-Clause BSD License.
 */


package cl.ravenhill.keen.builders

import cl.ravenhill.keen.evolution.executors.ConstructorExecutor
import cl.ravenhill.keen.evolution.executors.SequentialConstructor
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe


class ConstructorsTest : FreeSpec({
    "A Constructor Scope" - {
        "can set a factory" {
            val scope = ConstructorScope<IntGene>()
            val factory = ConstructorExecutor.Factory<IntGene>()
            scope.factory = factory
            scope.factory shouldBe factory
        }

        "can set a creator" {
            val scope = ConstructorScope<IntGene>()
            val creator: (Unit) -> ConstructorExecutor<IntGene> = { SequentialConstructor() }
            scope.creator = creator
            scope.creator shouldBe creator
        }
    }
})