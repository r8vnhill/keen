/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.genetic.chromosomes.SimpleChromosome
import cl.ravenhill.keen.genetic.chromosomes.arbChromosome
import cl.ravenhill.keen.genetic.genes.arbSimpleGene
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.chromosomes.Chromosome
import cl.ravenhill.keen.genetics.genes.Gene
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll

class GenotypeTest : FreeSpec({
    "A Genotype" - {
        "can be created" - {
            "with a list of chromosomes" {
                checkAll(Arb.list(arbChromosome(arbSimpleGene()))) { chromosomes ->
                    val genotype = Genotype(chromosomes)
                    genotype.chromosomes shouldBe chromosomes
                }
            }

            "with variadic chromosomes" {
                checkAll(Arb.list(arbChromosome(arbSimpleGene()))) { chromosomes ->
                    val genotype = Genotype(*chromosomes.toTypedArray())
                    genotype.chromosomes shouldBe chromosomes
                }
            }
        }

        "can be flattened" - {
            checkAll(
                arbGenotypeAndFlattenedChromosomes(
                    arbSimpleGene(),
                    ::SimpleChromosome
                )
            ) { (genotype, flattenedGenes) ->
                genotype.flatten() shouldBe flattenedGenes
            }
        }

        "should have a size property that is equal to the size of the list of chromosomes" {
            checkAll(Arb.list(arbChromosome(arbSimpleGene()))) { chromosomes ->
                Genotype(chromosomes).size shouldBe chromosomes.size
            }
        }


    }
})

private fun <T, G> arbGenotypeAndFlattenedChromosomes(
    geneArb: Arb<G>,
    chromosomeBuilder: (List<G>) -> Chromosome<T, G>
) where G : Gene<T, G> = arbitrary {
    val genotypeSize = Arb.int(0..10).bind()
    val chromosomes = mutableListOf<Chromosome<T, G>>()
    val flattenedGenes = mutableListOf<T>()
    repeat(genotypeSize) {
        val chromosomeSize = Arb.int(0..10).bind()
        val genes = mutableListOf<G>()
        repeat(chromosomeSize) {
            genes += geneArb.bind()
            flattenedGenes += genes.last().value
        }
        chromosomes += chromosomeBuilder(genes)
    }
    Genotype(chromosomes) to flattenedGenes
}
