/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic

import cl.ravenhill.jakt.exceptions.CompositeException
import cl.ravenhill.keen.exceptions.InvalidIndexException
import cl.ravenhill.keen.genetic.chromosomes.SimpleChromosome
import cl.ravenhill.keen.genetic.chromosomes.arbChromosome
import cl.ravenhill.keen.genetic.chromosomes.arbChromosomeWithInvalidGenes
import cl.ravenhill.keen.genetic.genes.SimpleGene
import cl.ravenhill.keen.genetic.genes.arbSimpleGene
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.chromosomes.Chromosome
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.matchers.shouldBeValid
import cl.ravenhill.keen.matchers.shouldNotBeValid
import cl.ravenhill.matchers.shouldHaveInfringement
import cl.ravenhill.utils.arbProbability
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.collections.shouldNotBeIn
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.flatMap
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
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
                arbGenotypeAndFlattenedChromosomes(
                    arbSimpleGene(),
                    ::SimpleChromosome
                )
            ) { (genotype, flattenedGenes) ->
                genotype.flatten() shouldBe flattenedGenes
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
                checkAll(Arb.list(arbChromosome(arbSimpleGene()))) { chromosomes ->
                    val genotype = Genotype(chromosomes)
                    chromosomes.forEachIndexed { index, chromosome ->
                        genotype[index] shouldBe chromosome
                    }
                }
            }

            "should throw an exception if the index is out of range" {
                checkAll(Arb.list(arbChromosome(arbSimpleGene())).flatMap { chromosomes ->
                    Arb.int().filter { it !in chromosomes.indices }.map { it to chromosomes }
                }) { (index, chromosomes) ->
                    val genotype = Genotype(chromosomes)
                    shouldThrow<CompositeException> {
                        genotype[index]
                    }.shouldHaveInfringement<InvalidIndexException>(
                        "The index ($index) must be in the range [0, ${chromosomes.size})"
                    )
                }
            }
        }
    }
})

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
