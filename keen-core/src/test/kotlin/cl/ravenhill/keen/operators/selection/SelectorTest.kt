/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.selection

import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.ranking.IndividualRanker
import cl.ravenhill.keen.repr.Feature
import cl.ravenhill.keen.repr.Representation
import cl.ravenhill.keen.repr.SimpleFeature
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.negativeInt
import io.kotest.property.arbitrary.next
import io.kotest.property.checkAll

//class SelectorTest : FreeSpec({
//
//    "A Selector" - {
//        "should throw an exception when" - {
//            "the population is empty" {
//                checkAll(
//                    arbSimpleSelector<Double, SimpleFeature<Double>, Representation<Double, SimpleFeature<Double>>>(),
//                    arbIndividualRanker(),
//                    Arb.int()
//                ) { selector, ranker, count ->
//                    shouldThrow<CompositeException> {
//                        selector(GeneticEvolutionState.empty(ranker), count)
//                    }.shouldHaveInfringement<CollectionConstraintException>(
//                        "Population must not be empty"
//                    )
//                }
//            }
//        }
//    }
//})
//
//fun <T, F, R> arbSimpleSelector(): Arb<Selector<T, F, R>> where F : Feature<T, F>, R : Representation<T, F> =
//    arbitrary {
//        object : Selector<T, F, R> {
//            override fun select(
//                population: Population<T, F, R>,
//                count: Int,
//                ranker: IndividualRanker<T, F, R>
//            ) = ranker.sort(population).take(count)
//        }
//    }
