/*
 * "Makarena" (c) by R8V.
 * "Makarena" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */

package cl.ravenhill.keen

import cl.ravenhill.keen.evolution.Engine
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.chromosomes.BoolChromosome

/**
 * Builder methods for Keen core classes.
 */
object Builders {

    /**
     * Creates a new [Engine] with the given ``fitnessFunction``, ``genotype`` and ``init`` block.
     *
     * __Usage:__
     * ```
     *  val engine = engine(::fitnessFn, genotype {
     *      chromosomes = listOf(BoolChromosome.Factory(20, 0.15))
     *  }) {
     *      populationSize = 500
     *      alterers = listOf(Mutator(0.55), SinglePointCrossover(0.06))
     *      limits = listOf(SteadyGenerations(20), GenerationCount(100))
     *  }
     * ```
     */
    fun <DNA> engine(
        fitnessFunction: (Genotype<DNA>) -> Double,
        genotype: Genotype.Factory<DNA>,
        init: Engine.Builder<DNA>.() -> Unit
    ) = Engine.Builder(fitnessFunction, genotype).apply(init).build()

    /**
     * Creates a new [Genotype] with the given ``init`` block.
     *
     * __Usage:__
     * ```
     *  genotype {
     *      chromosomes = listOf(BoolChromosome.Factory(20, 0.15))
     *  }
     * ```
     */
    fun <DNA> genotype(init: Genotype.Factory<DNA>.() -> Unit) =
        Genotype.Factory<DNA>().apply(init)

    object Chromosomes {
        /**
         * Creates a new [BoolChromosome.Factory] with the given ``size`` and ``truesProbability``.
         *
         * __Usage:__
         * ```
         *  BoolChromosome.Factory(20, 0.15)
         * ```
         */
        fun bool(size: Int, truesProbability: Double) =
            BoolChromosome.Factory(size, truesProbability)
    }
}