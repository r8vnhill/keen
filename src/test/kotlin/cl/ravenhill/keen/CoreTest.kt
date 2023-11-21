/*
 *  Copyright (c) 2023, Ignacio Slater M.
 *  2-Clause BSD License.
 */

package cl.ravenhill.keen

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.arbs.random
import cl.ravenhill.keen.assertions.shouldHaveInfringement
import cl.ravenhill.keen.prog.Environment
import io.kotest.assertions.throwables.shouldThrowUnit
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import kotlin.random.Random
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.nonPositiveInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.checkAll
import cl.ravenhill.keen.arbs.uniqueStrings
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldContainKey
import io.kotest.matchers.maps.shouldHaveSize

class CoreTest : FreeSpec({

    beforeAny {
        Core.random = Random.Default
        Core.maxProgramDepth = Core.DEFAULT_MAX_PROGRAM_DEPTH
        Core.environments.clear()
    }

    "The random number generator" - {
        "has a default value of Random.Default" {
            Core.random shouldBe Random.Default
        }

        "can be set to a new Random" {
            checkAll(Arb.random()) {
                Core.random = it
                Core.random shouldBe it
            }
        }

        "can generate random numbers" {
            checkAll<Long> { seed ->
                val r = Random(seed)
                Core.random = Random(seed)
                repeat(100) {
                    r.nextDouble() shouldBe Core.random.nextDouble()
                }
            }
        }
    }

    "The maximum program depth" - {
        "has a default value of 7" {
            Core.DEFAULT_MAX_PROGRAM_DEPTH shouldBe 7
            Core.maxProgramDepth shouldBe Core.DEFAULT_MAX_PROGRAM_DEPTH
        }

        "can be set to a positive integer" {
            Core.maxProgramDepth shouldBe Core.DEFAULT_MAX_PROGRAM_DEPTH
            checkAll(Arb.positiveInt()) { depth ->
                Core.maxProgramDepth = depth
                Core.maxProgramDepth shouldBe depth
            }
        }

        "cannot be set to a non-positive integer" {
            Core.maxProgramDepth shouldBe Core.DEFAULT_MAX_PROGRAM_DEPTH
            checkAll(Arb.nonPositiveInt()) { depth ->
                shouldThrowUnit<CompositeException> {
                    Core.maxProgramDepth = depth
                }.shouldHaveInfringement<IntConstraintException>("The maximum program depth [$depth] must be positive")
                Core.maxProgramDepth shouldBe Core.DEFAULT_MAX_PROGRAM_DEPTH
            }
        }
    }

    "The collection of environments" - {
        "is empty by default" {
            Core.environments.isEmpty() shouldBe true
        }

        "can be added to" {
            Core.environments.isEmpty() shouldBe true
            checkAll(Arb.uniqueStrings()) { ids ->
                Core.environments.shouldBeEmpty()
                ids.forEach {
                    it to Environment<Any>(it)
                }
                Core.environments.shouldHaveSize(ids.size)
                ids.forEach {
                    Core.environments shouldContainKey it
                }
                Core.environments.clear()
            }
        }
    }
})
