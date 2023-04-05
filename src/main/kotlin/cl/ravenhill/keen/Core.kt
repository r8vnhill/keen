package cl.ravenhill.keen

import cl.ravenhill.keen.Core.DEFAULT_MAX_PROGRAM_DEPTH
import cl.ravenhill.keen.Core.maxProgramDepth
import cl.ravenhill.keen.Core.random
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.requirements.CollectionRequirement
import cl.ravenhill.keen.requirements.DoubleRequirement
import cl.ravenhill.keen.requirements.IntRequirement
import cl.ravenhill.keen.requirements.LongRequirement
import cl.ravenhill.keen.requirements.PairRequirement
import cl.ravenhill.keen.util.logging.Level
import cl.ravenhill.keen.util.logging.logger
import cl.ravenhill.keen.util.logging.stdoutChannel
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
     * The scope of the contract enforcement.
     *
     * @property errors The list of errors that occurred during the evaluation.
     * @since 2.0.0
     * @version 2.0.0
     */
    class EnforceScope {
        /**
         * The list of results of evaluating the contract.
         */
        private val results: MutableList<Result<*>> = mutableListOf()

        val errors: List<Throwable>
            get() = results.filter { it.isFailure }.map { it.exceptionOrNull()!! }

        /**
         * Extension function that checks an integer constraint.
         */
        infix fun Int.should(requirement: IntRequirement) =
            results.add(requirement.validate(this))

        /**
         * Extension function that checks a long constraint.
         */
        infix fun Long.should(requirement: LongRequirement) =
            results.add(requirement.validate(this))

        /**
         * Extension function that checks a double constraint.
         */
        infix fun Double.should(constraint: DoubleRequirement) =
            results.add(constraint.validate(this))

        /**
         * Extension function that checks a collection clause.
         */
        infix fun <T> Collection<T>.should(constraint: CollectionRequirement) =
            results.add(constraint.validate(this))

        /**
         * Extension function that checks a pair constraint.
         */
        infix fun <A, B> Pair<A, B>.should(constraint: PairRequirement<A, B>) =
            results.add(constraint.validate(this))

        /**
         * A requirement defined by a predicate.
         *
         * @param description The description of the clause.
         * @param predicate The predicate that defines the clause.
         */
        fun requirement(description: String, predicate: () -> Boolean) = results.add(
            if (predicate()) {
                Result.success(Unit)
            } else {
                Result.failure(UnfulfilledRequirementException { description })
            }
        )
    }

    /**
     * A logger for tracking the evolution of the system.
     *
     * @property level The logging level. Defaults to [Level.Warn].
     * @property logger The logger instance used for logging.
     */
    object EvolutionLogger {
        var level: Level = Level.Warn()
            set(value) {
                field = value
                logger.level = value
            }
        var logger = logger("Evolution") {
            level = Level.Warn()
            stdoutChannel { }
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
typealias Population<DNA> = List<Phenotype<DNA>>

/**
 * A typealias for a mutable list of [Phenotype] objects representing a population of individuals.
 */
typealias MutablePopulation<DNA> = MutableList<Phenotype<DNA>>

/**
 * Rolls a n-dimensional dice.
 */
fun Core.Dice.int(n: Int) = random.nextInt(n)

/**
 * Generates a new random double in [0, 1).
 */
fun Core.Dice.probability() = random.nextDouble()
