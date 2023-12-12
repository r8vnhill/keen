/*
 *  Copyright (c) 2023, Ignacio Slater M.
 *  2-Clause BSD License.
 */


package cl.ravenhill.keen.builders

import cl.ravenhill.keen.arbs.genetic.chromosomes.nothingChromosomeFactory
import cl.ravenhill.keen.genetic.genes.NothingGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll


class GenotypeScopeTest : FreeSpec({
    "A Genotype Scope" - {
        "should have a list of chromosomes that" - {
            "is empty when created" {
                GenotypeScope<Nothing, NothingGene>().chromosomes.shouldBeEmpty()
            }

            "can be modified" {
                checkAll(Arb.list(Arb.nothingChromosomeFactory(), 0..10)) { chromosomes ->
                    val scope = GenotypeScope<Nothing, NothingGene>()
                    scope.chromosomes.shouldBeEmpty()
                    scope.chromosomes += chromosomes
                    scope.chromosomes.shouldHaveSize(chromosomes.size)
                }
            }
        }
    }
})
