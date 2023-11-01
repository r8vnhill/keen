/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.prog.terminals

import cl.ravenhill.any
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldHaveSameHashCodeAs
import io.kotest.matchers.types.shouldNotHaveSameHashCodeAs
import io.kotest.property.Arb
import io.kotest.property.assume
import io.kotest.property.checkAll

class ConstantTest : FreeSpec({
    "A [Constant]" - {
        "equality comparisons" - {
            "should return true when comparing two constants with the same value" {
                checkAll(Arb.any()) {
                    Constant(it) shouldBe Constant(it)
                }
            }

            "should return false when comparing two constants with different values" {
                checkAll(Arb.any(), Arb.any()) { a, b ->
                    assume { a shouldNotBe b }
                    Constant(a) shouldNotBe Constant(b)
                }
            }

            "should be reflexive" {
                checkAll(Arb.any()) {
                    val constant = Constant(it)
                    constant shouldBe constant
                }
            }

            "should be symmetric" {
                checkAll(Arb.any(), Arb.any()) { a, b ->
                    assume { a shouldNotBe b }
                    val constantA = Constant(a)
                    val constantB = Constant(b)
                    constantA shouldNotBe constantB
                    constantB shouldNotBe constantA
                }
            }

            "should be transitive" {
                checkAll(Arb.any()) { a ->
                    val constantA = Constant(a)
                    val constantB = Constant(a)
                    val constantC = Constant(a)
                    constantA shouldBe constantB
                    constantB shouldBe constantC
                    constantA shouldBe constantC
                }
            }
        }

        "hashing should" - {
            "be the same for two constants with the same value" {
                checkAll(Arb.any()) { a ->
                    val constantA = Constant(a)
                    val constantB = Constant(a)
                    constantA shouldHaveSameHashCodeAs constantB
                }
            }

            "be different for two constants with different values" {
                checkAll(Arb.any(), Arb.any()) { a, b ->
                    assume { a shouldNotBe b }
                    val constantA = Constant(a)
                    val constantB = Constant(b)
                    constantA shouldNotHaveSameHashCodeAs constantB
                }
            }
        }

        "should have 0 arity" {
            checkAll(Arb.any()) {
                Constant(it).arity shouldBe 0
            }
        }

        "should return its value when invoked" {
            checkAll(Arb.any()) { a ->
                Constant(a).invoke(emptyList()) shouldBe a
            }
        }

        "should be able to create a new instance with the same value" {
            checkAll(Arb.any()) { a ->
                val constant = Constant(a)
                constant.create() shouldBe constant
            }
        }
    }
})
