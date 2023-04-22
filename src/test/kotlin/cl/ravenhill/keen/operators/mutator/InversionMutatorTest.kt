//package cl.ravenhill.keen.operators.mutator
//
//import cl.ravenhill.keen.Core
//import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
//import cl.ravenhill.keen.genetic.chromosomes.numerical.intChromosome
//import io.kotest.core.spec.style.FreeSpec
//import io.kotest.datatest.withData
//import io.kotest.matchers.shouldBe
//import io.kotest.property.Arb
//import io.kotest.property.assume
//import io.kotest.property.checkAll
//import kotlin.random.Random
//
//data class InversionMutatorTestData(
//    val probability: Double,
//    val seed: Long,
//    val expectedMutations: Int,
//    val expectedGenes: List<Int>
//)
//
//class InversionMutatorTest : FreeSpec({
//    afterAny {
//        Core.random = Random.Default
//        Core.Dice.random = Random.Default
//    }
//    "Mutating a CHROMOSOME of Ints should" - {
//        "return the same chromosome if the probability is 0" {
//            `mutating a chromosome with probability 0 returns the same chromosome`(
//                InversionMutator(0.0),
//                Arb.intChromosome()
//            )
//        }
//        "return a chromosome with all genes inverted if the probability is 1" {
//            checkAll(Arb.intChromosome()) { chromosome ->
//                assume(chromosome.size > 1)
//                val mutator = InversionMutator<Int>(1.0)
//                val (mutated, mutations) = mutator.mutateChromosome(chromosome)
//                mutations shouldBe 1
//                mutated.genes.map { it.dna } shouldBe chromosome.genes.map { it.dna }
//                    .reversed()
//            }
//        }
//        "return a chromosome mutated according to probability" - {
//            withData(
//                nameFn = { "probability=${it.probability}, seed=${it.seed}" },
//                listOf(
//                    // [4, 8, 7, 7*, 2*] -> [4, 8, 7, 2, 7]
//                    InversionMutatorTestData(0.5, 0, 1, listOf(4, 8, 7, 2, 7)),
//                    // [1*, 6, 3*, 8, 9] -> [3, 6, 1, 8, 9]
//                    InversionMutatorTestData(0.7, 11, 1, listOf(3, 6, 1, 8, 9)),
//                )
//            ) { (probability, seed, expectedMutations, expectedGenes) ->
//                Core.random = Random(seed)
//                Core.Dice.random = Random(seed)
//                val mutator = InversionMutator<Int>(probability)
//                val chromosome = IntChromosome.Factory().apply {
//                    size = 5
//                    range = 0 to 10
//                }.make()
//                val (mutated, mutations) = mutator.mutateChromosome(chromosome)
//                mutations shouldBe expectedMutations
//                mutated.genes.map { it.dna } shouldBe expectedGenes
//            }
//        }
//    }
//})
