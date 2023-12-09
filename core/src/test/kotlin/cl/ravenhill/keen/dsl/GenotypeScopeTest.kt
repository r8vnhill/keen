/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.dsl

import cl.ravenhill.keen.arb.genetic.chromosomes.doubleChromosomeFactory
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import io.kotest.assertions.fail
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll

class GenotypeScopeTest : FreeSpec({

    "A [GenotypeScope]" - {
        "should have a list of chromosome factories that" - {
            "is empty when created" {
                GenotypeScope<Double, DoubleGene>().chromosomes.shouldBeEmpty()
            }

            "can be modified" {
                checkAll(Arb.list(Arb.doubleChromosomeFactory())) { factories ->
                    val scope = GenotypeScope<Double, DoubleGene>()
                    factories.forEach { scope.chromosomes += it }
                    scope.chromosomes shouldBe factories
                }
            }
        }
    }
})
