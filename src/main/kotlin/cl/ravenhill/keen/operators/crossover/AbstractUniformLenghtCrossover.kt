/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.enforcer.requirements.DoubleRequirement.BeInRange
import cl.ravenhill.enforcer.requirements.IntRequirement.BeAtLeast
import cl.ravenhill.enforcer.requirements.IntRequirement.BeEqualTo
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Population
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.AbstractAlterer
import cl.ravenhill.keen.operators.AltererResult
import cl.ravenhill.keen.util.ceil
import cl.ravenhill.keen.util.indices
import cl.ravenhill.keen.util.neq
import cl.ravenhill.keen.util.subsets
import cl.ravenhill.keen.util.transpose

/**
 * An abstract class for performing crossover operations on individuals within a population.
 * Subclasses must implement the [crossoverChromosomes] method for actual crossover functionality.
 *
 * @param probability The probability that a crossover operation will be performed on a given pair of individuals
 * @param numOut The number of new individuals produced by each crossover operation
 * @param numIn The number of individuals required as input for each crossover operation
 * @param exclusivity If true, individuals cannot be selected more than once for a given crossover operation
 * @param chromosomeRate The probability that a given chromosome within an individual will be selected for recombination
 */
abstract class AbstractUniformLenghtCrossover<DNA, G : Gene<DNA, G>>(
    probability: Double,
    val numOut: Int = 2,
    val numIn: Int = 2,
    val exclusivity: Boolean = false,
    val chromosomeRate: Double = 1.0,
) : AbstractAlterer<DNA, G>(probability), Crossover<DNA, G> {

    init {
        enforce {
            "There should be at least 2 inputs to perform a crossover operation" {
                numIn must BeAtLeast(2)
            }
            "The number of outputs should be greater than 0" {
                numOut must BeAtLeast(1)
            }
            "The chromosome crossover probability should be in 0..1" {
                chromosomeRate must BeInRange(0.0..1.0)
            }
        }
    }

    /* Documentation inherited from [Alterer] interface.    */
    override fun invoke(
        population: Population<DNA, G>,
        generation: Int,
    ): AltererResult<DNA, G> {
        // check if the probability is non-zero, and there are at least 2 individuals in the
        // population
        return if (probability neq 0.0 && population.size >= 2) {
            // select a subset of individuals to recombine using the provided probability and other
            // parameters
            val indices = Core.random.indices(probability, population.size)
            if (indices.size < numIn) return AltererResult(population)
            val parents = Core.random.subsets(population, numIn, exclusivity)
            // recombine the selected parents and count the number of individuals that were
            // recombined
            val recombined = generateSequence {
                crossover(parents.random(Core.random).map { it.genotype })
            }
                // Ceiling division to ensure that we generate enough individuals to maintain or
                // exceed the original population size.
                .take((population.size / numOut.toDouble()).ceil())
                .flatten().map {
                    Individual(it)
                }.toList()
                .take(population.size) // Truncate the list to the original population size
            // return the resulting population and count
            AltererResult(recombined, recombined.size)
        } else {
            // if probability is zero or there are less than 2 individuals in the population, return
            // the original population
            AltererResult(population)
        }
    }

    override fun crossover(inGenotypes: List<Genotype<DNA, G>>): List<Genotype<DNA, G>> {
        enforce {
            val inCount = inGenotypes.size
            "Input count [$inCount] must match constructor-specified count [$numIn]." {
                inGenotypes.size must BeEqualTo(numIn)
            }
            "All inputs must have the same genotype length" {
                inGenotypes.map { it.size }.distinct().size must BeEqualTo(1)
            }
        }
        val size = inGenotypes[0].size
        // Select random indices of chromosomes to recombine
        val chIndices = Core.random.indices(chromosomeRate, size)
        // Associate the chromosomes at the selected indices
        val chromosomes = chIndices.map { i -> inGenotypes.map { it[i] } }
        // Recombine the selected chromosomes to create new genotypes.
        val recombined = chromosomes.map { crossoverChromosomes(it) }.transpose()
        // Create new genotypes from the recombined chromosomes.
        return recombined.map { chs ->
            var i = 0
            Genotype(
                // Iterate over the input genotypes and create a new genotype by selecting
                // chromosomes from the recombined chromosomes or the original genotype.
                inGenotypes[0].mapIndexed { index, ch ->
                    if (index in chIndices) chs[i++] else ch
                }
            )
        }
    }

    /**
     * Performs crossover on a list of chromosomes to create new individuals.
     *
     * This method receives a list of [numIn] input [chromosomes] and returns a list of [numOut]
     * output [chromosomes].
     *
     * @param chromosomes The list of chromosomes to recombine
     * @return The list of chromosomes produced by the crossover operation
     */
    protected abstract fun crossoverChromosomes(
        chromosomes: List<Chromosome<DNA, G>>,
    ): List<Chromosome<DNA, G>>
}
