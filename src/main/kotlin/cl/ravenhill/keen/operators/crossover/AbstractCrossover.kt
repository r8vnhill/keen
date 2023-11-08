/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.operators.crossover

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.DoubleConstraint.BeInRange
import cl.ravenhill.jakt.constraints.IntConstraint.*
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Individual
import cl.ravenhill.keen.genetic.Population
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.AltererResult
import cl.ravenhill.keen.util.indices
import cl.ravenhill.keen.util.subsets
import cl.ravenhill.keen.util.transpose

/**
 * An abstract class for performing crossover operations on individuals within a population.
 * Subclasses must implement the [crossoverChromosomes] method for actual crossover functionality.
 *
 * @param numOut The number of new individuals produced by each crossover operation
 * @param numIn The number of individuals required as input for each crossover operation
 * @param exclusivity If true, individuals cannot be selected more than once for a given crossover operation
 * @param chromosomeRate The probability that a given chromosome within an individual will be selected for recombination
 */
abstract class AbstractCrossover<DNA, G : Gene<DNA, G>>(
    val numOut: Int = 2,
    val numIn: Int = 2,
    val exclusivity: Boolean = false,
    val chromosomeRate: Double = 1.0,
) : Crossover<DNA, G> {

    init {
        constraints {
            "There should be at least 2 inputs to perform a crossover operation" {
                numIn must BeAtLeast(2)
            }
            "The number of outputs should be greater than 0" {
                numOut must BePositive
            }
            "The chromosome crossover probability should be in 0..1" {
                chromosomeRate must BeInRange(0.0..1.0)
            }
        }
    }

    override fun invoke(
        population: Population<DNA, G>,
        generation: Int,
    ): AltererResult<DNA, G> {
        // select a subset of individuals to recombine using the provided probability and other
        // parameters
        val parents = Core.random.subsets(population, numIn, exclusivity)
        // recombine the selected parents and count the number of individuals that were
        // recombined
        val recombined = mutableListOf<Individual<DNA, G>>()
        while (recombined.size < population.size) {
            crossover(parents.random(Core.random).map { it.genotype }).forEach { genotype ->
                recombined += Individual(genotype)
            }
        }
        // return the resulting population and count
        return AltererResult(recombined.take(population.size), recombined.size)
    }

    override fun crossover(parentGenotypes: List<Genotype<DNA, G>>): List<Genotype<DNA, G>> {
        constraints {
            "Input count [${parentGenotypes.size}] must match constructor-specified count [$numIn]." {
                parentGenotypes.size must BeEqualTo(numIn)
            }
        }
        val size = parentGenotypes[0].size
        // Select random indices of chromosomes to recombine
        val chIndices = Core.random.indices(chromosomeRate, size)
        // Associate the chromosomes at the selected indices
        val chromosomes = chIndices.map { i -> parentGenotypes.map { it[i] } }
        // Recombine the selected chromosomes to create new genotypes.
        val recombined = chromosomes.map { crossoverChromosomes(it) }.transpose()
        // Create new genotypes from the recombined chromosomes.
        return recombined.map { chs ->
            var i = 0
            Genotype(
                // Iterate over the input genotypes and create a new genotype by selecting
                // chromosomes from the recombined chromosomes or the original genotype.
                parentGenotypes[0].mapIndexed { index, ch ->
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
