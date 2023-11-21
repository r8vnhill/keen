/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.assertions.chromosomes

import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll

/**
 * Ensures that a chromosome built using a provided list of genes accurately reflects those genes.
 * This function performs a test to check that the genes of the built chromosome match the input genes exactly.
 *
 * @param T The gene's type parameter.
 * @param G The type of gene, which is self-referential.
 *
 * @param arb The arbitrary generator for the gene's type [T].
 * @param buildChromosome A lambda function that, given a list of genes, constructs a chromosome.
 *
 * @throws AssertionError If the genes in the constructed chromosome do not match the input genes.
 */
suspend fun <T, G : Gene<T, G>> `chromosome should reflect input genes`(
    arb: Arb<G>,
    buildChromosome: (List<G>) -> Chromosome<T, G>,
) {
    with(Arb) {
        checkAll(list(arb)) { genes ->
            buildChromosome(genes).genes shouldBe genes
        }
    }
}
