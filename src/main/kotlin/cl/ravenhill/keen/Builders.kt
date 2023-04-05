package cl.ravenhill.keen

import cl.ravenhill.keen.evolution.CoroutineEvaluator
import cl.ravenhill.keen.evolution.Engine
import cl.ravenhill.keen.evolution.Evaluator
import cl.ravenhill.keen.evolution.SequentialEvaluator
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Phenotype
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

    /**
     * Returns an [Evaluator.Factory] instance initialized with custom settings through [init].
     *
     * __Usage:__
     * ```
     * val evaluatorFactory = evaluator<Int> {
     *   creator = { MyEvaluator(it) }
     * }
     * ```
     *
     * @param init A function that initializes an [Evaluator.Factory] instance with custom settings.
     * @return An [Evaluator.Factory] instance.
     */
    fun <DNA> evaluator(init: Evaluator.Factory<DNA>.() -> Unit): Evaluator.Factory<DNA> =
        Evaluator.Factory<DNA>().apply(init)

    /**
     * Returns a [CoroutineEvaluator.Factory] instance initialized with custom settings through
     * [init].
     * The [CoroutineEvaluator] instances created by this factory will use coroutines to evaluate
     * fitness functions for [Phenotype] instances in a [Population].
     *
     * __Usage:__
     * ```
     * val coroutineEvaluatorFactory = coroutines<Int> {
     *     dispatcher = Dispatchers.IO // Use the IO dispatcher for parallel evaluation
     *     chunkSize = 50 // Use a chunk size of 50 for parallel evaluation
     * }
     * ```
     *
     * @param init A function that initializes a [CoroutineEvaluator.Factory] instance with custom settings.
     * @return A [CoroutineEvaluator.Factory] instance.
     */
    fun <DNA> coroutines(init: CoroutineEvaluator.Factory<DNA>.() -> Unit = {}) =
        CoroutineEvaluator.Factory<DNA>()
            .apply(init)
            .apply {
                creator = { CoroutineEvaluator(it, dispatcher, chunkSize) }
            }

    /**
     * Sets the ``creator`` property of this [Evaluator.Factory] instance to create
     * [SequentialEvaluator] instances.
     * The [SequentialEvaluator] instances created by this factory will evaluate fitness functions
     * for [Phenotype] instances in a [Population] sequentially.
     *
     * __Usage:__
     * ```
     * val sequentialEvaluatorFactory = evaluator<String> {
     *     sequential() // Use the SequentialEvaluator implementation
     * }
     * ```
     *
     * @receiver An [Evaluator.Factory] instance.
     */
    fun <DNA> Evaluator.Factory<DNA>.sequential() {
        creator = { SequentialEvaluator(it) }
    }
}
