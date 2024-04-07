/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.alteration.mutation

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ResetDomainListener
import cl.ravenhill.keen.arb.KeenArb
import cl.ravenhill.keen.arb.anyRanker
import cl.ravenhill.keen.arb.evolution.arbEvolutionState
import cl.ravenhill.keen.arb.genetic.chromosomes.intChromosome
import cl.ravenhill.keen.arb.genetic.genotype
import cl.ravenhill.keen.arb.genetic.arbIndividual
import cl.ravenhill.keen.arb.genetic.arbPopulation
import cl.ravenhill.keen.arb.operators.anyMutator
import cl.ravenhill.keen.arb.operators.baseMutator
import cl.ravenhill.keen.arb.rngPair
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.doubles.shouldBeNaN
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.constant
import io.kotest.property.checkAll

@OptIn(ExperimentalKotest::class)
class MutatorTest : FreeSpec({

    "A Mutator instance" - {
        "when mutating an individual" - {
            "should perform no mutations if the probability is 0" {
                checkAll(
                    Arb.anyMutator(chromosomeRate = Arb.constant(0.0)),
                    arbIndividual(Arb.genotype(Arb.intChromosome()))
                ) { mutator, individual ->
                    with(mutator.mutateIndividual(individual)) {
                        fitness.shouldBeNaN()
                        genotype shouldBe individual.genotype
                    }
                }
            }

            "should mutate all chromosomes if the probability is 1" {
                checkAll(
                    PropTestConfig(listeners = listOf(ResetDomainListener)),
                    Arb.baseMutator<Int, IntGene>(chromosomeRate = Arb.constant(1.0)),
                    arbIndividual(Arb.genotype(Arb.intChromosome())),
                    Arb.rngPair()
                ) { mutator, individual, (rng1, rng2) ->
                    Domain.random = rng1
                    val mutated = mutator.mutateIndividual(individual)
                    Domain.random = rng2
                    mutated.genotype.forEachIndexed { index, chromosome ->
                        chromosome shouldBe individual.genotype[index].reversed()
                    }
                }
            }

            "should mutate chromosomes according to the chromosome rate" {
                checkAll(
                    PropTestConfig(listeners = listOf(ResetDomainListener)),
                    Arb.baseMutator<Int, IntGene>(),
                    arbIndividual(Arb.genotype(Arb.intChromosome())),
                    Arb.rngPair()
                ) { mutator, individual, (rng1, rng2) ->
                    Domain.random = rng1
                    val mutated = mutator.mutateIndividual(individual)
                    Domain.random = rng2
                    mutated.genotype.forEachIndexed { index, chromosome ->
                        if (Domain.random.nextDouble() > mutator.chromosomeRate) {
                            chromosome shouldBe individual.genotype[index]
                        } else {
                            chromosome shouldBe chromosome.duplicateWithGenes(individual.genotype[index].reversed())
                        }
                    }
                }
            }
        }

        "when mutating a population" - {
            "should perform no mutations if the probability is 0" {
                checkAll(
                    Arb.anyMutator(chromosomeRate = Arb.constant(0.0)),
                    arbEvolutionState(
                        arbPopulation(arbIndividual(Arb.genotype(Arb.intChromosome()))),
                        KeenArb.anyRanker()
                    )
                ) { mutator, state ->
                    with(mutator(state, state.population.size)) {
                        population shouldBe state.population
                    }
                }
            }

            "should mutate all chromosomes if the probability is 1" {
                checkAll(
                    PropTestConfig(listeners = listOf(ResetDomainListener)),
                    Arb.anyMutator(individualRate = Arb.constant(1.0)),
                    arbEvolutionState(
                        arbPopulation(arbIndividual(Arb.genotype(Arb.intChromosome()))),
                        KeenArb.anyRanker()
                    ),
                    Arb.rngPair()
                ) { mutator, state, (rng1, rng2) ->
                    Domain.random = rng1
                    val mutated = mutator(state, state.population.size)
                    Domain.random = rng2
                    mutated.population.forEachIndexed { i, individual ->
                        Domain.random.nextDouble()  // Skip the individual rate check
                        individual shouldBe mutator.mutateIndividual(state.population[i])
                    }
                }
            }

            "should mutate individuals according to the individual rate" {
                checkAll(
                    PropTestConfig(listeners = listOf(ResetDomainListener)),
                    Arb.anyMutator(),
                    arbEvolutionState(
                        arbPopulation(arbIndividual(Arb.genotype(Arb.intChromosome()))),
                        KeenArb.anyRanker()
                    ),
                    Arb.rngPair()
                ) { mutator, state, (rng1, rng2) ->
                    Domain.random = rng1
                    val mutated = mutator(state, state.population.size)
                    Domain.random = rng2
                    mutated.population.forEachIndexed { i, individual ->
                        if (Domain.random.nextDouble() > mutator.individualRate) {
                            individual shouldBe state.population[i]
                        } else {
                            individual shouldBe mutator.mutateIndividual(state.population[i])
                        }
                    }
                }
            }
        }
    }
})
