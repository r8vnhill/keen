/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetics

import cl.ravenhill.keen.genetics.chromosomes.SimpleChromosomeFactory
import cl.ravenhill.keen.genetics.chromosomes.arbSimpleChromosomeFactory
import cl.ravenhill.keen.genetics.genes.SimpleGene
import cl.ravenhill.keen.genetics.genotype.GenotypeFactory
import cl.ravenhill.matchers.shouldBeRight
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll

class GenotypeFactoryTest : FreeSpec({
    "A GenotypeFactory" - {
        "should be able to create a genotype" {
            checkAll(arbGenotypeFactory()) { factory ->
                val genotype = factory()
                    .shouldBeRight()
                    .getOrNull()!!
                genotype.forEach { chromosome ->
                    chromosome.forEachIndexed { index, gene ->
                        gene shouldBe SimpleGene(index)
                    }
                }
            }
        }
    }
})

/**
 * Generates an arbitrary instance of a `GenotypeFactory` for property-based testing.
 *
 * @param chromosomeFactoryArb An [Arb] generator for a list of `SimpleChromosomeFactory` instances. Defaults to
 *   generating a list using `arbSimpleChromosomeFactory()`.
 * @return An [Arb] generator for creating instances of `GenotypeFactory<Int, SimpleGene>`.
 */
fun arbGenotypeFactory(
    chromosomeFactoryArb: Arb<List<SimpleChromosomeFactory>> =
        Arb.list(arbSimpleChromosomeFactory())
) = chromosomeFactoryArb.map {
    GenotypeFactory<Int, SimpleGene>().apply { chromosomes.addAll(it) }
}
