package cl.ravenhill.keen.operators.mutator

import cl.ravenhill.keen.genetic.chromosomes.Chromosome
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
