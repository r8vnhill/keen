/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.arbs.genetic

import io.kotest.property.Arb
import io.kotest.property.arbitrary.choice

fun Arb.Companion.geneticMaterial() = choice(
    boolChromosome(),
    charChromosome(),
    doubleChromosome(),
    intChromosome(),
    boolGene(),
    charGene(),
    doubleGene(),
    intGene()
)
