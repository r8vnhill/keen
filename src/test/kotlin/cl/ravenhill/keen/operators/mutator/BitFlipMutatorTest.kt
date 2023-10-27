/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.mutator

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.arbs.genetic.boolChromosome
import cl.ravenhill.keen.arbs.genetic.boolGene
import cl.ravenhill.keen.assertions.operations.`mutator chromosome rate defaults to one half`
import cl.ravenhill.keen.assertions.operations.`mutator gene rate defaults to one half`
import cl.ravenhill.keen.assertions.operations.`should enforce valid mutation probability`
import cl.ravenhill.keen.genetic.genes.BoolGene
import cl.ravenhill.real
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.doubles.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.negativeDouble
import io.kotest.property.arbitrary.positiveDouble
import io.kotest.property.assume
import io.kotest.property.checkAll
import kotlin.random.Random

class BitFlipMutatorTest : FreeSpec({
    "A [BitFlipMutator]" - {
        "when created" - {
            "without a chromosome probability then it defaults to 0.5" {
                `mutator chromosome rate defaults to one half` { probability, geneRate ->
                    BitFlipMutator<BoolGene>(probability, geneRate = geneRate)
                }
            }

            "without a gene rate defaults to 0.5" {
                `mutator gene rate defaults to one half` { probability, chromosomeRate ->
                    BitFlipMutator<BoolGene>(probability, chromosomeRate = chromosomeRate)
                }
            }

            "with valid parameters then it should create a valid mutator" {
                checkAll(
                    Arb.real(0.0..1.0),
                    Arb.real(0.0..1.0),
                    Arb.real(0.0..1.0)
                ) { probability, chromosomeRate, geneRate ->
                    val mutator =
                        BitFlipMutator<BoolGene>(probability, chromosomeRate, geneRate)
                    mutator.probability shouldBe probability
                    mutator.chromosomeRate shouldBe chromosomeRate
                    mutator.geneRate shouldBe geneRate
                }
            }

            "should throw an exception" - {
                "if the gene rate is negative" {
                    `should enforce valid mutation probability`(
                        Arb.negativeDouble(),
                        "gene rate"
                    ) { probability, rate ->
                        BitFlipMutator<BoolGene>(
                            probability,
                            geneRate = rate
                        )
                    }
                }

                "if the gene rate is greater than 1" {
                    `should enforce valid mutation probability`(
                        Arb.positiveDouble(),
                        "gene rate",
                        { assume { it shouldBeGreaterThan 1.0 } }
                    ) { probability, rate ->
                        BitFlipMutator<BoolGene>(
                            probability,
                            geneRate = rate
                        )
                    }
                }
            }
        }

        "when mutating a gene" - {
            "should make no mutations if the gene rate is 0" {
                checkAll(Arb.boolGene()) { gene ->
                    val mutator = BitFlipMutator<BoolGene>(0.0, geneRate = 0.0)
                    val result = mutator.mutateGene(gene)
                    result.mutated shouldBe gene
                    result.mutations shouldBe 0
                }
            }

            "should flip the gene if the gene rate is 1" {
                checkAll(Arb.boolGene()) { gene ->
                    val mutator = BitFlipMutator<BoolGene>(0.0, geneRate = 1.0)
                    val result = mutator.mutateGene(gene)
                    if (gene.dna) {
                        result.mutated shouldBe BoolGene.False
                    } else {
                        result.mutated shouldBe BoolGene.True
                    }
                    result.mutations shouldBe 1
                }
            }

            "should flip the gene according to the probability" {
                checkAll(
                    Arb.boolGene(),
                    Arb.real(0.0..1.0),
                    Arb.real(0.0..1.0),
                    Arb.long()
                ) { gene, probability, geneRate, seed ->
                    val mutator =
                        BitFlipMutator<BoolGene>(probability, geneRate = geneRate)
                    Core.random = Random(seed)
                    val rng = Random(seed)

                    val result = mutator.mutateGene(gene)

                    if (rng.nextDouble() < geneRate) {
                        // Expect a mutation
                        val expectedMutation =
                            if (gene.dna) BoolGene.False else BoolGene.True
                        result.mutated shouldBe expectedMutation
                        result.mutations shouldBe 1
                    } else {
                        // Expect no mutation
                        result.mutated shouldBe gene
                        result.mutations shouldBe 0
                    }
                }
            }
        }

        "when mutating a chromosome" - {
            "should make no mutations if the chromosome rate is 0" {
                checkAll(
                    Arb.boolChromosome(),
                    Arb.real(0.0..1.0)
                ) { chromosome, probability ->
                    val mutator =
                        BitFlipMutator<BoolGene>(probability, chromosomeRate = 0.0)
                    val result = mutator.mutateChromosome(chromosome)
                    result.mutated shouldBe chromosome
                    result.mutations shouldBe 0
                }
            }

            "should flip random genes if the chromosome rate is 1" {
                checkAll(
                    Arb.boolChromosome(),
                    Arb.real(0.0..1.0),
                    Arb.long()
                ) { chromosome, probability, seed ->
                    val mutator =
                        BitFlipMutator<BoolGene>(probability, chromosomeRate = 1.0)
                    Core.random = Random(seed)
                    val rng = Random(seed)
                    val result = mutator.mutateChromosome(chromosome)
                    rng.nextDouble()
                    var expectedMutations = 0
                    result.mutated.zip(chromosome.genes).forEach { (mutated, original) ->
                        if (rng.nextDouble() >= 0.5) {
                            mutated shouldBe original
                        } else {
                            expectedMutations++
                            if (original.dna) {
                                mutated shouldBe BoolGene.False
                            } else {
                                mutated shouldBe BoolGene.True
                            }
                        }
                    }
                    result.mutations shouldBe expectedMutations
                }
            }

            "should flip random genes according to the probability" {
                checkAll(
                    Arb.boolChromosome(),
                    Arb.real(0.0..1.0),
                    Arb.real(0.0..1.0),
                    Arb.long()
                ) { chromosome, probability, chromosomeRate, seed ->
                    val mutator =
                        BitFlipMutator<BoolGene>(
                            probability,
                            chromosomeRate = chromosomeRate
                        )
                    Core.random = Random(seed)
                    val rng = Random(seed)
                    val result = mutator.mutateChromosome(chromosome)
                    if (rng.nextDouble() < chromosomeRate) {
                        // Expect a mutation
                        var expectedMutations = 0
                        result.mutated.zip(chromosome.genes)
                            .forEach { (mutated, original) ->
                                if (rng.nextDouble() < 0.5) {
                                    expectedMutations++
                                    if (original.dna) {
                                        mutated shouldBe BoolGene.False
                                    } else {
                                        mutated shouldBe BoolGene.True
                                    }
                                } else {
                                    mutated shouldBe original
                                }
                            }
                        result.mutations shouldBe expectedMutations
                    } else {
                        // Expect no mutation
                        result.mutated shouldBe chromosome
                        result.mutations shouldBe 0
                    }
                }
            }
        }
    }
})
