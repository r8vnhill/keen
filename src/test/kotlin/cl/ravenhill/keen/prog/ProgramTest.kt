/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.prog

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.arbs.prog.environment
import cl.ravenhill.keen.arbs.prog.terminal
import cl.ravenhill.keen.prog.functions.Add
import cl.ravenhill.keen.shouldHaveInfringement
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.checkAll

class ProgramTest : FreeSpec({

    "A [Program]" - {
        "when created" - {
            "without explicit [children] should default to an empty list" {
                checkAll(Arb.terminal()) { t ->
                    with(Program(t)) {
                        node shouldBe t
                        children shouldBe emptyList()
                    }
                }
            }

            "with explicit [children] should use them" {
                checkAll(Arb.environment<Double>(), Arb.terminal(), Arb.terminal()) { environment, t1, t2 ->
                    with(Program(Add(), listOf(Program(t1), Program(t2)))) {
                        children.size shouldBe 2
                        children[0].node(environment) shouldBe t1(environment)
                        children[1].node(environment) shouldBe t2(environment)
                    }
                }
            }

            "throw an exception if the arity of the reduceable expression is different from the number of children" {
                checkAll(Arb.terminal(), Arb.terminal(), Arb.terminal()) { t1, t2, t3 ->
                    shouldThrow<CompositeException> {
                        Program(Add(), listOf(Program(t1), Program(t2), Program(t3)))
                    }.shouldHaveInfringement<IntConstraintException>(
                        "The arity of the reduceable expression must be equal to the number of children"
                    )
                }
            }
        }

        "can access its [root]" - {
            "when it is the only node" {
                checkAll(Arb.terminal(), Arb.environment<Double>()) { t, environment ->
                    Program(t).root(environment) shouldBe Program(t)(environment)
                }
            }

            "when it is not the only node" {
                checkAll(Arb.environment<Double>(), Arb.terminal(), Arb.terminal()) { environment, t1, t2 ->
                    with(Program(Add(), listOf(Program(t1), Program(t2)))) {
                        root(environment) shouldBe Program(Add(), listOf(Program(t1), Program(t2)))(environment)
                    }
                }
            }
        }

        "can be invoked" {
            checkAll(Arb.environment<Double>(), Arb.terminal(), Arb.terminal()) { environment, t1, t2 ->
                with(Program(Add(), listOf(Program(t1), Program(t2)))) {
                    node(environment, t1(environment), t2(environment)) shouldBe
                          Add()(environment, listOf(t1(environment), t2(environment)))
                }
            }
        }

        "can create a new instance" {
            checkAll(Arb.environment<Double>(), Arb.terminal(), Arb.terminal()) { environment, t1, t2 ->
                with(Program(Add(), listOf(Program(t1), Program(t2)))) {
                    val newProgram = createNode(Add(), listOf(Program(t1), Program(t2)))
                    newProgram.node(environment, t1(environment), t2(environment)) shouldBe
                          Add()(environment, listOf(t1(environment), t2(environment)))
                }
            }
        }

        "can be copied" {
            checkAll(Arb.environment<Double>(), Arb.terminal(), Arb.terminal()) { environment, t1, t2 ->
                with(Program(Add(), listOf(Program(t1), Program(t2)))) {
                    val copy = copy()
                    copy.node(environment, t1(environment), t2(environment)) shouldBe
                          Add()(environment, listOf(t1(environment), t2(environment)))
                }
            }
        }
    }
})
