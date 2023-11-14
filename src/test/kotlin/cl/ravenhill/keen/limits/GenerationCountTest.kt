/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.limits

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.shouldHaveInfringement
import io.kotest.assertions.fail
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.nonPositiveInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.checkAll

class GenerationCountTest : FreeSpec({

    "A [GenerationCount]" - {
        "can be created with a positive integer" {
            checkAll(Arb.positiveInt()) {
                GenerationCount<Int, IntGene>(it).count shouldBe it
            }
        }

        "cannot be created with a non-positive integer" {
            checkAll(Arb.nonPositiveInt()) {
                shouldThrow<CompositeException> {
                    GenerationCount<Int, IntGene>(it)
                }.shouldHaveInfringement<IntConstraintException>("Generation count [$it] must be at least 1")
            }
        }
    }
})
