/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.enforcer

import cl.ravenhill.enforcer.requirements.Requirement
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

class EnforcementTest : FreeSpec({
    beforeEach {
        Enforcement.skipChecks = false
    }

    "The skip checks flag" - {
        "has a default value of false" {
            Enforcement.skipChecks shouldBe false
        }

        "can be set to true" {
            Enforcement.skipChecks shouldBe false
            Enforcement.skipChecks = true
            Enforcement.skipChecks shouldBe true
        }
    }

    "Enforcing a requirement" - {
        "performs no checks if the skip checks flag is set to true" {
            Enforcement.skipChecks = true
            checkAll(Arb.list(Arb.pair(Arb.string(), Arb.requirement()), 0..50)) { strings ->
                Enforcement.enforce {
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
                    Enforcement.enforce {
                        strings.forEach { msg ->
                            msg.invoke { must(trueRequirement) }
                        }
                    }
                }
            }
        }
        "throws an exception if the requirement is not fulfilled" {
            checkAll(Arb.list(Arb.string())) { strings ->
                assume { strings.shouldNotBeEmpty() }
                shouldThrow<EnforcementException> {
                    Enforcement.enforce {
                        strings.forEach { msg ->
                            msg.invoke { must(falseRequirement) }
                        }
                    }
                }
            }
        }
    }

    "The [Enforcement.Scope]" - {
        "has a list of" - {
            "[Result]s that is empty by default" {
                Enforcement.Scope().results.shouldBeEmpty()
            }
            "[Failure]s that is empty by default" {
                Enforcement.Scope().failures.shouldBeEmpty()
            }
        }
        "has a [StringScope] that" - {
            "can be created with a message" {
                checkAll<String> { msg ->
                    Enforcement.Scope().StringScope(msg).message shouldBe msg
                }
            }

            "can access its outer scope" {
                checkAll<String> { msg ->
                    val scope = Enforcement.Scope()
                    scope.StringScope(msg).outerScope shouldBeSameInstanceAs scope
                }
            }

            "can be converted to [String]" {
                checkAll<String> { msg ->
                    Enforcement.Scope().StringScope(msg)
                        .toString() shouldBe "StringScope(message='$msg')"
                }
            }

            "can validate a `must` requirement when" - {
                "the predicate is true" {
                    `check must`(trueRequirement) { scope, _ ->
                        scope.results.forEach { it.shouldBeSuccess() }
                        scope.failures.shouldBeEmpty()
                    }
                }

                "the predicate is false" {
                    `check must`(falseRequirement) { scope, iterations ->
                        scope.results.forEach { it.shouldBeFailure() }
                        scope.failures shouldHaveSize iterations
                    }
                }

                "the predicate is true for some iterations and false for others" {
                    checkAll(Arb.list(Arb.pair(Arb.string(), Arb.requirement()), 0..50)) { enforced ->
                        val scope = Enforcement.Scope()
                        with(scope) {
                            enforced.forEach { (msg, req) ->
                                msg.invoke { must(req) }
                            }
                        }
                        enforced.map { it.second }.zip(scope.results).forEach { (req, res) ->
                            when (req) {
                                trueRequirement -> res.shouldBeSuccess()
                                else -> res.shouldBeFailure()
                            }
                        }
                    }
                }
            }

            "can validate a `mustNot` requirement when" - {
                "the predicate is true" {
                    checkAll(Arb.string(), Arb.nonNegativeInt(100)) { msg, iterations ->
                        val scope = Enforcement.Scope()
                        with(scope.StringScope(msg)) {
                            repeat(iterations) {
                                mustNot(trueRequirement)
                            }
                        }
                        scope.results.size shouldBe iterations
                        scope.results.forEach { it.shouldBeFailure() }
                        scope.failures.size shouldBe iterations
                    }
                }
                "the predicate is false" {
                    checkAll(Arb.string(), Arb.int(0..50)) { msg, iterations ->
                        val scope = Enforcement.Scope()
                        with(scope.StringScope(msg)) {
                            repeat(iterations) {
                                mustNot(falseRequirement)
                            }
                        }
                        scope.results.size shouldBe iterations
                        scope.results.forEach { it.shouldBeSuccess() }
                        scope.failures.shouldBeEmpty()
                    }
                }

                "the predicate is true for some iterations and false for others" {
                    checkAll(Arb.list(Arb.pair(Arb.string(), Arb.requirement()))) { enforced ->
                        val scope = Enforcement.Scope()
                        with(scope) {
                            enforced.forEach { (msg, req) ->
                                msg.invoke { mustNot(req) }
                            }
                        }
                        enforced.map { it.second }.zip(scope.results).forEach { (req, res) ->
                            when (req) {
                                trueRequirement -> res.shouldBeFailure()
                                else -> res.shouldBeSuccess()
                            }
                        }
                    }
                }
            }

            "can validate a _predicate requirement_ when" - {
                "the predicate is true" {
                    checkAll(Arb.string(), Arb.nonNegativeInt(100)) { msg, iterations ->
                        val scope = Enforcement.Scope()
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
                        val scope = Enforcement.Scope()
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
                val scope = Enforcement.Scope()
                with(scope) {
                    msg.invoke {
                        must(trueRequirement)
                        mustNot(falseRequirement)
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
    req: Requirement<Any>,
    afterChecks: (scope: Enforcement.Scope, iterations: Int) -> Unit,
) {
    checkAll(PropTestConfig(iterations = 97), Arb.string(), Arb.nonNegativeInt(50)) { msg, iterations ->
        val scope = Enforcement.Scope()
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
 * Provides an arbitrary ([Arb]) instance that generates either the [trueRequirement]
 * or the [falseRequirement].
 */
private fun Arb.Companion.requirement() = Arb.element(trueRequirement, falseRequirement)

/**
 * A requirement instance that is always fulfilled, regardless of the input.
 */
private val trueRequirement = object : Requirement<Any> {
    override val validator: (Any) -> Boolean = { true }

    override fun generateException(description: String) =
        UnfulfilledRequirementException { description }
}

/**
 * A requirement instance that is never fulfilled, regardless of the input.
 */
private val falseRequirement = object : Requirement<Any> {
    override val validator: (Any) -> Boolean = { false }

    override fun generateException(description: String) =
        UnfulfilledRequirementException { description }
}
