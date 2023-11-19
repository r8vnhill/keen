/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.selector

import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.arbs.genetic.population
import cl.ravenhill.keen.arbs.optimizer
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.shouldHaveInfringement
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.nonPositiveInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.checkAll
import kotlin.random.Random

class TournamentSelectorTest : FreeSpec({
    "A [TournamentSelector]" - {
        "when created" - {
            "should throw an exception if the tournament size is negative" {
                checkAll(Arb.nonPositiveInt()) { size ->
                    shouldThrow<cl.ravenhill.jakt.exceptions.CompositeException> {
                        TournamentSelector<Int, IntGene>(size)
                    }.shouldHaveInfringement<IntConstraintException>(
                        "The sample size [$size] must be positive"
                    )
                }
            }
        }

        "can be converted to a string" {
            checkAll(Arb.positiveInt()) { size ->
                TournamentSelector<Int, IntGene>(size).toString()
                    .shouldBe("TournamentSelector(sampleSize=$size)")
            }
        }

        "when selecting" - {
            "should return a population with the same size as the given count" {
                checkAll(
                    Arb.population(),
                    Arb.int(0..5),
                    Arb.int(1..5),
                    Arb.optimizer<Int, IntGene>()
                ) { population, count, sampleSize, optimizer ->
                    TournamentSelector<Int, IntGene>(sampleSize)(
                        population,
                        count,
                        optimizer
                    ).size shouldBe count
                }
            }

            "should return the first picked individual if the sample size is 1" {
                checkAll(
                    Arb.population(),
                    Arb.int(1..5),
                    Arb.optimizer<Int, IntGene>(),
                    Arb.long()
                ) { population, count, optimizer, seed ->
                    Core.random = Random(seed)
                    val rng = Random(seed)
                    TournamentSelector<Int, IntGene>(1)(
                        population,
                        count,
                        optimizer
                    ).forEach {
                        it shouldBe population[rng.nextInt(population.size)]
                    }
                }
            }

            "should return the best of the sample" {
                checkAll(
                    Arb.population(),
                    Arb.int(1..5),
                    Arb.int(1..5),
                    Arb.optimizer<Int, IntGene>(),
                    Arb.long()
                ) { population, count, sampleSize, optimizer, seed ->
                    Core.random = Random(seed)
                    val rng = Random(seed)
                    val best = mutableListOf<Individual<Int, IntGene>>()
                    repeat(count) {
                        val sample = (0 until sampleSize).map {
                            population.random(rng)
                        }
                        best += sample.maxWith(optimizer.comparator)
                    }
                    TournamentSelector<Int, IntGene>(sampleSize)(
                        population,
                        count,
                        optimizer
                    ) shouldBe best
                }
            }
        }
    }
})
