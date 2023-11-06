/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.prog.terminals

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.arbs.prog.environment
import io.kotest.core.spec.style.FreeSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.long
import io.kotest.property.checkAll
import kotlin.random.Random


class EphemeralConstantTest : FreeSpec({
    "An [EphemeralConstant]" - {
        "can be created with a generator" - {
            Core.random = Random(11)
            withData(nameFn = { it.repr },
                Generator("0", 0) { 0 },
                Generator("random", Core.random.nextInt()) {
                    Core.random = Random(11)
                    Core.random.nextInt()
                }) { (_, expected, generator) ->
                val constant = EphemeralConstant(generator)
                repeat(10) {
                    constant.value shouldBe expected
                }
            }
        }

        "can be invoked as a function" {
            checkAll(Arb.list(Arb.long()), Arb.environment<Long>(), Arb.long()) { args, env, seed ->
                Core.random = Random(seed)
                val randomGenerator = Random(seed)
                val constant = EphemeralConstant(Core.random::nextLong)
                constant(env, args) shouldBe randomGenerator.nextLong()
            }
        }

        "can be converted to a string" {
            checkAll(Arb.long()) { seed ->
                Core.random = Random(seed)
                val randomGenerator = Random(seed)
                val constant = EphemeralConstant(Core.random::nextLong)
                constant.toString() shouldBe "${randomGenerator.nextLong()}"
            }
        }

        "can create another instance of itself" {
            checkAll(Arb.long()) { seed ->
                Core.random = Random(seed)
                val randomGenerator = Random(seed)
                val constant = EphemeralConstant(Core.random::nextLong)
                val newConstant = constant.create()
                repeat(10) {
                    newConstant.value shouldBe randomGenerator.nextLong()
                }
            }
        }
    }
}) {
    private data class Generator(val repr: String, val expected: Int, val generator: () -> Int)
}