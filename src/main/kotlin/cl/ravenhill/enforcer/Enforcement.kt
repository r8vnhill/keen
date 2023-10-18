/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.enforcer

import cl.ravenhill.enforcer.requirements.Requirement

/**
 * Provides utility methods and inner classes for enforcing contracts.
 *
 * `Enforcement` simplifies contract validation by offering a declarative syntax
 * and meaningful error messages.
 *
 * @property skipChecks A flag indicating if contract validation should be skipped.
 *                      If set to `true`, contract enforcement will be bypassed.
 *                      Use with caution; this might be useful for scenarios like testing,
 *                      but might have implications if misused in production.
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
object Enforcement {
    var skipChecks = false

    /**
     * Enforces the contract of the given builder.
     *
     * ## Example: Enforcing a contract
     *
     * ```kotlin
     *  data class Person(val name: String, val age: Int) {
     *      init {
     *          enforce {
     *              "Name must not be empty" { name mustNot BeEmpty }
     *              "Age must be greater than 0" { age must BePositive }
     *          }
     *      }
     *  }
     * ```
     *
     * @param builder The builder that contains the contract.
     * @throws EnforcementException If the contract is not fulfilled.
     */
    inline fun enforce(builder: Scope.() -> Unit) {
        if (skipChecks) return
        Scope().apply(builder).failures.let { errors ->
            if (errors.isNotEmpty()) {
                throw EnforcementException(errors)
            }
        }
    }

    /**
     * A utility class for enforcing contracts.
     *
     * An instance of this class can be used to enforce a contract by defining clauses using string
     * literals as message keys and lambda expressions that define the predicate.
     * Each clause defines a requirement, which can be validated by calling the `validate()` method
     * of a [Requirement] instance.
     *
     * @property results The list of results of evaluating the contract.
     * @property failures The list of exceptions thrown by the contract.
     *
     * @since 2.0.0
     * @version 2.0.0
     */
    class Scope {
        private val _results: MutableList<Result<*>> = mutableListOf()
        val results: List<Result<*>>
            get() = _results

        val failures: List<Throwable>
            get() = _results.filter { it.isFailure }.map { it.exceptionOrNull()!! }

        /**
         * Defines a clause of a contract.
         *
         * @receiver The message key for the clause.
         * @param value A lambda expression that defines the predicate for the clause.
         *
         * @return A [StringScope] instance that can be used to define a [Requirement] for the clause.
         */
        inline operator fun String.invoke(value: StringScope.() -> Boolean) =
            StringScope(this).apply { value() }

        /**
         * A scope for defining a [Requirement] for a contract clause.
         *
         * @property message The message key associated with the clause.
         */
        inner class StringScope(val message: String) {

            /**
             * Property that returns the outer `EnforceScope` instance.
             */
            internal val outerScope: Scope
                get() = this@Scope

            /**
             * Infix function that validates that the current value satisfies the specified
             * requirement.
             *
             * @param requirement the requirement to validate against.
             * @receiver the current value to be validated.
             *
             * @see Requirement.validate
             */

            infix fun <T, R : Requirement<T>> T.must(requirement: R) =
                _results.add(requirement.validate(this, message))

            /**
             * Infix function that validates that the current value does not satisfy the specified
             * requirement.
             *
             * @param requirement the requirement to validate against.
             * @receiver the current value to be validated.
             *
             * @see Requirement.validateNot
             */
            infix fun <T, R : Requirement<T>> T.mustNot(requirement: R) =
                _results.add(requirement.validateNot(this, message))

            /**
             * Defines a [Requirement] based on a predicate.
             *
             * @param predicate The predicate that defines the clause.
             */
            fun requirement(predicate: () -> Boolean) = _results.add(
                if (predicate()) {
                    Result.success(Unit)
                } else {
                    Result.failure(UnfulfilledRequirementException { message })
                }
            )

            /**
             * Represents the clause as a string using its message key.
             */
            override fun toString() = "StringScope(message='$message')"
        }
    }
}
