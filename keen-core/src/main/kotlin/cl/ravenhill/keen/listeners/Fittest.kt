package cl.ravenhill.keen.listeners

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.listeners.records.EvolutionRecord
import cl.ravenhill.keen.listeners.records.IndividualRecord
import cl.ravenhill.keen.ranking.Ranker

/**
 * Determines the fittest individual from the latest generation in the evolutionary record.
 *
 * ## Usage:
 * This function takes an `IndividualRanker` and an `EvolutionRecord`, and returns the fittest individual from the
 * latest generation based on the provided ranker.
 *
 * ### Example 1: Finding the Fittest Individual
 * ```
 * val ranker = FitnessMaxRanker<MyGene>()
 * val evolution = EvolutionRecord<Int, MyGene>()
 * // Assume evolution has been populated with generations and individuals
 * val fittestIndividual = fittest(ranker, evolution)
 * println("Fittest individual's fitness: ${fittestIndividual.fitness}")
 * ```
 *
 * @param ranker the ranker used to sort and determine the fittest individual
 * @param evolution the record of the evolution process containing generations and populations
 * @param T the type of the gene value
 * @param G the type of the gene, which must extend [Gene]
 * @return the fittest individual record from the latest generation
 */
fun <T, G> fittest(
    ranker: Ranker<T, G>,
    evolution: EvolutionRecord<T, G>
): IndividualRecord<T, G> where G : Gene<T, G> =
    ranker.sort(
        evolution.generations
            .last().population
            .offspring
            .map { it.toIndividual() }
    ).first().let { IndividualRecord(it.genotype, it.fitness) }
