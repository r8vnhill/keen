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
import cl.ravenhill.keen.genetic.chromosomes.CharChromosome
import cl.ravenhill.keen.genetic.chromosomes.ProgramChromosome
import cl.ravenhill.keen.genetic.chromosomes.numerical.DoubleChromosome
import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome

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
     *      chromosome { booleans(20, 0.15) }
     *  }
     * ```
     */
    fun <DNA> genotype(init: Genotype.Factory<DNA>.() -> Unit) =
        Genotype.Factory<DNA>().apply(init)

    /**
     * Builder blocks for [Chromosome.Factory]s.
     */
    object Chromosomes {
        /**
         * Creates a new [BoolChromosome.Factory].
         *
         * __Usage:__
         * ```
         *  chromosome {
         *      booleans {
         *          size = 20
         *          truesProbability = 0.15
         *      }
         *  }
         * ```
         */
        fun booleans(builder: BoolChromosome.Factory.() -> Unit) =
            BoolChromosome.Factory().apply(builder)

        /**
         * Creates a new [CharChromosome.Factory].
         *
         * __Usage:__
         * ```
         * chromosome { chars { size = 20 } }
         * ```
         */
        fun chars(builder: CharChromosome.Factory.() -> Unit) =
            CharChromosome.Factory().apply(builder)

        /**
         * Creates a new [IntChromosome.Factory].
         *
         * __Usage:__
         * ```
         *  chromosome {
         *      ints {
         *          size = 20
         *          range = 0 to 100
         *          filter = { it % 2 == 0 }
         *      }
         *  }
         */
        fun ints(builder: IntChromosome.Factory.() -> Unit) =
            IntChromosome.Factory().apply(builder)

        /**
         * Creates a new [DoubleChromosome.Factory].
         *
         * __Usage:__
         * ```
         *  chromosome {
         *      doubles {
         *          size = 20
         *          range = 0.0 to 100.0
         *      }
         *  }
         *  ```
         */
        fun doubles(builder: DoubleChromosome.Factory.() -> Unit) =
            DoubleChromosome.Factory().apply(builder)

        fun <T> program(builder: ProgramChromosome.Factory<T>.() -> Unit) =
            ProgramChromosome.Factory<T>().apply(builder)
    }
}