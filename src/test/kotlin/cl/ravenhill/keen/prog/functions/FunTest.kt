/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.prog.functions

import cl.ravenhill.enforcer.EnforcementException
import cl.ravenhill.enforcer.IntRequirementException
import cl.ravenhill.keen.arbs.datatypes.list
import cl.ravenhill.keen.arbs.prog.environment
import cl.ravenhill.keen.arbs.prog.function
import cl.ravenhill.keen.shouldHaveInfringement
import cl.ravenhill.unfulfilledConstraint
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldNotHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.arbitrary.string
import io.kotest.property.assume
import io.kotest.property.checkAll

class FunTest : FreeSpec({

    "A [Fun]" - {
        "when created" - {
            "with a name, arity and function should have the same values" {
                checkAll(
                    Arb.string(), Arb.nonNegativeInt(), Arb.function<Int>()
                ) { name, arity, function ->
                    val funObj = Fun(name, arity, function)
                    funObj.name shouldBe name
                    funObj.arity shouldBe arity
                    funObj.body shouldBe function
                }
            }

            "should throw an exception when created with a negative arity" {
                checkAll(Arb.string(), Arb.negativeInt(), Arb.function<Int>()) { name, arity, function ->
                    shouldThrow<EnforcementException> {
                        Fun(name, arity, function)
                    }.shouldHaveInfringement<IntRequirementException>(
                        unfulfilledConstraint("The arity [$arity] must be at least 0")
                    )
                }
            }
        }

        "when invoked" - {
            "should return the result of the function" {
                checkAll(
                    Arb.string(),
                    Arb.function<Int>(),
                    Arb.environment<Int>(),
                    Arb.list(Arb.int())
                ) { name, body, environment, args ->
                    val funObj = Fun(name, args.size, body)
                    funObj(environment, args) shouldBe body(args)
                }
            }

            "should throw a exception if the number of arguments is different from the arity" {
                checkAll(
                    Arb.string(),
                    Arb.nonNegativeInt(),
                    Arb.function<Int>(),
                    Arb.environment<Int>(),
                    Arb.list(Arb.int())
                ) { name, arity, body, environment, args ->
                    assume { args shouldNotHaveSize arity }
                    val funObj = Fun(name, args.size + 1, body)
                    shouldThrow<EnforcementException> {
                        funObj(environment, args)
                    }.shouldHaveInfringement<IntRequirementException>(
                        unfulfilledConstraint("The number of arguments [${args.size}] must be equal to the arity [${args.size + 1}]")
                    )
                }
            }
        }
    }
})