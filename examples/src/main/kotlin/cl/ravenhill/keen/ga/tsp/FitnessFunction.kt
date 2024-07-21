/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.ga.tsp

import cl.ravenhill.keen.genetic.Genotype
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Calculates the Euclidean distance to another point.
 *
 * @param other The other point to calculate the distance to.
 * @return The Euclidean distance between this point and the other point.
 */
private infix fun Pair<Int, Int>.distanceTo(other: Pair<Int, Int>): Double {
    val dx = (first - other.first).toDouble() // Difference in X
    val dy = (second - other.second).toDouble() // Difference in Y
    return sqrt(dx.pow(2) + dy.pow(2))
}

/**
 * Calculates the fitness of a given genotype in the context of the Traveling Salesman Problem.
 *
 * This function is an essential component of the [TravelingSalesmanProblem], where it evaluates the efficiency of a
 * route represented by a genotype. The fitness score is determined based on the total distance of the route, with the
 * objective of minimizing this distance.
 *
 * ## Process:
 * 1. **Route Construction**: The function constructs a route based on the order of points suggested by the genotype.
 * 2. **Distance Calculation**: The total distance of the route is calculated by summing the distances between
 *    consecutive points in the route.
 * 3. **Fitness Score Computation**: The fitness score is the total distance of the route. A lower score indicates a
 *    shorter and more efficient route.
 *
 * ## Implementation Details:
 * - The genotype is a sequence of [RoutePointGene] objects, each representing a point in the route.
 * - The distance between consecutive points is calculated using the Euclidean distance formula.
 * - The total distance is the sum of these individual distances.
 *
 * @param genotype The genotype representing a sequence of route points. Each gene corresponds to a coordinate in the
 *   route.
 * @return The fitness score as a `Double`. Lower scores indicate shorter and more efficient routes.
 */
fun TravelingSalesmanProblem.fitnessFunction(genotype: Genotype<Pair<Int, Int>, RoutePointGene>): Double {
    val routePoints = genotype.flatten()
    var distance = 0.0
    routePoints.zipWithNext { a, b -> distance += a distanceTo b }
    return distance
}
