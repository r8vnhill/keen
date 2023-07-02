/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.enforcer

import cl.ravenhill.enforcer.requirements.Requirement
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.property.Arb
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.list
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
                    checkAll<String> {
                        val scope = Enforcement.Scope()
                        with(scope.StringScope(it)) {
                            repeat(100) {

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
                    checkAll(Arb.string(), Arb.nonNegativeInt(100)) { msg, iterations ->
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

private suspend fun `check must`(
    req: Requirement<Any>,
    afterChecks: (scope: Enforcement.Scope, iterations: Int) -> Unit,
) {
    checkAll(Arb.string(), Arb.nonNegativeInt(100)) { msg, iterations ->
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
