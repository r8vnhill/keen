/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.matchers

import cl.ravenhill.keen.evolution.states.EvolutionState
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import io.kotest.matchers.shouldNot

/**
 * Creates a matcher that checks if an [EvolutionState] is empty.
 *
 * @param T The type of value stored by the feature.
 * @param F The kind of feature stored in a representation.
 * @param R The type of representation used by the individual.
 * @param S The type of evolution state to be matched.
 *
 * @return A [Matcher] that tests if the [EvolutionState] is empty.
 */
fun <T, F, R, S> beEmpty() where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R, S> =
    object : Matcher<S> {
        override fun test(value: S) = MatcherResult(
            value.isEmpty(),
            { "Population should be empty" },
            { "Population should not be empty" }
        )
    }

/**
 * Asserts that the [EvolutionState] is empty.
 *
 * @param T The type of value stored by the feature.
 * @param F The kind of feature stored in a representation.
 * @param R The type of representation used by the individual.
 * @param S The type of evolution state.
 *
 * @receiver [EvolutionState] instance to be checked.
 */
fun <T, F, R, S> EvolutionState<T, F, R>.shouldBeEmpty()
        where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R, S> = this should beEmpty()

/**
 * Asserts that the [EvolutionState] is not empty.
 *
 * @param T The type of value stored by the feature.
 * @param F The kind of feature stored in a representation.
 * @param R The type of representation used by the individual.
 * @param S The type of evolution state.
 *
 * @receiver [EvolutionState] instance to be checked.
 */
fun <T, F, R, S> EvolutionState<T, F, R>.shouldNotBeEmpty()
        where F : Feature<T, F>, R : Representation<T, F>, S : EvolutionState<T, F, R, S> = this shouldNot beEmpty()
