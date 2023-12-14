/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.genetic.genes.NothingGene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll

class NothingChromosomeTest : FreeSpec({

    "A NothingChromosome instance" - {
        "can be created with a list of NothingGene objects" {
            checkAll(Arb.list(Arb.constant(NothingGene))) {
                NothingChromosome(it).genes shouldBe it
            }
        }

        "can create a duplicate with a new list of NothingGene objects" {
            checkAll(Arb.list(Arb.constant(NothingGene)), Arb.list(Arb.constant(NothingGene))) { original, new ->
                val duplicated = NothingChromosome(original).duplicateWithGenes(new)
                duplicated.genes shouldBe new
            }
        }
    }

    "A NothingChromosome.Factory instance" - {
        "can create a new NothingChromosome instance" {
            checkAll(Arb.list(Arb.constant(NothingGene))) {
                val chromosome = NothingChromosome.Factory().apply { size = it.size }.make()
                chromosome.genes shouldBe it
            }
        }
    }
})