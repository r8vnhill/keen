package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.keen.*
import cl.ravenhill.keen.genetic.chromosomes.numerical.DoubleChromosome
import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.operators.AltererResult
import cl.ravenhill.keen.operators.crossover.combination.MeanCrossover
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.positiveInt
import io.kotest.property.checkAll
import kotlin.random.Random

class MeanCombinatorSpec : FreeSpec({
    beforeAny {
        Core.random = Random.Default
    }
    "Converting to" - {
        "String should" - {
            "Return the class name and the probability" {
                checkAll(Arb.probability()) { probability ->
                    MeanCrossover<Number>(probability).toString() shouldBe
                            "MeanCrossover { probability: $probability }"
                }
            }
        }
    }
    "Combining two chromosomes" - {
        "the genes are Ints" - {
            "return the first chromosome if the probability is 0" {
                checkAll(Arb.intChromosomePair(), Arb.long()) { (c1, c2), seed ->
                    Core.random = Random(seed)
                    val combined = MeanCrossover<Int>(0.0).combine(c1, c2)
                    combined shouldBe c1.genes
                }
            }
            "return the mean of all the genes if the probability is 1" {
                checkAll(Arb.intChromosomePair(), Arb.long()) { (c1, c2), seed ->
                    Core.random = Random(seed)
                    val combined = MeanCrossover<Int>(1.0).combine(c1, c2)
                    val expected = c1.genes.zip(c2.genes) { g1, g2 ->
                        IntGene(
                            ((g1.dna.toLong() + g2.dna.toLong()) / 2).toInt(),
                            (c1 as IntChromosome).range
                        )
                    }
                    combined shouldBe expected
                }
            }
            "return the mean of genes according to the probability" {
                checkAll(
                    Arb.intChromosomePair(),
                    Arb.probability(),
                    Arb.long()
                ) { (c1, c2), probability, seed ->
                    Core.random = Random(seed)
                    val random = Random(seed)
                    val combined = MeanCrossover<Int>(probability).combine(c1, c2)
                    val expected = c1.genes.zip(c2.genes) { g1, g2 ->
                        IntGene(
                            if (random.nextDouble() < probability) {
                                ((g1.dna.toLong() + g2.dna.toLong()) / 2).toInt()
                            } else {
                                g1.dna
                            },
                            (c1 as IntChromosome).range
                        )
                    }
                    combined shouldBe expected
                }
            }
        }
        "the genes are Double" - {
            "return the first chromosome if the probability is 0" {
                checkAll(Arb.doubleChromosomePair(), Arb.long()) { (c1, c2), seed ->
                    Core.random = Random(seed)
                    val combined = MeanCrossover<Double>(0.0).combine(c1, c2)
                    combined shouldBe c1.genes
                }
            }
            "return the mean of all the genes if the probability is 1" {
                checkAll(Arb.doubleChromosomePair(), Arb.long()) { (c1, c2), seed ->
                    Core.random = Random(seed)
                    val combined = MeanCrossover<Double>(1.0).combine(c1, c2)
                    val expected = c1.genes.zip(c2.genes) { g1, g2 ->
                        DoubleGene(
                            (g1.dna + g2.dna) / 2,
                            (c1 as DoubleChromosome).range
                        )
                    }
                    (combined zip expected).forEach { (c, e) ->
                        c.dna shouldBe e.dna
                    }
                }
            }
            "return the mean of genes according to the probability" {
                checkAll(
                    Arb.doubleChromosomePair(),
                    Arb.probability(),
                    Arb.long()
                ) { (c1, c2), probability, seed ->
                    Core.random = Random(seed)
                    val random = Random(seed)
                    val combined = MeanCrossover<Double>(probability).combine(c1, c2)
                    val expected = c1.genes.zip(c2.genes) { g1, g2 ->
                        DoubleGene(
                            if (random.nextDouble() < probability) {
                                (g1.dna + g2.dna) / 2
                            } else {
                                g1.dna
                            },
                            (c1 as DoubleChromosome).range
                        )
                    }
                    (combined zip expected).forEach { (c, e) ->
                        c.dna shouldBe e.dna
                    }
                }
            }
        }
    }
    "Invoking the combinator on a population of " - {
        "Ints should" - {
            "return the first population with 0 alterations if the probability is 0" {
                checkAll(
                    Arb.population(Arb.intChromosomeFactory(10), 10),
                    Arb.positiveInt(),
                    Arb.long()
                ) { population, generation, seed ->
                    Core.random = Random(seed)
                    val combinator = MeanCrossover<Int>(0.0)
                    val combined = combinator(population, generation)
                    combined shouldBe AltererResult(population, 0)
                }
            }
        }
    }
})
