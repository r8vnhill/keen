/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb.genetic

import cl.ravenhill.keen.genetic.GeneticMaterial
import cl.ravenhill.keen.genetic.genes.Gene
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary

/**
 * Creates an arbitrary generator for `GeneticMaterial<T, G>`.
 *
 * @param T The type of the underlying value in the genetic material.
 * @param G The type of gene used in the genetic material, which must extend the `Gene<T, G>` interface.
 * @param arb An arbitrary of type `T` that is used to generate the underlying value in the genetic material.
 * @return An `Arb<GeneticMaterial<T, G>>` which is capable of generating instances of genetic material with the
 *   specified types.
 */
fun <T, G> arbGeneticMaterial(arb: Arb<T>): Arb<GeneticMaterial<T, G>> where G : Gene<T, G> = arbitrary {
    val bounded = arb.bind()
    object : GeneticMaterial<T, G> {
        override fun flatten() = listOf(bounded)
    }
}
