/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen

import cl.ravenhill.keen.Core.DEFAULT_MAX_PROGRAM_DEPTH
import cl.ravenhill.keen.Core.maxProgramDepth
import cl.ravenhill.keen.Core.random
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.util.logging.Level
import cl.ravenhill.keen.util.logging.logger
import cl.ravenhill.keen.util.logging.stdoutChannel
import kotlin.random.Random

/**
 * The `Core` object contains the functions and variables that are used by the rest of the library.
 *
 * @property logger The logger used by the library.
 * @property maxProgramDepth The maximum depth of a program tree.
 * @property DEFAULT_MAX_PROGRAM_DEPTH The default maximum depth of a program tree (7).
 * @property random The random number generator.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 */
object Core {
    const val DEFAULT_MAX_PROGRAM_DEPTH = 7
    var maxProgramDepth = DEFAULT_MAX_PROGRAM_DEPTH
    var random: Random = Random.Default

    /**
     * Checks that a given contract is fulfilled.
     *
     * @param builder The contract builder.
     * @throws UnfulfilledContractException If the contract is not fulfilled.
     */
    fun contracts(builder: ContractContext.() -> Unit) {
        ContractContext().apply(builder).errors.let { errors ->
            if (errors.isNotEmpty()) {
                throw UnfulfilledContractException(errors)
            }
        }
    }

    /**
     * A contract context that contains the results of evaluating the contract.
     *
     * @property errors The list of errors that occurred during the evaluation.
     * @since 2.0.0
     * @version 2.0.0
     */
    class ContractContext {
        /**
         * The list of results of evaluating the contract.
         */
        private val results: MutableList<Result<*>> = mutableListOf()

        val errors: List<Throwable>
            get() = results.filter { it.isFailure }.map { it.exceptionOrNull()!! }

        /**
         * Extension function that checks an integer constraint.
         */
        infix fun Int.should(clause: IntClause) =
            results.add(clause.validate(this))

        /**
         * Extension function that checks a double constraint.
         */
        infix fun Double.should(constraint: DoubleClause) =
            results.add(constraint.validate(this))

        /**
         * Extension function that checks a collection clause.
         */
        infix fun <T> Collection<T>.should(constraint: CollectionClause) =
            results.add(constraint.validate(this))

        /**
         * Extension function that checks a pair constraint.
         */
        infix fun <A, B> Pair<A, B>.should(constraint: PairClause<A, B>) =
            results.add(constraint.validate(this))

        /**
         * A clause defined by a predicate.
         *
         * @param description The description of the clause.
         * @param predicate The predicate that defines the clause.
         */
        fun clause(description: String, predicate: () -> Boolean) = results.add(
            if (predicate()) {
                Result.success(Unit)
            } else {
                Result.failure(UnfulfilledClauseException { description })
            }
        )
    }

    object EvolutionLogger {
        var level: Level = Level.Warn()
            set(value) {
                field = value
                logger.level = value
            }
        var logger = logger("Evolution") {
            level = Level.Warn()
            stdoutChannel {
            }
        }

        /**
         * Logs a message at the Trace level.
         */
        fun trace(lazyMessage: () -> String) = logger.trace(lazyMessage)

        /**
         * Logs a message at the Debug level.
         */
        fun debug(lazyMessage: () -> String) = logger.debug(lazyMessage)

        /**
         * Logs a message at the Info level.
         */
        fun info(lazyMessage: () -> String) = logger.info(lazyMessage)
    }
}

typealias Population<DNA> = List<Phenotype<DNA>>
