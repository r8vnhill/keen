/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.assertions.`check that an Absurd Operation is thrown`
import io.kotest.core.spec.style.FreeSpec


class NothingGeneTest : FreeSpec({

        "A Nothing Gene" - {
            "should throw an AbsurdOperation object when" - {
                "accessing it's value" {
                    `check that an Absurd Operation is thrown`(NothingGene::value)
                }

                "mutating" {
                    `check that an Absurd Operation is thrown`(NothingGene::mutate)
                }

                "flattening" {
                    `check that an Absurd Operation is thrown`(NothingGene::flatten)
                }
            }
        }
})
