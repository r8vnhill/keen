/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.selection

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right
import cl.ravenhill.InvalidGeneratorException
import cl.ravenhill.SimpleFeature
import cl.ravenhill.SimpleRepresentation
import cl.ravenhill.and
import cl.ravenhill.arbNamed
import cl.ravenhill.arbNonEmptyPopulation
import cl.ravenhill.arbPopulation
import cl.ravenhill.arbSimpleFeature
import cl.ravenhill.arbSimpleRepresentation
import cl.ravenhill.jakt.constrainedTo
import cl.ravenhill.jakt.constraints.collections.HaveSize
import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.emptyPopulation
import cl.ravenhill.keen.evolution.states.SimpleEvolutionStateShrinker
import cl.ravenhill.keen.evolution.states.arbEvolutionState
import cl.ravenhill.keen.exceptions.InvalidSizeException
import cl.ravenhill.keen.exceptions.SelectionException
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.toPopulation
import cl.ravenhill.matchers.shouldBeLeft
import cl.ravenhill.matchers.shouldContainExceptionOfType
import cl.ravenhill.utils.arbIndividual
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.throwable.shouldHaveMessage
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.arbitrary.IntShrinker
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.flatMap
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.checkAll

class SelectorTest : FreeSpec({
    "A Selector" - {
        "when invoking" - {
            "should return an exception if the population is empty" { shouldReturnExceptionIfPopulationIsEmpty() }

            "should return an exception if the expected output size is negative" {
                shouldReturnExceptionIfNegativeOutputSize()
            }

            "should return an exception if an error occurs during selection" {
                shouldReturnExceptionIfErrorOccursDuringSelection()
            }
        }
    }
}) {
    companion object {

        /**
         * Tests if the selection process correctly returns a `SelectionException` when the population is empty.
         */
        private suspend fun shouldReturnExceptionIfPopulationIsEmpty() {
            val stateArb = arbEvolutionState(
                Arb.constant(
                    emptyPopulation<Int, SimpleFeature, SimpleRepresentation<Int, SimpleFeature>>()
                )
            )
            checkAll(stateArb, Arb.int()) { state, count ->
                val selector = SimpleSelector<_, _, SimpleRepresentation<Int, SimpleFeature>>()
                selector(state, count) { state.makeCopy(population = emptyPopulation()) }
                    .shouldBeLeft()
                    .apply {
                        shouldHaveMessage("Invalid selection parameters")
                        cause
                            .shouldBeInstanceOf<CompositeException>()
                            .shouldContainExceptionOfType<CollectionConstraintException>(
                                "Population must not be empty"
                            )
                    }
            }
        }

        /**
         * Tests if the selection process correctly returns a `SelectionException` when the output size is negative.
         */
        private suspend fun shouldReturnExceptionIfNegativeOutputSize() {
            val individualArb = arbIndividual(arbSimpleRepresentation(arbSimpleFeature()))
            val populationArb = arbPopulation(individualArb)
            val stateArb = arbEvolutionState(populationArb)
            checkAll(stateArb, Arb.negativeInt()) { population, count ->
                val selector = SimpleSelector<_, _, SimpleRepresentation<Int, SimpleFeature>>()
                selector(population, count) { population }
                    .shouldBeLeft()
                    .apply {
                        shouldHaveMessage("Invalid selection parameters")
                        cause
                            .shouldBeInstanceOf<CompositeException>()
                            .shouldContainExceptionOfType<InvalidSizeException>(
                                "Selection count ($count) must not be negative"
                            )
                    }
            }
        }

        /**
         * Tests if the selection process correctly returns a `SelectionException` when an error occurs during
         * selection.
         */
        private suspend fun shouldReturnExceptionIfErrorOccursDuringSelection() {
            val individualArb = arbIndividual(arbSimpleRepresentation(arbSimpleFeature()))
            val populationArb = arbNonEmptyPopulation(individualArb)
            val stateAndSizeArb = arbNamed("state", arbEvolutionState(populationArb), SimpleEvolutionStateShrinker())
                .flatMap { namedState ->
                    val (_, state) = namedState
                    arbNamed(
                        "size",
                        Arb.int(state.size + 1..Int.MAX_VALUE),
                        IntShrinker(state.size + 1..Int.MAX_VALUE)
                    ).map { namedSize -> namedState and namedSize }
                }
            checkAll(stateAndSizeArb) { (population, count) ->
                val selector = SimpleSelector<_, _, SimpleRepresentation<Int, SimpleFeature>>()
                selector(population.unwrap(), count.unwrap()) { population.unwrap() }
                    .shouldBeLeft()
                    .apply {
                        shouldHaveMessage("Invalid selection parameters")
                        cause
                            .shouldBeInstanceOf<CompositeException>()
                            .shouldContainExceptionOfType<CollectionConstraintException>(
                                "The population size must be greater than or equal to the selection count"
                            )
                    }
            }
        }
    }
}

/**
 * A simple selection operator for evolutionary algorithms.
 *
 * @param T The type of the value held by the features in the representation.
 * @param F The type of feature, which must extend [Feature].
 * @param R The type of representation, which must extend [Representation].
 * @throws IllegalArgumentException if the population size is less than the selection count.
 */
private class SimpleSelector<T, F, R> : Selector<T, F, R> where F : Feature<T, F>, R : Representation<T, F> {

    /**
     * Selects a specified number of individuals from the population.
     *
     * @param population The population of individuals from which to select.
     * @param count The number of individuals to select.
     * @param ranker The ranker used to evaluate and compare individuals (not used in this simple selector).
     * @return An [Either] containing the selected population on the right, or a [SelectionException] on the left if the
     *   selection fails.
     * @throws InvalidGeneratorException if the population size is less than the selection count.
     */
    override suspend fun select(
        population: Population<T, F, R>,
        count: Int,
        ranker: IndividualRanker<T, F, R>
    ): Either<SelectionException, Population<T, F, R>> = population
        .constrainedTo {
            "The population size must be greater than or equal to the selection count" {
                it must HaveSize { size -> size >= count }
            }
        }
        .getOrElse { return SelectionException("Invalid selection parameters", it).left() }
        .take(count)
        .toPopulation()
        .right()
}
