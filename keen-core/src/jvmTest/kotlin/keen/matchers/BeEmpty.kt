/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package keen.matchers

import cl.ravenhill.keen.evolution.states.EvolutionState
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot

/**
 * Creates a matcher that checks if an `EvolutionState` is empty.
 *
 * @return A matcher that checks if an `EvolutionState` is empty.
 */
fun beEmpty() = Matcher<EvolutionState<*, *, *, *>> {
    MatcherResult(
        it.isEmpty(),
        { "State should be empty" },
        { "State should not be empty" }
    )
}

/**
 * Asserts that an `EvolutionState` is empty.
 *
 * @receiver The `EvolutionState` to be checked.
 * @return The original `EvolutionState` for further assertions or operations.
 */
fun EvolutionState<*, *, *, *>.shouldBeEmpty(): EvolutionState<*, *, *, *> {
    this should beEmpty()
    return this
}

/**
 * Asserts that an `EvolutionState` is not empty.
 *
 * @receiver The `EvolutionState` to be checked.
 * @return The original `EvolutionState` for further assertions or operations.
 */
fun EvolutionState<*, *, *, *>.shouldNotBeEmpty(): EvolutionState<*, *, *, *> {
    this shouldNot beEmpty()
    return this
}
