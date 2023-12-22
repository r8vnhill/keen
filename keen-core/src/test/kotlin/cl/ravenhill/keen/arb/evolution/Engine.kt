/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb.evolution

import cl.ravenhill.keen.evolution.EvolutionEngine
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.Gene
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary

//fun <T, G> Arb.Companion.engine(
//    genotypeFactory: Arb<Genotype.Factory<T, G>>,
//) where G : Gene<T, G> = arbitrary {
//    EvolutionEngine(
//        genotypeFactory = genotypeFactory.bind()
//
//    )
//}
