/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.exceptions.InvalidIndexException
import cl.ravenhill.keen.genetic.chromosomes.SimpleChromosome
import cl.ravenhill.keen.genetic.chromosomes.arbChromosome
import cl.ravenhill.keen.genetic.chromosomes.arbChromosomeWithInvalidGenes
import cl.ravenhill.keen.genetic.genes.SimpleGene
import cl.ravenhill.keen.genetic.genes.arbSimpleGene
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.chromosomes.Chromosome
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.matchers.shouldBeLeft
import cl.ravenhill.matchers.shouldBeRight
import cl.ravenhill.matchers.shouldBeValid
import cl.ravenhill.matchers.shouldHaveInfringement
import cl.ravenhill.matchers.shouldNotBeValid
import cl.ravenhill.utils.arbProbability
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.collections.shouldNotBeIn
import io.kotest.matchers.ranges.shouldNotBeIn
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.next
import io.kotest.property.assume
import io.kotest.property.checkAll

class GenotypeTest : FreeSpec({
    "A Genotype" - {
        "can be created" - {
            "with a list of chromosomes" {
                checkAll(Arb.list(arbChromosome(arbSimpleGene()))) { chromosomes ->
                    val genotype = Genotype(chromosomes)
                    genotype.chromosomes shouldBe chromosomes
                }
            }

            "with variadic chromosomes" {
                checkAll(Arb.list(arbChromosome(arbSimpleGene()))) { chromosomes ->
                    val genotype = Genotype(*chromosomes.toTypedArray())
                    genotype.chromosomes shouldBe chromosomes
                }
            }
        }

        "can be flattened" - {
            checkAll(
                arbGenotypeAndFlattenedChromosomes(arbSimpleGene(), ::SimpleChromosome)
            ) { (genotype, flattenedGenes) ->
                genotype.flatten() shouldBe flattenedGenes
            }
        }

        "fold should correctly sum all gene values" {
            checkAll(
                arbGenotypeAndChromosomes(
                    arbChromosome(arbSimpleGene(range = 0..100)),
                    probability = Arb.constant(1.0)
                )
            ) { (genotype, chromosomes) ->
                val result = genotype.fold(0) { acc, value -> acc + value }
                val expected = chromosomes.flatMap { it.flatten() }.sum()
                result shouldBe expected
            }
        }

        "foldRight should correctly sum all gene values" {
            checkAll(
                arbGenotypeAndChromosomes(
                    arbChromosome(arbSimpleGene(range = 0..100)),
                    probability = Arb.constant(1.0)
                )
            ) { (genotype, chromosomes) ->
                val result = genotype.foldRight(0) { value, acc -> acc + value }
                val expected = chromosomes.flatMap { it.flatten() }.sum()
                result shouldBe expected
            }
        }

        "fold should be associative" {
            checkAll(
                arbSimpleGenotype(arbChromosome(arbSimpleGene(range = 0..100)))
            ) { genotype ->
                val geneValues = genotype.flatten()
                val foldLeft = genotype.fold(0) { acc, value -> acc + value }
                val foldRight = geneValues.foldRight(0) { value, acc -> acc + value }

                foldLeft shouldBe foldRight
            }
        }

        "should have a size property that is equal to the size of the list of chromosomes" {
            checkAll(Arb.list(arbChromosome(arbSimpleGene()))) { chromosomes ->
                Genotype(chromosomes).size shouldBe chromosomes.size
            }
        }

        "when testing for emptiness" - {
            "should return true if the list of chromosomes is empty" {
                Genotype(emptyList<SimpleChromosome>()).shouldBeEmpty()
            }

            "should return false if the list of chromosomes is not empty" {
                checkAll(Arb.list(arbChromosome(arbSimpleGene()), 1..100)) { chromosomes ->
                    Genotype(chromosomes).shouldNotBeEmpty()
                }
            }
        }

        "should have an iterator that iterates over the chromosomes" {
            checkAll(Arb.list(arbChromosome(arbSimpleGene()))) { chromosomes ->
                val genotype = Genotype(chromosomes)
                val iterator = genotype.iterator()
                chromosomes.forEach {
                    iterator.hasNext().shouldBeTrue()
                    iterator.next() shouldBe it
                }
            }
        }

        "when checking for containment" - {
            "should return true if the chromosome is in the genotype" {
                checkAll(Arb.list(arbChromosome(arbSimpleGene()), 1..10)) { chromosomes ->
                    val genotype = Genotype(chromosomes)
                    chromosomes.forEach {
                        genotype.contains(it).shouldBeTrue()
                    }
                }
            }

            "should return false if the chromosome is not in the genotype" {
                checkAll(
                    Arb.list(arbChromosome(arbSimpleGene()), 1..10),
                    arbChromosome(arbSimpleGene(), 1..10)
                ) { chromosomes, chromosome ->
                    assume { chromosome shouldNotBeIn chromosomes }
                    Genotype(chromosomes).contains(chromosome).shouldBeFalse()
                }
            }

            "should return true if all the chromosomes are in the genotype" {
                checkAll(arbGenotypeAndChromosomes(arbChromosome(arbSimpleGene()))) { (genotype, chromosomes) ->
                    genotype.containsAll(chromosomes).shouldBeTrue()
                }
            }

            "should return false if not all the chromosomes are in the genotype" {
                checkAll(
                    PropTestConfig(iterations = 100),
                    arbGenotypeAndNotContainedChromosomes(arbChromosome(arbSimpleGene()))
                ) { (genotype, notContained) ->
                    genotype.containsAll(notContained).shouldBeFalse()
                }
            }
        }

        "when testing for validity" - {
            "should return true if all chromosomes are valid" {
                checkAll(Arb.list(arbChromosome(arbSimpleGene()))) { chromosomes ->
                    Genotype(chromosomes).shouldBeValid()
                }
            }

            "should return false if any chromosome is invalid" {
                checkAll(arbGenotypeWithInvalidChromosome()) { genotype ->
                    genotype.shouldNotBeValid()
                }
            }
        }

        "when retrieving a chromosome by index" - {
            "should return the chromosome at the specified index" {
                checkAll(arbGenotypeAndChromosomeAtIndex(arbChromosome(arbSimpleGene()))) { (genotype, chromosome, index) ->
                    genotype[index]
                        .shouldBeRight()
                        .map { it shouldBe chromosome }
                }
            }

            "should return a left CompositeException if the index is out of range" {
                checkAll(arbSimpleGenotype(arbChromosome(arbSimpleGene())), Arb.int()) { genotype, index ->
                    assume { index shouldNotBeIn genotype.indices }
                    genotype[index]
                        .shouldBeLeft()
                        .mapLeft {
                            it.shouldHaveInfringement<InvalidIndexException>(
                                "The index ($index) must be in the range [0, ${genotype.size})"
                            )
                        }
                }
            }
        }
    }
})

/**
 * Generates an arbitrary simple genotype consisting of a list of chromosomes.
 *
 * @param chromosomeArb An [Arb] generator for producing individual chromosomes of type `Chromosome<Int, SimpleGene>`.
 * @return An [Arb] generator that produces instances of `Genotype<Int, SimpleGene>`.
 */
fun arbSimpleGenotype(chromosomeArb: Arb<Chromosome<Int, SimpleGene>>) = Arb.list(chromosomeArb).map(::Genotype)

private fun <T, G> arbGenotypeAndFlattenedChromosomes(
    geneArb: Arb<G>,
    chromosomeBuilder: (List<G>) -> Chromosome<T, G>,
) where G : Gene<T, G> = arbitrary {
    val genotypeSize = Arb.int(0..10).bind()
    val chromosomes = mutableListOf<Chromosome<T, G>>()
    val flattenedGenes = mutableListOf<T>()
    repeat(genotypeSize) {
        val chromosomeSize = Arb.int(0..10).bind()
        val genes = mutableListOf<G>()
        repeat(chromosomeSize) {
            genes += geneArb.bind()
            flattenedGenes += genes.last().value
        }
        chromosomes += chromosomeBuilder(genes)
    }
    Genotype(chromosomes) to flattenedGenes
}

/**
 * Generates an arbitrary `Genotype` along with a subset of its chromosomes based on a probability filter.
 *
 * @param chromosome An [Arb] generator for producing individual chromosomes.
 * @param size An optional `IntRange` specifying the possible number of chromosomes in the genotype. The default is 1 to
 *   10.
 * @param probability An optional [Arb] generator for the probability of selecting each chromosome. The default is an
 *   arbitrary probability generator.
 * @return An [Arb] generator that produces pairs containing a genotype and a subset of its chromosomes.
 */
private fun <T, G> arbGenotypeAndChromosomes(
    chromosome: Arb<Chromosome<T, G>>,
    size: IntRange = 1..10,
    probability: Arb<Double> = arbProbability(),
): Arb<Pair<Genotype<T, G>, List<Chromosome<T, G>>>> where G : Gene<T, G> = arbitrary { (random, _) ->
    val chromosomes = Arb.list(chromosome, size).bind()
    val ratio = probability.bind()
    Genotype(chromosomes) to chromosomes.filter { random.nextDouble() < ratio }
}

private fun <T, G> arbGenotypeAndNotContainedChromosomes(
    chromosome: Arb<Chromosome<T, G>>,
    size: IntRange = 1..10,
): Arb<Pair<Genotype<T, G>, List<Chromosome<T, G>>>> where G : Gene<T, G> = arbitrary {
    val chromosomes = Arb.list(chromosome, size).bind()
    var notContained: List<Chromosome<T, G>>
    do {
        notContained = Arb.list(chromosome, size).bind().filter { it !in chromosomes }
    } while (notContained.isEmpty())
    Genotype(chromosomes) to notContained
}

private fun arbGenotypeWithInvalidChromosome(): Arb<Genotype<Int, SimpleGene>> = arbitrary {
    val chromosomes = Arb.list(arbChromosome(arbSimpleGene(Arb.boolean())), 1..10).map {
        it.toMutableList()
    }.bind()
    val invalid = arbChromosomeWithInvalidGenes().bind()
    val invalidIndex = Arb.int(chromosomes.indices).bind()
    chromosomes[invalidIndex] = invalid
    Genotype(chromosomes)
}

private fun arbGenotypeAndChromosomeAtIndex(
    chromosomeArb: Arb<Chromosome<Int, SimpleGene>>,
    size: IntRange = 1..10
): Arb<Triple<Genotype<Int, SimpleGene>, Chromosome<Int, SimpleGene>, Int>> =
    Arb.list(chromosomeArb, size).map { chromosomes ->
        Arb.int(chromosomes.indices).map { index ->
            Triple(Genotype(chromosomes), chromosomes[index], index)
        }.next()
    }
