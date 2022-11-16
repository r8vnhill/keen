package cl.ravenhill.keen.util.statistics

import cl.ravenhill.keen.core.Genotype


interface Statistic<T> {
    val selectionTime: MutableList<Long>
    val alterTime: MutableList<Long>
    var evolutionTime: Long
    val generationTimes: MutableList<Long>
    var bestFitness: Double
    var fittest: Genotype<T>
    var steadyGenerations: Int
    var generation: Int
}