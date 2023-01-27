package cl.ravenhill.keen.operators.mutator

import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.checkAll

suspend fun <T> `mutating a chromosome with probability 0 returns the same chromosome`(
    mutator: Mutator<T>,
    chromosomeArb: Arb<Chromosome<T>>,
) {
    checkAll(chromosomeArb) { chromosome ->
        val (mutated, mutations) = mutator.mutateChromosome(chromosome)
        mutations shouldBe 0
        mutated shouldBe chromosome
    }
}

suspend fun <T> `mutating a gene with probability 0 returns the same gene`(
    mutator: Mutator<T>,
    arbGene: Arb<Gene<T>>
) {
    checkAll(arbGene) { gene ->
        val (mutated, mutations) = mutator.mutateGene(gene)
        mutations shouldBe 0
        mutated shouldBe gene
    }
}
