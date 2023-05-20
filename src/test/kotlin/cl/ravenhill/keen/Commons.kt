package cl.ravenhill.keen

import cl.ravenhill.keen.util.logging.Logger
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.byte
import io.kotest.property.arbitrary.char
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.float
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.set
import io.kotest.property.arbitrary.short
import io.kotest.property.arbitrary.string
import kotlin.random.Random
import kotlin.reflect.KClass

// region : -== SHOULD ASSERTIONS ==-
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
infix fun Any.shouldBeOfClass(kClass: KClass<*>) = should(Matcher { value ->
    MatcherResult(
        kClass == value::class,
        { "$value should be an instance of $kClass" },
        { "$value should not be an instance of $kClass" }
    )
})

/**
 * A custom assertion that compares two strings while ignoring differences in line breaks.
 *
 * @param expected The expected string to compare against.
 * @throws AssertionError If the strings are not equal.
 */
infix fun String.shouldBeEqualIgnoringBreaks(expected: String) = should(Matcher { value ->
    val normalizedValue = value.replace("\r\n", "\n").replace("\r", "\n")
    val normalizedExpected = "$expected\n".replace("\r\n", "\n").replace("\r", "\n")

    MatcherResult(
        normalizedValue == normalizedExpected,
        { "$value should be equal to $expected ignoring line breaks" },
        { "$value should not be equal to $expected ignoring line breaks" }
    )
})

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
    })
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
 * Generates a pair of values in ascending or descending order based on the `reverted` parameter.
 *
 * This function binds two values, `i` and `j`, from the provided arbitrary generators `a` and `b`
 * respectively.
 * If the value `i` is less than `j` and `reverted` is false, or `i` is more than `j` and `reverted`
 * is true, the function returns the pair `(i, j)`.
 * Otherwise, it returns `(j, i)`.
 * If `strict` is true, it ensures that `i` and `j` are distinct values by re-binding `j` until it
 * is different from `i`.
 *
 * @receiver The `Arb.Companion` object.
 * @param a An [Arb] instance that generates [T] values.
 * @param b An [Arb] instance that generates [T] values.
 * @param strict Whether the pair should be strictly ordered (i.e., `i` is always less than `j`).
 * Defaults to `false`.
 * @param reverted Whether the generated pair should be in descending order.
 * Defaults to `false`.
 * @return An [Arb] instance that generates ordered pairs of [T] values.
 */
fun <T : Comparable<T>> Arb.Companion.orderedPair(
    a: Arb<T>,
    b: Arb<T>,
    strict: Boolean = false,
    reverted: Boolean = false
) = arbitrary {
    val i = a.bind()
    var j = b.bind()

    while (strict && i == j) {
        j = b.bind() // Re-bind `j` until it is different from `i` if `strict` is `true`
    }

    if ((i < j && !reverted) || (i > j && reverted)) i to j else j to i
}

/**
 * Generates a pair of values in ascending or descending order from a single [Arb] instance.
 *
 * This function delegates to the [orderedPair] function that takes two separate [Arb] instances.
 * The same [Arb] instance `gen` is used for both values of the pair, which means that it generates
 * pairs of values from the same distribution.
 *
 * If `strict` is true, it ensures that the pair consists of distinct values.
 * If `reverted` is true, the pair is in descending order.
 *
 * @receiver The `Arb.Companion` object.
 * @param gen An [Arb] instance that generates [T] values.
 * @param strict Whether the pair should be strictly ordered (i.e., both values are distinct).
 * Defaults to `false`.
 * @param reverted Whether the generated pair should be in descending order.
 * Defaults to `false`.
 * @return An [Arb] instance that generates ordered pairs of [T] values.
 */
fun <T : Comparable<T>> Arb.Companion.orderedPair(
    gen: Arb<T>,
    strict: Boolean = false,
    reverted: Boolean = false
) = orderedPair(gen, gen, strict, reverted)

/**
 * Generates a triple of values in ascending or descending order based on the `reverted` parameter.
 *
 * This function binds three values from the provided arbitrary generators `a`, `b`, and `c`.
 * The values are put into a list, sorted, and then used to create a triple.
 * If `strict` is true, it ensures that all three values are distinct.
 * If `reverted` is true, the triple will be in descending order.
 *
 * @receiver The `Arb.Companion` object.
 * @param a An [Arb] instance that generates [T] values.
 * @param b An [Arb] instance that generates [T] values.
 * @param c An [Arb] instance that generates [T] values.
 * @param strict Whether the triple should be strictly ordered (i.e., all three values are
 * distinct).
 * Defaults to `false`.
 * @param reverted Whether the generated triple should be in descending order.
 * Defaults to `false`.
 * @return An [Arb] instance that generates ordered triples of [T] values.
 */
fun <T : Comparable<T>> Arb.Companion.orderedTriple(
    a: Arb<T>,
    b: Arb<T>,
    c: Arb<T>,
    strict: Boolean = false,
    reverted: Boolean = false
) = arbitrary {
    val i = a.bind()
    var j = b.bind()
    var k = c.bind()

    while (strict && i == j) {
        j = b.bind() // Re-bind `j` until it is different from `i` if `strict` is `true`
    }

    while (strict && (i == k || j == k)) {
        k = c.bind() // Re-bind `k` until it is different from `i` and `j` if `strict` is `true`
    }

    val sortedTriple = if (!reverted) {
        listOf(i, j, k).sorted()
    } else {
        listOf(i, j, k).sortedDescending()
    }
    Triple(sortedTriple[0], sortedTriple[1], sortedTriple[2])
}


/**
 * Returns an arbitrary generator that produces values of any type.
 * The generated values can be of the following types:
 * [String], [Int], [Long], [Double], [Float], [Boolean], [Char], [Byte], [Short].
 *
 * All the types are generated with the default generators provided by _Kotest_.
 */
fun Arb.Companion.any() = arbitrary {
    choice(
        string(),
        int(),
        long(),
        double(),
        float(),
        boolean(),
        char(),
        byte(),
        short()
    ).bind()
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

/**
 * Constructs an error message indicating an unfulfilled constraint based on the given
 * [description].
 * This function is typically used when enforcing constraints and reporting constraint violations.
 *
 * @param description the description of the unfulfilled constraint.
 * @return an error message indicating the unfulfilled constraint.
 */
fun unfulfilledConstraint(description: String): String = "Unfulfilled constraint: $description"
