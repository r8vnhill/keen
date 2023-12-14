/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.arb.genetic.genes.doubleGene
import cl.ravenhill.keen.arb.range
import cl.ravenhill.keen.assertions.`each gene should have the specified range`
import cl.ravenhill.keen.assertions.`each gene should pass the specified filter`
import cl.ravenhill.keen.assertions.`validate all genes against single filter`
import cl.ravenhill.keen.assertions.`validate all genes against single range`
import cl.ravenhill.keen.assertions.`validate genes with specified range and factory`
import cl.ravenhill.keen.genetic.chromosomes.numeric.DoubleChromosome
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import cl.ravenhill.keen.utils.nextDoubleInRange
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll

class DoubleChromosomeTest : FreeSpec({

    "A Double Chromosome" - {
        "should have a genes property that" - {
            "is set to a list provided in the constructor" {
                checkAll(Arb.list(Arb.doubleGene())) { genes ->
                    DoubleChromosome(genes).genes shouldBe genes
                }
            }

            "is set to the vararg provided in the constructor" {
                checkAll(Arb.list(Arb.doubleGene())) { genes ->
                    DoubleChromosome(*genes.toTypedArray()).genes shouldBe genes
                }
            }
        }

        "should be able to create a new chromosome with the provided genes" {
            checkAll(Arb.list(Arb.doubleGene())) { genes ->
                val chromosome = DoubleChromosome(genes)
                val newGenes = genes.map { it.copy() }
                val newChromosome = chromosome.duplicateWithGenes(newGenes)
                newChromosome.genes shouldBe newGenes
            }
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
                    Arb.range(Arb.double(), Arb.double()).filterNot { it.start.isNaN() || it.endInclusive.isNaN() }
                ) {
                    DoubleChromosome.Factory()
                }
            }

            "without an explicit filter should default all genes to the filter { true }" {
                `each gene should pass the specified filter`(Arb.double()) {
                    DoubleChromosome.Factory()
                }
            }

            "with an explicit filter should use the provided filter" {
                `validate all genes against single filter`(Arb.double(), { true }) {
                    DoubleChromosome.Factory()
                }
            }

            "with valid ranges and filters should create a chromosome with genes that satisfy the constraints" {
                `validate genes with specified range and factory`(
                    Arb.range(Arb.double(), Arb.double()), { rng, ranges, index ->
                        DoubleGene(rng.nextDoubleInRange(ranges[index]), ranges[index])
                    }) { DoubleChromosome.Factory() }
            }
        }
    }
})
