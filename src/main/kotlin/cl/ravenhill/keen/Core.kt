package cl.ravenhill.keen

import cl.ravenhill.keen.Core.DEFAULT_MAX_PROGRAM_DEPTH
import cl.ravenhill.keen.Core.EvolutionLogger.level
import cl.ravenhill.keen.Core.EvolutionLogger.logger
import cl.ravenhill.keen.Core.maxProgramDepth
import cl.ravenhill.keen.Core.random
import cl.ravenhill.keen.Core.skipChecks
import cl.ravenhill.keen.evolution.Evolver
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.requirements.IntRequirement.BePositive
import cl.ravenhill.keen.requirements.Requirement
import cl.ravenhill.keen.util.logging.Level
import cl.ravenhill.keen.util.logging.logger
import cl.ravenhill.keen.util.logging.stdoutChannel
import java.util.Objects
import kotlin.random.Random

/***************************************************************************************************
 * The Core object contains various properties and functions that are used by the rest of the
 * library.
 * It includes a logger, the maximum depth of a program tree, a random number generator, and a
 * boolean that can skip all checks.
 * The EnforceScope inner class is used to enforce contracts with different constraint requirements,
 * and the EvolutionLogger object is a logger that tracks the system's evolution.
 * The Dice object rolls an n-dimensional or continuous dice.
 * Additionally, there are two type-aliases: Population and MutablePopulation, which represent a
 * list of Phenotype objects that represent a population of individuals with mutable or immutable
 * data.
 * Lastly, there are two extension functions: int and probability, which generate a random integer
 * and double in the given ranges.
 **************************************************************************************************/

/**
 * The `Core` object contains the functions and variables that are used by the rest of the library.
 *
 * @property logger The logger used by the library.
 * @property maxProgramDepth The maximum depth of a program tree.
 * @property DEFAULT_MAX_PROGRAM_DEPTH The default maximum depth of a program tree (7).
 * @property random The random number generator.
 * @property skipChecks If true, the library will skip all the checks.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 1.0.0
 */
object Core {
    const val DEFAULT_MAX_PROGRAM_DEPTH = 7
    var maxProgramDepth = DEFAULT_MAX_PROGRAM_DEPTH
        set(value) {
            enforce { "The maximum program depth must be positive" { value should BePositive } }
            field = value
        }
    var random: Random = Random.Default
    var skipChecks = false

    /**
     * Represents the "roll" of an n-dimensional or continuous dice.
     */
    object Dice {

        /**
         * Backing [Random] instance.
         */
        var random: Random = Random.Default
    }

    /**
     * Enforces the contract of the given builder.
     *
     * @param builder The builder that contains the contract.
     * @throws EnforcementException If the contract is not fulfilled.
     */
    fun enforce(builder: EnforceScope.() -> Unit) {
        if (skipChecks) return
        EnforceScope().apply(builder).errors.let { errors ->
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
    class EnforceScope {
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
        operator fun String.invoke(value: StringScope.() -> Boolean) =
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
            internal val outerScope: EnforceScope
                get() = this@EnforceScope

            /**
             * Defines a [Requirement] for a contract clause.
             *
             * @param requirement The [Requirement] instance to validate.
             * @return A [Result] instance representing the result of the validation.
             */
            infix fun <T, R : Requirement<T>> T.should(requirement: R) =
                _results.add(requirement.validate(this, message))

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
            override fun equals(other: Any?) = when (other) {
                is StringScope -> message == other.message
                else -> false
            }

            /// Documentation inherited from [Any].
            override fun hashCode() = Objects.hash(StringScope::class, message)
        }
    }

    /**
     * A logger for tracking the evolution of [Evolver] instances.
     *
     * @property DEFAULT_LEVEL The default logging level ([Level.Warn]).
     * @property level The logging level. Defaults to [DEFAULT_LEVEL].
     * @property logger The logger instance used for logging.
     */
    object EvolutionLogger {
        val DEFAULT_LEVEL: Level = Level.Warn()
        var level: Level = DEFAULT_LEVEL
            set(value) {
                field = value
                logger.level = value
            }
        var logger = logger("Evolution") {
            level = Level.Warn()
            stdoutChannel()
        }

        /**
         * Logs a message at the Trace level.
         *
         * @param lazyMessage A lambda that returns the message to be logged.
         */
        fun trace(lazyMessage: () -> String) = logger.trace(lazyMessage)

        /**
         * Logs a message at the Debug level.
         *
         * @param lazyMessage A lambda that returns the message to be logged.
         */
        fun debug(lazyMessage: () -> String) = logger.debug(lazyMessage)

        /**
         * Logs a message at the Info level.
         *
         * @param lazyMessage A lambda that returns the message to be logged.
         */
        fun info(lazyMessage: () -> String) = logger.info(lazyMessage)
    }
}

/**
 * A typealias for a list of [Phenotype] objects representing a population of individuals.
 */
typealias Population<DNA, G> = List<Phenotype<DNA, G>>

/**
 * Rolls a n-dimensional dice.
 */
fun Core.Dice.int(n: Int) = random.nextInt(n)

/**
 * Generates a new random double in [0, 1).
 */
fun Core.Dice.probability() = random.nextDouble()
