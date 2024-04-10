/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.benchmarks.numopt.functions

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Represents the Ackley optimization problem.
 *
 * The Ackley function is a well-known benchmark problem in the field of optimization. It is used for testing
 * optimization algorithms and has a global minimum at the origin (0, 0). This implementation defines the Ackley
 * function with constants to improve readability and maintainability.
 *
 * ## Functionality:
 * The [invoke] method evaluates the Ackley function given a genotype consisting of `Double` values.
 *
 * @property A Coefficient used in the exponential term of the function. Default is 20.0.
 * @property B Coefficient used to scale the argument of the first exponential term. Default is 0.2.
 * @property C Coefficient used to scale the arguments of the cosine functions. Default is 2.0 * PI.
 * @property D Coefficient used to scale the argument of the second exponential term. Default is 0.5.
 * @property name The name of the optimization problem, which is "Ackley".
 * @property target The minimum value of the Ackley function, which is 0.0.
 */
data object Ackley : OptimizationProblem {
    private const val A = 20.0
    private const val B = 0.2
    private const val C = 2.0 * PI
    private const val D = 0.5

    override val name = "Ackley"
    override val target = 0.0
    private const val MIN = -5.0
    private const val MAX = 5.0
    override val ranges: List<ClosedRange<Double>> = listOf(MIN..MAX)

    /**
     * Evaluates the Ackley function for a given genotype.
     *
     * @param genotype The genotype to be evaluated, consisting of `Double` values.
     * @return The calculated fitness value according to the Ackley function.
     */
    override fun invoke(genotype: Genotype<Double, DoubleGene>): Double = genotype.flatten().let { (x, y) ->
        -A * exp(-B * sqrt(D * (x.pow(2) + y.pow(2)))) -
              exp(D * (cos(C * x) + cos(C * y))) + exp(1.0) + A
    }
}
