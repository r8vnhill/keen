/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.dsl.chromosomeOf
import cl.ravenhill.keen.dsl.geneticAlgorithm
import cl.ravenhill.keen.dsl.genotypeOf
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.genes.BooleanGene

private fun count(genotype: Genotype<Boolean, BooleanGene>) = genotype.flatten().count { it }.toDouble()

fun oneMax() {
    val engine = geneticAlgorithm(
        ::count,
        genotypeOf {
//            chromosomeOf {
//
//            }
        }
    ) {}
}
