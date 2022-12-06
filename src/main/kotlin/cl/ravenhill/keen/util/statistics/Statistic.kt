package cl.ravenhill.keen.util.statistics

import cl.ravenhill.keen.Population
import cl.ravenhill.keen.evolution.EvolutionResult
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.util.optimizer.PhenotypeOptimizer


interface Statistic<DNA> {
    var evolutionResult: EvolutionResult<DNA>
    var population: Population<DNA>

    var optimizer: PhenotypeOptimizer<DNA>
    val survivorSelectionTime: MutableList<Long>
    val offspringSelectionTime: MutableList<Long>
    val alterTime: MutableList<Long>
    var evolutionTime: Long
    val generationTimes: MutableList<Long>
    var bestFitness: MutableList<Double>
    var worstFitness: MutableList<Double>
    var averageFitness: MutableList<Double>
    val fittest: Phenotype<DNA>?
    var steadyGenerations: Int
    var generation: Int

    fun onResultUpdated()
}