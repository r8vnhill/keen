/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.selection

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.arbIndividual
import cl.ravenhill.keen.arbPopulation
import cl.ravenhill.keen.evolution.states.arbEvolutionState
import cl.ravenhill.keen.exceptions.SelectionException
import cl.ravenhill.keen.matchers.shouldHaveInfringement
import cl.ravenhill.keen.ranking.FitnessMaxRanker
import cl.ravenhill.keen.ranking.FitnessMinRanker
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.repr.SimpleFeature
import cl.ravenhill.keen.repr.arbSimpleFeature
import cl.ravenhill.keen.repr.arbSimpleRepresentation
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.element
import io.kotest.property.checkAll

class SelectorTest : FreeSpec({

    "A Selector" - {
        "should throw an exception when" - {
            "the population is empty" {
                checkAll(
                    arbSimpleSelector<Double, SimpleFeature<Double>, Representation<Double, SimpleFeature<Double>>>(),
                    arbEvolutionState(
                        arbPopulation(
                            arbIndividual(arbSimpleRepresentation(arbSimpleFeature(Arb.double()))),
                            0..0
                        ),
                        Arb.element(
                            FitnessMaxRanker<Double, SimpleFeature<Double>,
                                    Representation<Double, SimpleFeature<Double>>>(),
                            FitnessMinRanker()
                        )
                    ),
                ) { selector, state ->
                    shouldThrow<CompositeException> {
                        selector(state, 1) { state }
                    }.shouldHaveInfringement<SelectionException>("Population must not be empty")
                }
            }
        }
    }
})

fun <T, F, R> arbSimpleSelector(): Arb<Selector<T, F, R>> where F : Feature<T, F>, R : Representation<T, F> =
    arbitrary {
        object : Selector<T, F, R> {
            override fun select(
                population: Population<T, F, R>,
                count: Int,
                ranker: IndividualRanker<T, F, R>
            ) = ranker.sort(population).take(count)
        }
    }
