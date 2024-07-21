package cl.ravenhill.keen.ga.primefact

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import kotlin.math.abs

/**
 * Computes the absolute difference between the product of gene values in a genotype and the predefined target number.
 *
 * This function serves as the fitness function in the genetic algorithm for the Prime Factorization Problem. It
 * evaluates how close a given genotype's gene product is to the specified target number (420). The fitness is measured
 * as the absolute difference between the product of the genes in the genotype and the target number. A lower fitness
 * value indicates that the gene product is closer to the target, making the genotype a more suitable solution in the
 * context of factorization.
 *
 * A genotype with a fitness value of 0.0 would indicate a perfect factorization of the target number.
 *
 * @param genotype The genotype to be evaluated for fitness.
 * @return The absolute difference between the product of the genes in the genotype and the target number.
 */
fun PrimeFactorizationProblem.absDiff(genotype: Genotype<Int, IntGene>) = abs(
    TARGET.toLong() - genotype.flatMap { it.toLong() }.fold(1L) { acc, i -> acc * i }
).toDouble()
