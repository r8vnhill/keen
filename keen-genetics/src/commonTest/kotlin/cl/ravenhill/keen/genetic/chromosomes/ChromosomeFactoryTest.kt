/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.genetic.genes.SimpleGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.checkAll

class ChromosomeFactoryTest : FreeSpec({
    "A ChromosomeFactory" - {
        "has a size that delegates to non-null" {
            checkAll(arbSimpleChromosomeFactory(sizeArb = null)) { factory ->
                shouldThrow<IllegalStateException>(factory::size)
            }
        }

        "has a constructor executor that starts as default" {
            checkAll(arbSimpleChromosomeFactory(constructorExecutorArb = null)) { factory ->
                factory.executor::class shouldBe Domain.defaultConstructor<SimpleGene>()::class
            }
        }
    }
})
