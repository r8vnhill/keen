/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.enforcer

import cl.ravenhill.enforcer.requirements.Requirement
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.nonNegativeInt
import io.kotest.property.arbitrary.string
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
                    val req = object : Requirement<Any> {
                        override val validator: (Any) -> Boolean = { true }

                        override fun generateException(description: String) =
                            UnfulfilledRequirementException { description }
                    }
                    checkAll(Arb.string(), Arb.nonNegativeInt(100)) { msg, iterations ->
                        val scope = Enforcement.Scope()
                        with(scope.StringScope(msg)) {
                            repeat(iterations) {
                                must(req)
                            }
                        }
                        scope.results.size shouldBe iterations
                        scope.results.forEach { it.shouldBeSuccess() }
                        scope.failures.shouldBeEmpty()
                    }
                }
                "the predicate is false" {
                    val req = object : Requirement<Any> {
                        override val validator: (Any) -> Boolean = { false }

                        override fun generateException(description: String) =
                            UnfulfilledRequirementException { description }
                    }
                    checkAll(Arb.string(), Arb.nonNegativeInt(100)) { msg, iterations ->
                        val scope = Enforcement.Scope()
                        with(scope.StringScope(msg)) {
                            repeat(iterations) {
                                must(req)
                            }
                        }
                        scope.results.size shouldBe iterations
                        scope.results.forEach { it.shouldBeFailure() }
                        scope.failures.size shouldBe iterations
                    }
                }
            }

            "can validate a `mustNot` requirement when" - {
                "the predicate is true" {
                    val req = object : Requirement<Any> {
                        override val validator: (Any) -> Boolean = { true }

                        override fun generateException(description: String) =
                            UnfulfilledRequirementException { description }
                    }
                    checkAll(Arb.string(), Arb.nonNegativeInt(100)) { msg, iterations ->
                        val scope = Enforcement.Scope()
                        with(scope.StringScope(msg)) {
                            repeat(iterations) {
                                mustNot(req)
                            }
                        }
                        scope.results.size shouldBe iterations
                        scope.results.forEach { it.shouldBeFailure() }
                        scope.failures.size shouldBe iterations
                    }
                }
                "the predicate is false" {
                    val req = object : Requirement<Any> {
                        override val validator: (Any) -> Boolean = { false }

                        override fun generateException(description: String) =
                            UnfulfilledRequirementException { description }
                    }
                    checkAll(Arb.string(), Arb.nonNegativeInt(100)) { msg, iterations ->
                        val scope = Enforcement.Scope()
                        with(scope.StringScope(msg)) {
                            repeat(iterations) {
                                mustNot(req)
                            }
                        }
                        scope.results.size shouldBe iterations
                        scope.results.forEach { it.shouldBeSuccess() }
                        scope.failures.shouldBeEmpty()
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
    }
})