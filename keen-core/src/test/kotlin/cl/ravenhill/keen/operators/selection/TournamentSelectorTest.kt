/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.selection

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.jakt.exceptions.IntConstraintException
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ResetDomainListener
import cl.ravenhill.keen.arb.KeenArb
import cl.ravenhill.keen.arb.anyRanker
import cl.ravenhill.keen.arb.genetic.chromosomes.arbIntChromosome
import cl.ravenhill.keen.arb.genetic.chromosomes.arbNothingChromosome
import cl.ravenhill.keen.arb.genetic.genotype
import cl.ravenhill.keen.arb.genetic.arbIndividual
import cl.ravenhill.keen.arb.genetic.arbPopulation
import cl.ravenhill.keen.arb.operators.arbTournamentSelector
import cl.ravenhill.keen.arb.arbRngPair
import cl.ravenhill.keen.assertions.should.shouldHaveInfringement
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.types.shouldHaveSameHashCodeAs
import io.kotest.matchers.types.shouldNotHaveSameHashCodeAs
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.nonPositiveInt
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.assume
import io.kotest.property.checkAll

@OptIn(ExperimentalKotest::class)
class TournamentSelectorTest : FreeSpec({

    "A Tournament Selector instance" - {
        "should have a tournament size property that" - {
            "defaults to DEFAULT_SIZE" {
                TournamentSelector<Nothing, NothingGene>().tournamentSize shouldBe TournamentSelector.DEFAULT_SIZE
            }

            "must be positive" {
                checkAll(Arb.nonPositiveInt()) {
                    shouldThrow<CompositeException> {
                        TournamentSelector<Nothing, NothingGene>(it)
                    }.shouldHaveInfringement<IntConstraintException>("The tournament size ($it) must be positive")
                }
            }

            "is set accordingly to the constructor" {
                checkAll(Arb.positiveInt()) { size ->
                    TournamentSelector<Nothing, NothingGene>(size).tournamentSize shouldBe size
                }
            }
        }

        "when selecting individuals from a population" - {
            "should return the specified number of individuals" {
                checkAll(
                    arbPopulation(arbIndividual(Arb.genotype(arbNothingChromosome())), 1..25),
                    Arb.int(0..100),
                    KeenArb.anyRanker<Nothing, NothingGene>()
                ) { population, n, ranker ->
                    TournamentSelector<Nothing, NothingGene>().select(population, n, ranker).size shouldBe n
                }
            }

            "should return the expected individuals" {
                checkAll(
                    PropTestConfig(listeners = listOf(ResetDomainListener)),
                    arbTournamentSelector<Int, IntGene>(),
                    arbPopulation(arbIndividual(Arb.genotype(arbIntChromosome())), 1..25),
                    Arb.int(0..100),
                    KeenArb.anyRanker<Int, IntGene>(),
                    arbRngPair()
                ) { selector, population, n, ranker, (rng1, rng2) ->
                    Domain.random = rng1
                    val selected = selector.select(population, n, ranker)
                    selected.forEach {
                        val candidates = List(selector.tournamentSize) { population.random(rng2) }
                        val best = candidates.maxWithOrNull(ranker.comparator)
                        it shouldBe best
                    }
                }
            }
        }

        "properties" - {
            "can be converted to a string" - {
                "with the default tournament size" {
                    TournamentSelector<Nothing, NothingGene>().toString() shouldBe
                            "TournamentSelector(tournamentSize=3)"
                }

                "with a custom tournament size" {
                    checkAll(Arb.positiveInt()) { size ->
                        val selector = TournamentSelector<Nothing, NothingGene>(size)
                        val regex = """(TournamentSelector\(tournamentSize=)(\d+)(\))"""
                        val matcher = regex.toRegex().find(selector.toString())
                        matcher.shouldNotBeNull()
                        matcher.groupValues[1] shouldBe "TournamentSelector(tournamentSize="
                        matcher.groupValues[2].toInt() shouldBe size
                        matcher.groupValues[3] shouldBe ")"
                    }
                }
            }

            "hashing" - {
                "should be consistent" {
                    checkAll(Arb.positiveInt()) { size ->
                        TournamentSelector<Nothing, NothingGene>(size) shouldHaveSameHashCodeAs
                                TournamentSelector<Nothing, NothingGene>(size)
                    }
                }

                "should be different for different tournament sizes" {
                    checkAll(Arb.positiveInt(), Arb.positiveInt()) { size1, size2 ->
                        assume {
                            size1 shouldNotBe size2
                        }
                        TournamentSelector<Nothing, NothingGene>(size1) shouldNotHaveSameHashCodeAs
                                TournamentSelector<Nothing, NothingGene>(size2)
                    }
                }
            }
        }
    }
})
