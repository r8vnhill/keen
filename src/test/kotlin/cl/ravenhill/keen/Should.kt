package cl.ravenhill.keen

import cl.ravenhill.keen.util.DoubleRange
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot

private fun beInRange(range: DoubleRange) = object : Matcher<Double> {
    override fun test(value: Double): MatcherResult {
        return MatcherResult(
            value in range,
            { "$value should be in range $range" },
            { "$value should not be in range $range" }
        )
    }
}

infix fun Double.shouldBeInRange(range: DoubleRange) = this should beInRange(range)

infix fun Double.shouldNotBeInRange(range: DoubleRange) = this shouldNot beInRange(range)