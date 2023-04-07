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
import cl.ravenhill.keen.genetic.chromosomes.Chromosome

/**
 * Provides factory methods to create instances of genetic algorithm components, such as [Engine]s,
 * [Genotype]s, and [Chromosome]s.
 * For example, to create an engine that evolves a population towards better solutions, you can use
 * the [engine] method with a fitness function, a genotype factory, and an initialization block
 * that sets population size, alterers, and termination conditions.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 1.0.0
 */
object Builders {


    /**
     * Creates a new [Genotype] with the given [init] block.
     *
     * __Usage:__
     * ```
     * genotype {
     *     chromosome {
     *         booleans {
     *             size = 20
     *             truesProbability = 0.15
     *         }
     *     }
     * }
     * ```
     *
     * @param init A lambda block that allows configuring the genotype by specifying its chromosomes.
     *
     * @return A `Genotype` instance that contains the specified chromosomes.
     *
     * @see Genotype
     * @see Chromosomes
     */
    fun <DNA> genotype(init: Genotype.Factory<DNA>.() -> Unit) =
        Genotype.Factory<DNA>().apply(init)

    /**
     * A utility object that provides factory methods to create different types of chromosomes used
     * in genetic algorithms.
     *
     * @see Chromosome
     * @see Chromosome.Factory
     */
    object Chromosomes {

        /**
         * Creates a new [BoolChromosome.Factory] with the given [builder] block.
         *
         * __Usage:__
         * ```
         * booleans {
         *     size = 20
         *     truesProbability = 0.15
         * }
         * ```
         *
         * @param builder A lambda block that allows configuring the [BoolChromosome.Factory] by
         *  specifying its properties.
         *
         * @return A `BoolChromosome.Factory` instance with the specified properties.
         *
         * @see BoolChromosome
         * @see Chromosome
         */
        fun booleans(builder: BoolChromosome.Factory.() -> Unit) =
            BoolChromosome.Factory().apply(builder)

        /**
         * Creates a new [CharChromosome.Factory] with the given [builder] block.
         *
         * __Usage:__
         * ```
         * chars {
         *     size = 20
         * }
         * ```
         *
         * @param builder A lambda block that allows configuring the `CharChromosome.Factory` by
         *  specifying its properties.
         *
         * @return A `CharChromosome.Factory` instance with the specified properties.
         */
        fun chars(builder: CharChromosome.Factory.() -> Unit) =
            CharChromosome.Factory().apply(builder)

        /**
         * Creates a new [IntChromosome.Factory].
         *
         * __Usage:__
         * ```
         * ints {
         *     size = 20
         *     range = 0..100
         *     filter = { it % 2 == 0 }
         * }
         * ```
         *
         * @param builder A lambda block that allows configuring the properties of the
         *  [IntChromosome].
         *
         * @return An [IntChromosome.Factory] instance that can be used to create new
         *  [IntChromosome] instances.
         */
        fun ints(builder: IntChromosome.Factory.() -> Unit) =
            IntChromosome.Factory().apply(builder)

        /**
         * Creates a new [DoubleChromosome.Factory] with the given [builder] block.
         *
         * __Usage:__
         * ```
         * chromosome {
         *     doubles {
         *         size = 20
         *         range = 0.0..100.0
         *     }
         * }
         * ```
         *
         * @param builder A lambda block that allows configuring the properties of the
         *  [DoubleChromosome].
         *
         * @return A [DoubleChromosome.Factory] instance that can be used to create new
         *  [DoubleChromosome] instances.
         */
        fun doubles(builder: DoubleChromosome.Factory.() -> Unit) =
            DoubleChromosome.Factory().apply(builder)

        /**
         * Creates a new [ProgramChromosome.Factory] using the specified builder to configure it.
         *
         * __Usage:__
         * ```
         * program {
         *     function("*", 2) { it[0] * it[1] }
         *     function("+", 2) { it[0] + it[1] }
         *     terminal { EphemeralConstant { Core.random.nextInt(-1, 2).toDouble() } }
         *     terminal { Variable("x", 0) }
         * }
         * ```
         *
         * @param builder The lambda block that configures the properties of the [ProgramChromosome].
         *
         * @return A [ProgramChromosome.Factory] instance that can be used to create new
         *  [ProgramChromosome] instances.
         */
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
    fun <DNA> evaluator(init: () -> Evaluator.Factory<DNA>): Evaluator.Factory<DNA> = init()

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
