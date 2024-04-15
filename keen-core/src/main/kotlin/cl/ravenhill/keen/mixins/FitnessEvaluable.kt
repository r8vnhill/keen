package cl.ravenhill.keen.mixins

/**
 * Defines an interface for objects that can be evaluated for fitness in the context of evolutionary algorithms.
 * Fitness is a crucial concept in evolutionary algorithms, representing how well an individual (or a solution) performs
 * in the given problem space.
 *
 * ## Usage:
 * Implement this interface in classes that represent individuals or solutions in an evolutionary algorithm. The `fitness`
 * property should reflect the quality of the solution, and `isEvaluated()` should indicate whether the solution's fitness
 * has been calculated.
 *
 * ### Example 1: Implementing FitnessEvaluable in a Genetic Algorithm
 * ```
 * class GeneticSolution(val genotype: List<Int>) : FitnessEvaluable {
 *     override val fitness: Double by lazy { calculateFitness() }
 *
 *     override fun isEvaluated() = fitness.isFinite()
 *
 *     private fun calculateFitness(): Double {
 *         // Fitness calculation logic here
 *     }
 * }
 * ```
 *
 * ### Example 2: Using FitnessEvaluable in an Evolutionary Strategy
 * ```
 * class StrategySolution(val parameters: Map<String, Double>) : FitnessEvaluable {
 *     override val fitness: Double by lazy { evaluate() }
 *
 *     override fun isEvaluated() = fitness != Double.MAX_VALUE
 *
 *     private fun evaluate(): Double {
 *         // Evaluation logic here
 *     }
 * }
 * ```
 * @property fitness The calculated fitness of the individual or solution. Higher values indicate better fitness.
 */
interface FitnessEvaluable {
    val fitness: Double

    /**
     * Determines if the fitness of the entity has been evaluated.
     *
     * ## Usage:
     * This method should be called to check if the fitness value of an entity is calculated and valid before using it in
     * evolutionary operations like selection, crossover, or mutation.
     *
     * @return `true` if the fitness has been evaluated and set, `false` otherwise.
     */
    fun isEvaluated(): Boolean
}
