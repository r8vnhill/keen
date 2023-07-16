/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen.util.listeners

import cl.ravenhill.keen.Population
import cl.ravenhill.keen.evolution.EvolutionResult
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.listeners.records.EvolutionRecord
import cl.ravenhill.keen.util.listeners.records.GenerationRecord
import cl.ravenhill.keen.util.optimizer.FitnessMaximizer
import cl.ravenhill.keen.util.optimizer.PhenotypeOptimizer
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource

/**
 * Calculates statistics about a population.
 *
 * @param DNA  The type of the gene's value.
 *
 * @property population The population to calculate statistics from.
 * @property optimizer The optimizer used to calculate the fitness.
 * @property evolutionTime The time it took to evolve the population.
 * @property alterTime The time it took to alter the population.
 * @property offspringSelectionTime The time it took to select the offspring.
 * @property survivorSelectionTime The time it took to select the survivors.
 * @property fittest The fittest phenotype.
 * @property bestFitness The best fitness.
 * @property steadyGenerations The number of generations without improvement.
 * @property generation The current generation.
 */
abstract class AbstractEvolutionListener<DNA, G: Gene<DNA, G>> : EvolutionListener<DNA, G> {
    private var _fittest: Phenotype<DNA, G>? = null
    override var evolutionResult: EvolutionResult<DNA, G> =
        EvolutionResult(FitnessMaximizer(), listOf(), 0)
        set(value) {
            field = value
            onResultUpdated()
        }
    override var population: Population<DNA, G> = listOf()
    override var optimizer: PhenotypeOptimizer<DNA, G> = FitnessMaximizer()
    override var evolutionTime: Long = Long.MAX_VALUE
    override val alterTime: MutableList<Long> = mutableListOf()
    override var offspringSelectionTime: MutableList<Long> = mutableListOf()
    override val survivorSelectionTime: MutableList<Long> = mutableListOf()
    override var bestFitness: MutableList<Double> = mutableListOf()
    override var worstFitness: MutableList<Double> = mutableListOf()
    override var averageFitness: MutableList<Double> = mutableListOf()
    override val fittest: Phenotype<DNA, G>?
        get() = _fittest
    override var steadyGenerations: Int = 0
    override var generation: Int = 0
    override var evolution: EvolutionRecord<DNA, G> = EvolutionRecord()
    protected lateinit var _currentGeneration: GenerationRecord
    override val currentGeneration: GenerationRecord get() = _currentGeneration
    @ExperimentalTime
    override var timeSource: TimeSource = TimeSource.Monotonic

    override fun onResultUpdated() {
        optimizer = evolutionResult.optimizer
        population = optimizer.sort(evolutionResult.population)
        bestFitness += population.first().fitness
        worstFitness += population.last().fitness
        averageFitness += population.map { it.fitness }.average()
        _fittest = population.first()
        generation = evolutionResult.generation
    }

    fun display() = println(toString())
}