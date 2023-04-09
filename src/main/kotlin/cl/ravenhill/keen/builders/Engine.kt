/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.builders

import cl.ravenhill.keen.evolution.Engine
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.Gene

/**
 * Creates a new [Engine] with the given [fitnessFunction], [genotype], and [init] block.
 *
 * __Usage:__
 * ```
 * val engine = engine(::fitnessFn, genotype {
 *     chromosomes = listOf(BoolChromosome.Factory(20, 0.15))
 * }) {
 *     populationSize = 500
 *     alterers = listOf(Mutator(0.55), SinglePointCrossover(0.06))
 *     limits = listOf(SteadyGenerations(20), GenerationCount(100))
 * }
 * ```
 *
 * @param fitnessFunction A function that evaluates the fitness of a given `Genotype`.
 * @param genotype A factory for creating a new `Genotype` instance.
 * @param init A lambda block that allows configuring the engine by setting properties such as
 *  population size, alterers (e.g. mutation and crossover), and termination conditions (e.g.
 *  steady generations).
 *
 * @return An [Engine] instance that can be used to evolve a population towards better
 *  solutions.
 */
fun <DNA, G: Gene<DNA, G>> engine(
    fitnessFunction: (Genotype<DNA, G>) -> Double,
    genotype: Genotype.Factory<DNA, G>,
    init: Engine.Builder<DNA, G>.() -> Unit
): Engine<DNA, G> = Engine.Builder(fitnessFunction, genotype).apply(init).build()