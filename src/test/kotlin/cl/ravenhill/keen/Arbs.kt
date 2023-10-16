/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen

import cl.ravenhill.keen.genetic.genes.BoolGene
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.element


/**
 * Generates an arbitrary [BoolGene] value, either [BoolGene.True] or [BoolGene.False].
 */
fun Arb.Companion.boolGene() = arbitrary { element(BoolGene.True, BoolGene.False).bind() }