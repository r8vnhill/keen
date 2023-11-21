/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arbs.operators

import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.operators.Alterer
import cl.ravenhill.keen.operators.crossover.Crossover
import cl.ravenhill.keen.operators.mutator.Mutator
import io.kotest.property.Arb
import io.kotest.property.arbitrary.choice

/**
 * Creates an arbitrary generator for [Alterer] instances specifically tailored for [IntGene].
 * This function utilizes the Kotest's [Arb] (Arbitrary) API to generate various types of alterers,
 * each capable of modifying genetic information in unique ways. It's particularly useful for property-based
 * testing within genetic algorithms, allowing a diverse range of alteration strategies to be explored.
 *
 * The function randomly selects between two types of alterers:
 * - [Crossover] alterers, as generated by [intCrossover]: These alterers mix genetic information
 *   between different individuals, simulating the biological process of crossover.
 * - [Mutator] alterers, as generated by [mutator]<[Int], [IntGene]>: These alterers introduce random
 *   changes to the genetic material, mimicking natural mutations.
 *
 * This diversity in alteration strategies is crucial for testing the robustness and effectiveness
 * of genetic algorithms under various genetic manipulation techniques.
 *
 * @return An [Arb] that, on each invocation, generates either a crossover or a mutation alterer
 *         suitable for [IntGene].
 */
fun Arb.Companion.intAlterer(): Arb<Alterer<Int, IntGene>> = choice(intCrossover(), mutator<Int, IntGene>())
