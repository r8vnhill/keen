/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arbs.operators

import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import io.kotest.property.Arb
import io.kotest.property.arbitrary.element

fun Arb.Companion.combiner() = element(
    { g: List<IntGene> -> IntGene(g.sumOf { it.dna }) },

)