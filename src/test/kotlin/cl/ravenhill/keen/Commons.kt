/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen

import cl.ravenhill.enforcer.EnforcementException
import cl.ravenhill.enforcer.UnfulfilledRequirementException
import cl.ravenhill.kuro.Logger
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.set
import io.kotest.property.arbitrary.string
import kotlin.random.Random
import kotlin.reflect.KClass

// region : -====================== SHOULD ASSERTIONS =============================================-
/**
 * A helper function that returns a Matcher instance that matches if the given value is an instance
 * of the given class.
 *
 * ## Usage:
 *
 * ```kotlin
 * 1 shouldBeOfClass Int::class // Assertion passes
 * 1 shouldBeOfClass String::class // Assertion fails
 * ```
 *
 * @param kClass The class to match against.
 * @return A Matcher instance that matches if the given value is an instance of the given class.
 */
infix fun Any.shouldBeOfClass(kClass: KClass<*>) = should(
    Matcher { value ->
        MatcherResult(
            kClass == value::class,
            { "$value should be an instance of $kClass" },
            { "$value should not be an instance of $kClass" }
        )
    }
)

/**
 * A custom assertion that compares two strings while ignoring differences in line breaks.
 *
 * @param expected The expected string to compare against.
 * @throws AssertionError If the strings are not equal.
 */
infix fun String.shouldBeEqualIgnoringBreaks(expected: String) = should(
    Matcher { value ->
        val normalizedValue = value.replace("\r\n", "\n").replace("\r", "\n")
        val normalizedExpected = "$expected\n".replace("\r\n", "\n").replace("\r", "\n")

        MatcherResult(
            normalizedValue == normalizedExpected,
            { "$value should be equal to $expected ignoring line breaks" },
            { "$value should not be equal to $expected ignoring line breaks" }
        )
    }
)

/**
 * Enforces that an [UnfulfilledRequirementException] is present in the
 * [EnforcementException.infringements] list.
 *
 * @param message the message to match against the [UnfulfilledRequirementException].
 * @throws AssertionError if the [EnforcementException.infringements] list does not contain an
 * [UnfulfilledRequirementException] of type [T] with the specified [message].
 */
inline fun <reified T> EnforcementException.shouldHaveInfringement(message: String)
    where T : UnfulfilledRequirementException = should(
    if (infringements.none { it is T }) {
        Matcher { value ->
            MatcherResult(
                false,
                { "$value should have an infringement of type ${T::class.simpleName} with message: $message" },
                { "$value should not have an infringement of type ${T::class.simpleName} with message: $message" }
            )
        }
    } else {
        Matcher { value ->
            val filtered = infringements.filterIsInstance<T>()
            MatcherResult(
                filtered.any { it.message == message },
                {
                    "$value should have an infringement of type ${T::class.simpleName} with message: $message. " +
                        "Actual: $filtered"
                },
                {
                    "$value should not have an infringement of type ${T::class.simpleName} with message: $message. " +
                        "Actual: $filtered"
                }
            )
        }
    }
)
// endregion SHOULD ASSERTIONS

// region : -== ARBITRARY GENERATORS ==-
/**
 * Generates arbitrary instances of [Logger] using the provided [name].
 *
 * @param name the arbitrary generator for the name of the logger.
 * @return an arbitrary instance of [Logger].
 */
fun Arb.Companion.logger(name: Arb<String>) = arbitrary {
    Logger.instance(name.bind())
}

/**
 * Returns an arbitrary generator of a list of [Logger] instances based on a given [names] generator
 * of string lists.
 *
 * The returned generator creates a list of [Logger] instances with names generated by [names].
 * Note that creating an instance of a [Logger] automatically adds it to the [Logger.activeLoggers]
 * list.
 * This function can be used to generate a list of loggers for testing purposes.
 *
 * @param names The generator used to create the list of logger names.
 * @return An arbitrary generator of a list of [Logger] instances.
 */
fun Arb.Companion.loggers(names: Arb<List<String>>) = arbitrary {
    names.bind().map { Logger.instance(it) }
}

/**
 * Returns an arbitrary generator of a list of unique strings.
 * The function generates a set of strings and then converts it to a list, ensuring that
 * there are no repeated strings in the output.
 *
 * @return An arbitrary generator of a list of unique strings.
 */
fun Arb.Companion.uniqueStrings(range: IntRange = 0..100) = arbitrary {
    set(string(), range).bind().toList()
}

/**
 * Returns an arbitrary that generates random instances of [Random] class.
 *
 * @param seed the arbitrary of the seed value to be used for the initialization of the [Random]
 * instance.
 * @return an [Arb] that generates [Random] instances with the given seed.
 */
fun Arb.Companion.random(seed: Arb<Long> = Arb.long()) = arbitrary {
    Random(seed.bind())
}
// endregion ARBITRARY GENERATORS

/**
 * Returns a regular expression pattern that matches log messages with the specified level.
 * The pattern matches lines that start with a datetime string in the format
 * "yyyy-MM-ddTHH:mm:ss[.SSS...]" followed by a thread name in brackets, then the log level, then
 * the logger name, and finally a hyphen (-) followed by the message.
 * The (?s) flag at the beginning of the pattern enables dot-all mode, which makes the dot (.)
 * character match any character, including line breaks.
 *
 * Example of a log message that matches this pattern:
 *
 * ``2023-05-05T10:20:30.123456789 [main] ERROR com.example.app - An error occurred``
 *
 * @param level The log level to match (e.g. "ERROR", "WARN", etc.).
 * @return A regular expression pattern that matches log messages with the specified level.
 */
fun logPattern(level: String) =
    "(?s)^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d{3,9})? \\[.*] $level .* - .*".toRegex()
