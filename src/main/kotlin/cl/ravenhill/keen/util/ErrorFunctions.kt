package cl.ravenhill.keen.util

import kotlin.math.pow

/**
 * A collection of error functions.
 */

/**
 * The mean squared error (MSE)
 * The MSE is calculated as the average of the squared differences between the target and the
 * output.
 *
 * @param expected The expected output.
 * @param actual The actual output.
 * @return The MSE.
 */
fun meanSquaredError(expected: List<Double>, actual: List<Double>) =
    (expected zip actual).sumOf { (e, a) -> (e - a).pow(2.0) } / expected.size