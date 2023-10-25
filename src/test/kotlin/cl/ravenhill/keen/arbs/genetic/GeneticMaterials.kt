/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.arbs.genetic

import io.kotest.property.Arb
import io.kotest.property.arbitrary.choice

/**
 * Provides an arbitrary generator for creating random genetic material instances.
 *
 * This extension function is used to generate random instances of various genetic
 * material types.
 * The types include chromosomes, genes, genotypes, and individuals.
 *
 * The specific type of genetic material is determined by randomly selecting
 * one of the provided choices. This method is useful for testing scenarios where
 * any form of genetic material is applicable or required.
 *
 * @receiver Arb.Companion The companion object of the arbitrary type, allowing this
 *                         function to act as an extension.
 *
 * @return An [Arb] instance that yields random genetic material, with the specific type
 *         being one of the choices provided.
 */
fun Arb.Companion.geneticMaterial() = choice(
    chromosome(),
    gene(),
    genotype(),
    individual()
)

