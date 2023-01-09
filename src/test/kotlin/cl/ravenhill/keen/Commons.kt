package cl.ravenhill.keen

import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import kotlin.reflect.KClass


/**
 * Matcher that checks if the given object is of the given class.
 */
infix fun Any.shouldBeOfClass(kClass: KClass<*>) = Matcher<Any> { value ->
    MatcherResult(
        value::class == kClass,
        { "$value should be an instance of $kClass" },
        { "$value should not be an instance of $kClass" }
    )
}