/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.limits

import cl.ravenhill.keen.arbs.evolution.engine
import cl.ravenhill.keen.arbs.limits.listenLimit
import cl.ravenhill.keen.arbs.listeners.evolutionListener
import cl.ravenhill.keen.assertions.util.listeners.`test ListenLimit with varying generations`
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class ListenLimitTest : FreeSpec({

    "A [ListenLimit]" - {
        "when initialized with a specific listener and condition" - {
            "should retain the same listener instance" {
                checkAll(Arb.evolutionListener<Nothing, NothingGene>()) { listener ->
                    ListenLimit(listener) { true }.listener shouldBe listener
                }
            }
        }

        "its [engine] property" - {
            "should initially be null" {
                checkAll(Arb.listenLimit<Nothing, NothingGene>()) { limit ->
                    limit.engine.shouldBeNull()
                }
            }

            "can be assigned a null value" {
                checkAll(Arb.listenLimit<Nothing, NothingGene>()) { limit ->
                    limit.engine = null
                    limit.engine.shouldBeNull()
                }
            }

            "can be assigned a non-null engine instance" {
                checkAll(Arb.listenLimit<Int, IntGene>(), Arb.engine()) { limit, engine ->
                    limit.engine = engine
                    limit.engine shouldBe engine
                    engine.listeners shouldContain limit.listener
                }
            }
        }

        "when invoked" - {
            "accurately determines if the limit condition is met" {
                `test ListenLimit with varying generations`(Arb.listenLimit<Int, IntGene>()) {
                    generation % 100 == 0
                }
            }
        }
    }
})
