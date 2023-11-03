/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.builders

import cl.ravenhill.keen.assertions.builders.`test adding chromosome factory to GenotypeScope`
import cl.ravenhill.keen.genetic.genes.BoolGene
import cl.ravenhill.keen.genetic.genes.CharGene
import io.kotest.core.spec.style.FreeSpec

class ChromosomeScopeTest : FreeSpec({
    "A [ChromosomeScope]" - {
        "can add a boolean chromosome factory to a [GenotypeScope]" {
            `test adding chromosome factory to GenotypeScope`<Boolean, BoolGene> {
                booleans { }
            }
        }

        "can add a char chromosome factory to a [GenotypeScope]" {
            `test adding chromosome factory to GenotypeScope`<Char, CharGene> {
                chars { }
            }
        }
    }
})
