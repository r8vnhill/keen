/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.exceptions.MutatorConfigException
import cl.ravenhill.keen.genetic.genes.NothingGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class RandomMutatorTest : FreeSpec({

    "Should have an individual rate property that" - {
        "defaults to [RandomMutator.DEFAULT_INDIVIDUAL_RATE]" {
            RandomMutator<Nothing, NothingGene>().individualRate shouldBe RandomMutator.DEFAULT_INDIVIDUAL_RATE
        }

        "can be set to a value between 0 and 1" {
            RandomMutator<Nothing, NothingGene>(0.3).individualRate shouldBe 0.3
        }

        "should throw an exception if set to a value that's not between 0 and 1" {
            shouldThrow<CompositeException> {
                RandomMutator<Nothing, NothingGene>(1.1)
            }.shouldHaveInfringement<MutatorConfigException>("The individual rate (1.1) must be in 0.0..1.0")
        }
    }
})
