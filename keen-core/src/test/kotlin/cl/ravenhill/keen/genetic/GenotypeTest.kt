/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.arb.genetic.chromosomes.arbIntChromosome
import cl.ravenhill.keen.arb.genetic.chromosomes.arbNothingChromosome
import cl.ravenhill.keen.arb.genetic.genotype
import cl.ravenhill.keen.assertions.*
import cl.ravenhill.keen.genetic.genes.NothingGene
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldNotBeIn
import io.kotest.property.Arb
import io.kotest.property.PropTestConfig
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.assume
import io.kotest.property.checkAll

@OptIn(ExperimentalKotest::class)
class GenotypeTest : FreeSpec({
    include(`test Genotype creation`())
    include(`test Genotype verification`())
    include(`test Genotype behavior`())
    include(`test Genotype Factory behaviour`())
    include(`test Genotype emptiness`())

    "A Genotype instance" - {
        "when testing if it contains a chromosome" - {
            "should return true if the chromosome is in the genotype" {
                checkAll(Arb.genotype(arbIntChromosome())) { genotype ->
                    genotype.forEach { chromosome ->
                        genotype.contains(chromosome).shouldBeTrue()
                    }
                }
            }

            "should return false if the chromosome is not in the genotype" {
                checkAll(
                    PropTestConfig(maxDiscardPercentage = 30),
                    Arb.genotype(arbIntChromosome()),
                    arbIntChromosome()
                ) { genotype, chromosome ->
                    assume { chromosome shouldNotBeIn genotype.chromosomes }
                    genotype.contains(chromosome).shouldBeFalse()
                }
            }
        }

        "when testing if it contains all chromosomes in a list" - {
            "should return true if all chromosomes are in the genotype" {
                checkAll(Arb.genotype(arbIntChromosome())) { genotype ->
                    genotype.containsAll(genotype.chromosomes).shouldBeTrue()
                }
            }

            "should return false if any chromosome is not in the genotype" {
                checkAll(
                    Arb.genotype(arbIntChromosome()),
                    arbIntChromosome(size = Arb.int(1..5))
                ) { genotype, chromosome ->
                    assume { chromosome shouldNotBeIn genotype.chromosomes }
                    genotype.containsAll(genotype.chromosomes + chromosome).shouldBeFalse()
                }
            }
        }
    }
})
