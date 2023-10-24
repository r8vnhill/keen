/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.arbs.intChromosome
import cl.ravenhill.keen.intGenotype
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll

class GenotypeTest : FreeSpec({
    "A [Genotype]" - {
        "when creating a new one" - {
            with(Arb) {
                "with a list of chromosomes then the genotype should have the same chromosomes" {
                    checkAll(list(intChromosome())) { chromosomes ->
                        Genotype(chromosomes).chromosomes shouldBe chromosomes
                    }
                }

                "with chromosomes as varargs then the genotype should have the same chromosomes" {
                    checkAll(list(intChromosome())) { chromosomes ->
                        Genotype(*chromosomes.toTypedArray()).chromosomes shouldBe chromosomes
                    }
                }
            }
        }

        "size can be read" {
            with(Arb) {
                checkAll(intGenotype()) { genotype ->
                    genotype.size shouldBe genotype.chromosomes.size
                }
            }
        }

        "can verify itself when" - {
            "all chromosomes are valid then it should return true" {
                with(Arb) {
                    checkAll(intGenotype()) { genotype ->
                        genotype.verify() shouldBe true
                    }
                }
            }

//            "at least one chromosome is invalid then it should return false" {
//                with(Arb) {
//                    checkAll(list(intChromosome())) { chromosomes ->
//                        val invalidChromosome = chromosomes.random().withGenes(emptyList())
//                        Genotype(chromosomes + invalidChromosome).verify() shouldBe false
//                    }
//                }
//            }
        }
    }
})
