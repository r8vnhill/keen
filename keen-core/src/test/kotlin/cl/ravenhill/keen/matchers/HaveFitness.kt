/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.matchers

import cl.ravenhill.keen.Individual
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot

fun haveFitness(fitness: Double) = object : Matcher<Individual<*, *, *>> {
    override fun test(value: Individual<*, *, *>): MatcherResult {
        return MatcherResult(
            value.fitness == fitness,
            { "$value should have fitness $fitness" },
            { "$value should not have fitness $fitness" }
        )
    }
}

infix fun Individual<*, *, *>.shouldHaveFitness(fitness: Double) = this should haveFitness(fitness)

infix fun Individual<*, *, *>.shouldNotHaveFitness(fitness: Double) = this shouldNot haveFitness(fitness)
