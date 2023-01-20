package cl.ravenhill.keen.operators.mutator

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
import cl.ravenhill.keen.genetic.chromosomes.numerical.intChromosome
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import kotlin.random.Random

class SwapMutatorSpec : WordSpec({
    afterAny {
        Core.random = Random.Default
        Core.Dice.random = Random.Default
    }
    "Mutating a CHROMOSOME" When {
        "it is an Int chromosome" should {
            "return the same chromosome if the probability is 0" {
                `mutating a chromosome with probability 0 returns the same chromosome`(
                    mutator = SwapMutator(0.0),
                    chromosomeArb = Arb.intChromosome()
                )
            }
            "return a chromosome with all genes swapped if the probability is 1 with seed 0" {
                Core.random = Random(0)
                Core.Dice.random = Random(0)
                val mutator = SwapMutator<Int>(1.0)
                val chromosome = IntChromosome.Factory().apply {
                    size = 5
                    range = 0 to 10
                }.make()
                // Swaps: [0 -> 4, 1 - > 3, 2 -> 2, 3 -> 2, 4 -> 2]
                // [4*, 8, 7, 7, 2*] -> [2, 8*, 7, 7*, 4] -> [2, 7, 7**, 8, 4]
                // -> [2, 7, 7*, 8*, 4] -> [2, 7, 8*, 7, 4*] -> [2, 7, 4, 7, 8]
                val (mutated, mutations) = mutator.mutateChromosome(chromosome)
                mutations shouldBe 5
                mutated.genes.map { it.dna } shouldBe listOf(2, 7, 4, 7, 8)
            }
            "return a chromosome mutated according to probability with seed 0" {
                Core.random = Random(1)
                Core.Dice.random = Random(1)
                val mutator = SwapMutator<Int>(0.5)
                val chromosome = IntChromosome.Factory().apply {
                    size = 5
                    range = 0 to 10
                }.make()
                // Swaps: [0 -> 0, 4-> 1]
                // [5**, 6, 2, 8, 9] -> [5, 9*, 2, 8, 6*] -> [5, 9, 2, 8, 6]
                val (mutated, mutations) = mutator.mutateChromosome(chromosome)
                mutations shouldBe 2
                mutated.genes.map { it.dna } shouldBe listOf(5, 9, 2, 8, 6)
            }
        }
    }
})
