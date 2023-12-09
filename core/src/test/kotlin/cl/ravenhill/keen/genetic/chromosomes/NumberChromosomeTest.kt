/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.jakt.exceptions.CollectionConstraintException
import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.arb.genetic.chromosomes.numberChromosomeFactory
import cl.ravenhill.keen.arb.genetic.datatypes.orderedPair
import cl.ravenhill.keen.arb.genetic.genes.doubleGene
import cl.ravenhill.keen.assertions.shouldHaveInfringement
import io.kotest.assertions.fail
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import io.kotest.property.checkAll

class NumberChromosomeTest : FreeSpec({

    "A Number Chromosome Factory" - {
        "when enforcing constraints" - {
            "should throw an exception if the number of ranges is different from the number of genes" {
                checkAll(Arb.numberChromosomeFactory(Arb.doubleGene()).map {
                    it to Arb.list(Arb.orderedPair(Arb.double()), 1..100).next()
                }.filter { (factory, ranges) ->
                    factory.size != ranges.size
                }) { (factory, ranges) ->
                    factory.ranges += ranges.map { it.first..it.second }
                    shouldThrow<CompositeException> {
                        factory.enforceConstraints()
                    }.shouldHaveInfringement<CollectionConstraintException>(
                        "Chromosome with multiple ranges must have the same number of genes as ranges"
                    )
                }
            }

            "should throw an exception if the number of filters is different from the number of genes" {
                checkAll(Arb.numberChromosomeFactory(Arb.doubleGene()).map {
                    it to Arb.list(Arb.double(), 1..100).next()
                }.filter { (factory, filters) ->
                    factory.size != filters.size && filters.size > 1
                }) { (factory, filters) ->
                    factory.filters += filters.map { { it > 0 } }
                    shouldThrow<CompositeException> {
                        factory.enforceConstraints()
                    }.shouldHaveInfringement<CollectionConstraintException>(
                        "Chromosome with multiple filters must have the same number of genes as filters"
                    )
                }
            }

            "should throw an exception if the number of ranges and filters is different from the number of genes" {
                checkAll(Arb.numberChromosomeFactory(Arb.doubleGene()).map {
                    it to Arb.list(Arb.orderedPair(Arb.double()), 1..100).next()
                }.filter { (factory, ranges) ->
                    factory.size != ranges.size
                }) { (factory, ranges) ->
                    factory.ranges += ranges.map { it.first..it.second }
                    factory.filters += ranges.map { { it > 0 } }
                    shouldThrow<CompositeException> {
                        factory.enforceConstraints()
                    }.shouldHaveInfringement<CollectionConstraintException>(
                        "Chromosome with multiple ranges must have the same number of genes as ranges"
                    ).shouldHaveInfringement<CollectionConstraintException>(
                        "Chromosome with multiple filters must have the same number of genes as filters"
                    )
                }
            }

            "should not throw an exception if the number of ranges and filters is the same as the number of genes" {
                checkAll(Arb.numberChromosomeFactory(Arb.doubleGene()).map {
                    it to Arb.list(Arb.orderedPair(Arb.double()), it.size..it.size).next()
                }.filter { (factory, ranges) ->
                    factory.size == ranges.size
                }) { (factory, ranges) ->
                    factory.ranges += ranges.map { it.first..it.second }
                    factory.filters += ranges.map { { it > 0 } }
                    factory.enforceConstraints()
                }
            }
        }
    }
})
