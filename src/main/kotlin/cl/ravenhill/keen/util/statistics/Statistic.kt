package cl.ravenhill.keen.util.statistics

import cl.ravenhill.keen.genetic.Phenotype


interface Statistic<T> {
    val survivorSelectionTime: MutableList<Long>
    val offspringSelectionTime: MutableList<Long>
    val alterTime: MutableList<Long>
    var evolutionTime: Long
    val generationTimes: MutableList<Long>
    var bestFitness: Double
    var fittest: Phenotype<T>?
    var steadyGenerations: Int
    var generation: Int
}