/*
 *  Copyright (c) 2023, Ignacio Slater M.
 *  2-Clause BSD License.
 */


package cl.ravenhill.keen.builders

import cl.ravenhill.keen.assertions.builders.`test adding chromosome factory to GenotypeScope`
import cl.ravenhill.keen.genetic.genes.BoolGene
import cl.ravenhill.keen.genetic.genes.CharGene
import cl.ravenhill.keen.genetic.genes.ProgramGene
import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.prog.Program
import io.kotest.core.spec.style.FreeSpec


class ChromosomeScopeTest : FreeSpec({
    "A Chromosome Scope" - {
        "can add a boolean chromosome factory to a Genotype Scope" {
            `test adding chromosome factory to GenotypeScope`<Boolean, BoolGene> {
                booleans { }
            }
        }

        "can add a char chromosome factory to a Genotype Scope" {
            `test adding chromosome factory to GenotypeScope`<Char, CharGene> {
                chars { }
            }
        }

        "can add a double chromosome factory to a Genotype Scope" {
            `test adding chromosome factory to GenotypeScope`<Double, DoubleGene> {
                doubles { }
            }
        }

        "can add an integer chromosome factory to a Genotype Scope" {
            `test adding chromosome factory to GenotypeScope`<Int, IntGene> {
                ints { }
            }
        }

        "can add a program chromosome factory to a Genotype Scope" {
            `test adding chromosome factory to GenotypeScope`<Program<Int>, ProgramGene<Int>> {
                program { }
            }
        }
    }
})
