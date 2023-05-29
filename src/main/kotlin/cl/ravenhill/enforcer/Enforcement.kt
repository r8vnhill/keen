/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.enforcer

import cl.ravenhill.enforcer.requirements.Requirement
import java.util.Objects

object Enforcement {
    var skipChecks = false

    /**
     * Enforces the contract of the given builder.
     *
     * @param builder The builder that contains the contract.
     * @throws EnforcementException If the contract is not fulfilled.
     */
    fun enforce(builder: Scope.() -> Unit) {
        if (skipChecks) return
        Scope().apply(builder).errors.let { errors ->
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
     *
     * @since 2.0.0
     * @version 2.0.0
     */
    class Scope {
        private val _results: MutableList<Result<*>> = mutableListOf()
        val results: List<Result<*>>
            get() = _results

        val errors: List<Throwable>
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
         * @property message The message key for the clause.
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

            /// Documentation inherited from [Any].
            override fun equals(other: Any?) = when {
                other === this -> true
                other is StringScope -> message == other.message
                else -> false
            }

            /// Documentation inherited from [Any].
            override fun hashCode() = Objects.hash(StringScope::class, message)

            /// Documentation inherited from [Any].
            override fun toString() = "StringScope(message='$message')"
        }
    }
}