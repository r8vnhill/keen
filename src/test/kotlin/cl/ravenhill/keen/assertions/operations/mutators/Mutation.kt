/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.assertions.operations.mutators

import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.operators.mutator.ChromosomeMutator
import cl.ravenhill.keen.operators.mutator.GeneMutator
import cl.ravenhill.keen.arbs.datatypes.real
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.checkAll

/**
 * Validates that a gene remains unchanged when mutated with a zero mutation rate.
 *
 * This function asserts that a gene, sourced from the provided [geneArb] generator,
 * remains unaltered after mutation using a mutator created by the [mutatorBuilder]
 * (which is expected to produce a mutator with a zero mutation rate).
 *
 * Specifically:
 * - The resulting gene after mutation should be the same as the original.
 * - The number of mutations applied should be zero.
 *
 * @param T The type representing the genetic data or information.
 * @param G The type of gene that the mutator operates on, which holds [T] type data.
 * @param geneArb Arbitrary generator for genes of type [G].
 * @param mutatorBuilder A builder function that returns a [GeneMutator] instance without requiring any arguments.
 *
 * @throws AssertionError if the mutated gene is different from the original or if any mutations are detected.
 */
suspend fun <T, G> `validate unchanged gene with zero mutation rate`(
    geneArb: Arb<G>,
    mutatorBuilder: () -> GeneMutator<T, G>
) where G : Gene<T, G> {
    checkAll(geneArb) { gene ->
        val mutator = mutatorBuilder()
        val result = mutator.mutateGene(gene)
        result.mutated shouldBe gene
        result.mutations shouldBe 0
    }
}

suspend fun <T, G, C> `validate unchanged chromosome with zero mutation rate`(
    geneArb: Arb<C>,
    mutatorBuilder: (probability: Double, geneRate: Double) -> ChromosomeMutator<T, G>
) where G : Gene<T, G>, C : Chromosome<T, G> {
    checkAll(
        geneArb,
        Arb.real(0.0..1.0),
        Arb.real(0.0..1.0)
    ) { chromosome, probability, geneRate ->
        val mutator = mutatorBuilder(probability, geneRate)
        val result = mutator.mutateChromosome(chromosome)
        result.mutated shouldBe chromosome
        result.mutations shouldBe 0
    }
}
