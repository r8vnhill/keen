/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.arb.arbRange
import cl.ravenhill.keen.arb.datatypes.nonNaNDouble
import cl.ravenhill.keen.arb.genetic.chromosomes.doubleChromosome
import cl.ravenhill.keen.arb.genetic.genes.doubleGene
import cl.ravenhill.keen.assertions.`each gene should have the specified range`
import cl.ravenhill.keen.assertions.`each gene should pass the specified filter`
import cl.ravenhill.keen.assertions.`test chromosome gene consistency`
import cl.ravenhill.keen.assertions.`test that a gene can be duplicated with a new set of genes`
import cl.ravenhill.keen.assertions.`validate all genes against single filter`
import cl.ravenhill.keen.assertions.`validate all genes against single range`
import cl.ravenhill.keen.assertions.`validate genes with specified range and factory`
import cl.ravenhill.keen.genetic.chromosomes.numeric.DoubleChromosome
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import cl.ravenhill.keen.utils.nextDoubleInRange
import io.kotest.core.spec.style.FreeSpec
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filterNot

class DoubleChromosomeTest : FreeSpec({

    "A Double Chromosome" - {
        "should have a genes property that" - {
            "is set to a list provided in the constructor" {
                `test chromosome gene consistency`(Arb.doubleGene()) { DoubleChromosome(it) }
            }

            "is set to the vararg provided in the constructor" {
                `test chromosome gene consistency`(Arb.doubleGene()) { DoubleChromosome(*it.toTypedArray()) }
            }
        }

        "should be able to create a new chromosome with the provided genes" {
            `test that a gene can be duplicated with a new set of genes`(Arb.doubleChromosome(), Arb.doubleGene())
        }
    }

    "A Double Chromosome Factory" - {
        "when creating a new chromosome" - {
            "without an explicit range should default to the range -Double.MAX_VALUE..Double.MAX_VALUE" {
                `each gene should have the specified range`(-Double.MAX_VALUE..Double.MAX_VALUE) {
                    DoubleChromosome.Factory()
                }
            }

            "with an explicit range should use the provided range" {
                `validate all genes against single range`(
                    arbRange(Arb.nonNaNDouble(), Arb.nonNaNDouble())
                ) { DoubleChromosome.Factory() }
            }

            "without an explicit filter should default all genes to the filter { true }" {
                `each gene should pass the specified filter`(Arb.double()) {
                    DoubleChromosome.Factory()
                }
            }

            "with an explicit filter should use the provided filter" {
                `validate all genes against single filter`(
                    Arb.double().filterNot { it.isNaN() || it.isInfinite() },
                    { true }) {
                    DoubleChromosome.Factory()
                }
            }

            "with valid ranges and filters should create a chromosome with genes that satisfy the constraints" {
                `validate genes with specified range and factory`(
                    arbRange(Arb.double(), Arb.double()), { rng, ranges, index ->
                        DoubleGene(rng.nextDoubleInRange(ranges[index]), ranges[index])
                    }) { DoubleChromosome.Factory() }
            }
        }
    }
})
