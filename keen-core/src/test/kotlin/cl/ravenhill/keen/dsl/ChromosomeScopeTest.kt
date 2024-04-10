/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.dsl

import cl.ravenhill.keen.arb.genetic.chromosomes.arbDoubleChromosomeFactory
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll

class ChromosomeScopeTest : FreeSpec({

    "A Chromosome Scope" - {
        "can add a chromosome factory to a Genotype Scope" {
            checkAll(Arb.list(arbDoubleChromosomeFactory(), 0..10)) { factories ->
                val scope = GenotypeScope<Double, DoubleGene>()
                factories.forEach { scope.chromosomeOf { it } }
                scope.chromosomes.map { it } shouldBe factories
            }
        }
    }
})