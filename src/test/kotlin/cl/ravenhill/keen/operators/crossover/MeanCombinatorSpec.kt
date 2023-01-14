package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.keen.*
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.chromosomes.numerical.DoubleChromosome
import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.operators.AltererResult
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.checkAll
import kotlin.random.Random

class MeanCombinatorSpec : WordSpec({
    beforeAny {
        Core.random = Random.Default
    }
    "Convert to" When {
        "String" should {
            "Return the class name and the probability" {
                checkAll(Arb.probability()) { probability ->
                    MeanCombinator<Number>(probability).toString() shouldBe
                            "MeanCrossover { probability: $probability }"
                }
            }
        }
    }
    "Combining two chromosomes" When {
        "the genes are Ints" should {
            "return the first chromosome if the probability is 0" {
                checkAll(Arb.intChromosomePair(), Arb.long()) { (c1, c2), seed ->
                    Core.random = Random(seed)
                    val combined = MeanCombinator<Int>(0.0).combine(c1, c2)
                    combined shouldBe c1.genes
                }
            }
            "return the mean of all the genes if the probability is 1" {
                checkAll(Arb.intChromosomePair(), Arb.long()) { (c1, c2), seed ->
                    Core.random = Random(seed)
                    val combined = MeanCombinator<Int>(1.0).combine(c1, c2)
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
                    val combined = MeanCombinator<Int>(probability).combine(c1, c2)
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
        "the genes are Double" should {
            "return the first chromosome if the probability is 0" {
                checkAll(Arb.doubleChromosomePair(), Arb.long()) { (c1, c2), seed ->
                    Core.random = Random(seed)
                    val combined = MeanCombinator<Double>(0.0).combine(c1, c2)
                    combined shouldBe c1.genes
                }
            }
            "return the mean of all the genes if the probability is 1" {
                checkAll(Arb.doubleChromosomePair(), Arb.long()) { (c1, c2), seed ->
                    Core.random = Random(seed)
                    val combined = MeanCombinator<Double>(1.0).combine(c1, c2)
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
                    val combined = MeanCombinator<Double>(probability).combine(c1, c2)
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
    "Invoking" When {
        "the chromosomes are Ints" should {
            "!return the first population with 0 alterations if the probability is 0" {
                checkAll(
                    Arb.population(Arb.intChromosomeFactory(10), 10),
                    Arb.positiveInt(),
                    Arb.long()
                ) { population, generation, seed ->
                    Core.random = Random(seed)
                    val combinator = MeanCombinator<Int>(0.0)
                    val combined = combinator(population, generation)
                    combined shouldBe AltererResult(population, 0)
                }
            }
        }
    }
})

/**
 * Generates a pair of [IntChromosome]s with the same range and size.
 */
fun Arb.Companion.intChromosomePair() = chromosomePair(orderedIntPair()) { size, range ->
    IntChromosome.Factory().apply {
        this.range = range
        this.size = size
    }
}

/**
 * Generates a pair of [DoubleChromosome]s with the same range and size.
 */
fun Arb.Companion.doubleChromosomePair() =
    chromosomePair(orderedDoublePair()) { size, range ->
        DoubleChromosome.Factory().apply {
            this.range = range
            this.size = size
        }
    }

/**
 * Generates a pair of [Chromosome]s with the same range and size.
 */
private fun <T> Arb.Companion.chromosomePair(
    pairGenerator: Arb<Pair<T, T>>,
    factoryCreator: (Int, Pair<T, T>) -> Chromosome.Factory<T>
) = arbitrary {
    Core.random = Random(long().bind())
    val size = positiveInt(10).bind()
    val range = pairGenerator.bind()
    val factory = factoryCreator(size, range)
    factory.make() to factory.make()
}

/**
 * Generates an [Arb]itrary population of [IntChromosome]s with the same range and size.
 */
fun <T> Arb.Companion.population(
    chromosomeFactory: Arb<Chromosome.Factory<T>>,
    maxSize: Int
) = arbitrary {
    val size = positiveInt(maxSize).bind()
    val fitness = Arb.double().bind()
    List(size) {
        phenotype(chromosomeFactory, fitness).bind()
    }
}
