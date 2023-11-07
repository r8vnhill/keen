/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.assertions.chromosomes

import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.shouldHaveInfringement
import cl.ravenhill.keen.util.Filterable
import cl.ravenhill.keen.util.MutableFilterCollection
import cl.ravenhill.keen.util.MutableRangedCollection
import cl.ravenhill.keen.util.Ranged
import cl.ravenhill.unfulfilledConstraint
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldNotBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.assume
import io.kotest.property.checkAll

/**
 * This function ensures that when creating a chromosome with more than one range,
 * the number of specified ranges must match the number of genes.
 *
 * @param T The type parameter which needs to be comparable and represents the type within the closed range.
 * @param G The gene type parameter. The gene should implement both the Filterable and Ranged interfaces.
 * @param F The chromosome factory type parameter. It should support a mutable collection of ranges.
 *
 * @param arb An Arb instance generating a list of closed ranges.
 * @param factoryBuilder A lambda function that returns a chromosome factory instance.
 */
suspend fun <T, G, F> `assert chromosome enforces range to gene count equality`(
    arb: Arb<ClosedRange<T>>,
    factoryBuilder: () -> F,
) where
      T : Comparable<T>,
      G : Gene<T, G>, G : Filterable<T>, G : Ranged<T>,
      F : Chromosome.Factory<T, G>, F : MutableRangedCollection<T> {
    with(Arb) {
        checkAll(list(arb, 2..100), int(2..100)) { ranges, size ->
            assume {
                ranges.size shouldNotBe size
            }
            val factory = factoryBuilder()
            ranges.forEach { factory.ranges += it }
            factory.size = size
            shouldThrow<cl.ravenhill.jakt.exceptions.CompositeException> {
                factory.make()
            }.shouldHaveInfringement<IntConstraintException>(
                unfulfilledConstraint(
                    "When creating a chromosome with more than one range, the number of ranges " +
                        "must be equal to the number of genes"
                )
            )
        }
    }
}

/**
 * Validates the enforcement that the number of filters applied to a chromosome
 * must match the number of genes in the chromosome.
 *
 * @param T The type parameter which needs to be comparable.
 * @param G The gene type parameter.
 * @param F The chromosome factory type parameter. It should support a mutable collection of filters.
 *
 * @param filter The filter to be applied to the chromosome.
 * @param factoryBuilder A lambda function that returns a chromosome factory instance.
 *
 * @throws AssertionError If the code does not throw an exception when the number of filters
 *                        does not match the number of genes.
 */
suspend fun <T, G, F> `ensure chromosome filter count matches gene count`(
    filter: (T) -> Boolean,
    factoryBuilder: () -> F,
) where T : Comparable<T>, G : Gene<T, G>, F : Chromosome.Factory<T, G>, F : MutableFilterCollection<T> {
    with(Arb) {
        checkAll(int(2..100), int(2..100)) { filtersAmount, size ->
            assume { filtersAmount shouldNotBe size }
            val factory = factoryBuilder()
            repeat(filtersAmount) { factory.filters += filter }
            factory.size = size
            shouldThrow<cl.ravenhill.jakt.exceptions.CompositeException> {
                factory.make()
            }.shouldHaveInfringement<IntConstraintException>(
                unfulfilledConstraint(
                    "When creating a chromosome with more than one filter, the " +
                        "number of filters must be equal to the number of genes"
                )
            )
        }
    }
}
