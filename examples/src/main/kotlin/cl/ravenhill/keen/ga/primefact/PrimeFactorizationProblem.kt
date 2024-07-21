package cl.ravenhill.keen.ga.primefact

import cl.ravenhill.keen.dsl.chromosomeOf
import cl.ravenhill.keen.dsl.evolutionEngine
import cl.ravenhill.keen.dsl.genotypeOf
import cl.ravenhill.keen.evolution.EvolutionEngine
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import cl.ravenhill.keen.limits.steadyGenerations
import cl.ravenhill.keen.limits.targetFitness
import cl.ravenhill.keen.listeners.ListenerConfiguration
import cl.ravenhill.keen.listeners.mixins.EvolutionListener
import cl.ravenhill.keen.operators.alteration.crossover.UniformCrossover
import cl.ravenhill.keen.ranking.FitnessMinRanker

/**
 * An object that encapsulates the configuration and execution of a genetic algorithm for prime factorization.
 *
 * `PrimeFactorizationProblem` defines the constants and methods necessary to set up and run a genetic algorithm aimed
 * at factorizing a target number into its prime components. It includes settings for population size, chromosome size,
 * termination criteria, and candidate factors used in genetic computations.
 */
object PrimeFactorizationProblem {
    /**
     * The target number for the prime factorization in the genetic algorithm.
     *
     * `TARGET` specifies the integer that the genetic algorithm attempts to factorize into its prime components. The
     * value is set to 420, serving as the goal for the algorithm's factorization process.
     */
    const val TARGET = 420

    /**
     * The size of the population in the genetic algorithm.
     *
     * `POPULATION_SIZE` sets the total number of individuals in each generation of the genetic algorithm. The constant
     * is defined with a value of 5000, establishing the scale of the population that will be evolved and evaluated
     * during the algorithm's execution.
     */
    private const val POPULATION_SIZE = 5000

    /**
     * The specified number of genes in each chromosome for the genetic algorithm.
     *
     * `CHROMOSOME_SIZE` defines the length of each chromosome within the population of the genetic algorithm. It
     * determines how many genes each chromosome will contain, with the value set to 15. This size impacts the genetic
     * diversity and complexity of the solutions that the algorithm can explore.
     */
    private const val CHROMOSOME_SIZE = 15

    /**
     * The number of generations with no improvement in fitness after which the genetic algorithm will terminate.
     *
     * This constant is used as a criterion for stopping the genetic algorithm's evolution process. If there is no
     * improvement in the fitness of the population for `STEADY_GENERATIONS` consecutive generations, it is inferred
     * that the algorithm has potentially reached a state of convergence, and the evolutionary process is halted. The
     * value is set to 500, meaning the algorithm will stop if there is no improvement observed over 500 successive
     * generations.
     */
    private const val STEADY_GENERATIONS = 500

    /**
     * Lazily initialized list of candidate factors used for genetic computations in prime factorization.
     *
     * This property holds a list of numbers that includes all prime numbers up to a certain limit, plus the number 1.
     * The list is generated by the [primes] function, which calculates prime numbers, and then 1 is appended to this
     * list. Being a [lazy] property, `candidateFactors` is initialized only when it is first accessed, not when the
     * object containing it is created. This lazy initialization ensures efficiency, as the computation to generate
     * prime numbers is deferred until absolutely necessary.
     *
     * The inclusion of 1 alongside prime numbers is notable, as 1 is not a prime number. However, its presence in this
     * list suggests its utility in specific computations or algorithms where 1 is considered a valid factor, such as in
     * certain types of factorization problems.
     */
    val candidateFactors: List<Int> by lazy { primes() + 1 }

    /**
     * Sets up and runs the genetic algorithm for prime factorization with the specified observers.
     *
     * @param observers a vararg parameter of listener factories that create observers for the evolution process
     * @return the configured `EvolutionEngine` instance after it has evolved
     */
    operator fun invoke(
        vararg observers: (ListenerConfiguration<Int, IntGene>) -> EvolutionListener<Int, IntGene>
    ): EvolutionEngine<Int, IntGene> {
        val engine = evolutionEngine(
            ::absDiff,
            genotypeOf {
                chromosomeOf {
                    PrimeChromosomeFactory(CHROMOSOME_SIZE)
                }
            }
        ) {
            populationSize = POPULATION_SIZE
            alterers += listOf(
                PrimeMutator,
                UniformCrossover(chromosomeRate = 0.3)
            )
            ranker = FitnessMinRanker()
            limitFactories += listOf(targetFitness(0.0), steadyGenerations(STEADY_GENERATIONS))
            listenerFactories += observers
        }
        engine.evolve()
        return engine
    }
}
