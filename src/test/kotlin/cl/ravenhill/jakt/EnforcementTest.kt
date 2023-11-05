/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.jakt

import cl.ravenhill.jakt.constraints.Constraint
import cl.ravenhill.jakt.exceptions.ConstraintException
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.arbitrary.pair
import io.kotest.property.arbitrary.string
import io.kotest.property.assume
import io.kotest.property.checkAll

@OptIn(ExperimentalKotest::class)
class EnforcementTest : FreeSpec({
    beforeEach {
        Jakt.skipChecks = false
    }

    "The skip checks flag" - {
        "has a default value of false" {
            Jakt.skipChecks shouldBe false
        }

        "can be set to true" {
            Jakt.skipChecks shouldBe false
            Jakt.skipChecks = true
            Jakt.skipChecks shouldBe true
        }
    }

    "Enforcing a requirement" - {
        "performs no checks if the skip checks flag is set to true" {
            Jakt.skipChecks = true
            checkAll(Arb.list(Arb.pair(Arb.string(), Arb.requirement()), 0..50)) { strings ->
                Jakt.constraints {
                    shouldNotThrowAny {
                        strings.forEach { (msg, req) ->
                            msg.invoke { must(req) }
                        }
                    }
                }
            }
        }
        "doesn't throw an exception if the requirement is fulfilled" {
            checkAll(Arb.list(Arb.string())) { strings ->
                shouldNotThrowAny {
                    Jakt.constraints {
                        strings.forEach { msg ->
                            msg.invoke { must(trueConstraint) }
                        }
                    }
                }
            }
        }
        "throws an exception if the requirement is not fulfilled" {
            checkAll(Arb.list(Arb.string())) { strings ->
                assume { strings.shouldNotBeEmpty() }
                shouldThrow<cl.ravenhill.jakt.exceptions.CompositeException> {
                    Jakt.constraints {
                        strings.forEach { msg ->
                            msg.invoke { must(falseConstraint) }
                        }
                    }
                }
            }
        }
    }

    "The [Enforcement.Scope]" - {
        "has a list of" - {
            "[Result]s that is empty by default" {
                Jakt.Scope().results.shouldBeEmpty()
            }
            "[Failure]s that is empty by default" {
                Jakt.Scope().failures.shouldBeEmpty()
            }
        }
        "has a [StringScope] that" - {
            "can be created with a message" {
                checkAll<String> { msg ->
                    Jakt.Scope().StringScope(msg).message shouldBe msg
                }
            }

            "can access its outer scope" {
                checkAll<String> { msg ->
                    val scope = Jakt.Scope()
                    scope.StringScope(msg).outerScope shouldBeSameInstanceAs scope
                }
            }

            "can be converted to [String]" {
                checkAll<String> { msg ->
                    Jakt.Scope().StringScope(msg)
                        .toString() shouldBe "StringScope(message='$msg')"
                }
            }

            "can validate a `must` requirement when" - {
                "the predicate is true" {
                    `check must`(trueConstraint) { scope, _ ->
                        scope.results.forEach { it.shouldBeSuccess() }
                        scope.failures.shouldBeEmpty()
                    }
                }

                "the predicate is false" {
                    `check must`(falseConstraint) { scope, iterations ->
                        scope.results.forEach { it.shouldBeFailure() }
                        scope.failures shouldHaveSize iterations
                    }
                }

                "the predicate is true for some iterations and false for others" {
                    checkAll(Arb.list(Arb.pair(Arb.string(), Arb.requirement()), 0..50)) { enforced ->
                        val scope = Jakt.Scope()
                        with(scope) {
                            enforced.forEach { (msg, req) ->
                                msg.invoke { must(req) }
                            }
                        }
                        enforced.map { it.second }.zip(scope.results).forEach { (req, res) ->
                            when (req) {
                                trueConstraint -> res.shouldBeSuccess()
                                else -> res.shouldBeFailure()
                            }
                        }
                    }
                }
            }

            "can validate a `mustNot` requirement when" - {
                "the predicate is true" {
                    checkAll(Arb.string(), Arb.nonNegativeInt(100)) { msg, iterations ->
                        val scope = Jakt.Scope()
                        with(scope.StringScope(msg)) {
                            repeat(iterations) {
                                mustNot(trueConstraint)
                            }
                        }
                        scope.results.size shouldBe iterations
                        scope.results.forEach { it.shouldBeFailure() }
                        scope.failures.size shouldBe iterations
                    }
                }
                "the predicate is false" {
                    checkAll(
                        PropTestConfig(iterations = 50),
                        Arb.string(), Arb.int(0..50)) { msg, iterations ->
                        val scope = Jakt.Scope()
                        with(scope.StringScope(msg)) {
                            repeat(iterations) {
                                mustNot(falseConstraint)
                            }
                        }
                        scope.results.size shouldBe iterations
                        scope.results.forEach { it.shouldBeSuccess() }
                        scope.failures.shouldBeEmpty()
                    }
                }

                "the predicate is true for some iterations and false for others" {
                    checkAll(
                        PropTestConfig(iterations = 50),
                        Arb.list(Arb.pair(Arb.string(), Arb.requirement()))) { enforced ->
                        val scope = Jakt.Scope()
                        with(scope) {
                            enforced.forEach { (msg, req) ->
                                msg.invoke { mustNot(req) }
                            }
                        }
                        enforced.map { it.second }.zip(scope.results).forEach { (req, res) ->
                            when (req) {
                                trueConstraint -> res.shouldBeFailure()
                                else -> res.shouldBeSuccess()
                            }
                        }
                    }
                }
            }

            "can validate a _predicate requirement_ when" - {
                "the predicate is true" {
                    checkAll(Arb.string(), Arb.nonNegativeInt(100)) { msg, iterations ->
                        val scope = Jakt.Scope()
                        with(scope.StringScope(msg)) {
                            repeat(iterations) {
                                requirement { true }
                            }
                        }
                        scope.results.size shouldBe iterations
                        scope.results.forEach { it.shouldBeSuccess() }
                        scope.failures.shouldBeEmpty()
                    }
                }
                "the predicate is false" {
                    checkAll(Arb.string(), Arb.nonNegativeInt(100)) { msg, iterations ->
                        val scope = Jakt.Scope()
                        with(scope.StringScope(msg)) {
                            repeat(iterations) {
                                requirement { false }
                            }
                        }
                        scope.results.size shouldBe iterations
                        scope.results.forEach { it.shouldBeFailure() }
                        scope.failures.size shouldBe iterations
                    }
                }
            }
        }
        "can validate a list of clauses" {
            checkAll(Arb.string()) { msg ->
                val scope = Jakt.Scope()
                with(scope) {
                    msg.invoke {
                        must(trueConstraint)
                        mustNot(falseConstraint)
                        requirement { true }
                    }
                }
                scope.results.size shouldBe 3
                scope.results.forEach { it.shouldBeSuccess() }
                scope.failures.shouldBeEmpty()
            }
        }
    }
})

@OptIn(ExperimentalKotest::class)
private suspend fun `check must`(
    req: Constraint<Any>,
    afterChecks: (scope: Jakt.Scope, iterations: Int) -> Unit,
) {
    checkAll(PropTestConfig(iterations = 97), Arb.string(), Arb.nonNegativeInt(50)) { msg, iterations ->
        val scope = Jakt.Scope()
        with(scope.StringScope(msg)) {
            repeat(iterations) {
                must(req)
            }
        }
        scope.results.size shouldBe iterations
        afterChecks(scope, iterations)
    }
}

/**
 * Provides an arbitrary ([Arb]) instance that generates either the [trueConstraint]
 * or the [falseConstraint].
 */
private fun Arb.Companion.requirement() = Arb.element(trueConstraint, falseConstraint)

/**
 * A requirement instance that is always fulfilled, regardless of the input.
 */
private val trueConstraint = object : Constraint<Any> {
    override val validator: (Any) -> Boolean = { true }

    override fun generateException(description: String) =
        ConstraintException { description }
}

/**
 * A requirement instance that is never fulfilled, regardless of the input.
 */
private val falseConstraint = object : Constraint<Any> {
    override val validator: (Any) -> Boolean = { false }

    override fun generateException(description: String) =
        ConstraintException { description }
}
