//package cl.ravenhill.keen.genetic.chromosomes
//
//import io.kotest.core.spec.style.WordSpec
//import io.kotest.matchers.shouldBe
//import io.kotest.matchers.shouldNot
//import io.kotest.matchers.shouldNotBe
//import io.kotest.matchers.types.haveSameHashCodeAs
//import io.kotest.matchers.types.shouldHaveSameHashCodeAs
//import io.kotest.property.Arb
//import io.kotest.property.arbitrary.arbitrary
//import io.kotest.property.arbitrary.nonNegativeInt
//import io.kotest.property.arbitrary.positiveInt
//import io.kotest.property.assume
//import io.kotest.property.checkAll
//
//fun Arb.Companion.charChromosome() = arbitrary {
//    val size = Arb.positiveInt(10_000).bind()
//    CharChromosome.Factory().apply {
//        this.size = size
//    }.make()
//}
//
//class CharChromosomeSpec : WordSpec({
//    "Chromosome factory" When {
//        "Creating a chromosome with a given size" should {
//            "Return a chromosome with the given size" {
//                checkAll(Arb.nonNegativeInt(1000)) { size ->
//                    val chromosome = CharChromosome.Factory().apply {
//                        this.size = size
//                    }.make()
//                    chromosome.size shouldBe size
//                }
//            }
//        }
//    }
//
//    "Verifying" should {
//        "Return true if the chromosome is valid" {
//            checkAll(Arb.charChromosome()) { chromosome ->
//                chromosome.verify() shouldBe true
//            }
//        }
//    }
//
//    "Duplicating" should {
//        "Return a new chromosome with the same genes" {
//            checkAll(Arb.charChromosome()) { chromosome ->
//                val duplicated = chromosome.withGenes(chromosome.genes)
//                duplicated.size shouldBe chromosome.size
//                duplicated.genes shouldBe chromosome.genes
//            }
//        }
//    }
//
//    "Object identity" When {
//        "equality" should {
//            "return true if the chromosomes are the same instance" {
//                checkAll(Arb.charChromosome()) { chromosome ->
//                    chromosome shouldBe chromosome
//                }
//            }
//
//            "return true if the chromosomes have the same genes" {
//                checkAll(Arb.charChromosome()) { chromosome ->
//                    val other = chromosome.withGenes(chromosome.genes)
//                    chromosome shouldBe other
//                }
//            }
//
//            "return false if the chromosomes have different genes" {
//                checkAll(
//                    Arb.charChromosome(),
//                    Arb.charChromosome()
//                ) { chromosome1, chromosome2 ->
//                    assume(chromosome1.genes != chromosome2.genes)
//                    chromosome1 shouldNotBe chromosome2
//                }
//            }
//        }
//
//        "hashing" should {
//            "return the same hash code if the chromosomes are the same instance" {
//                checkAll(Arb.charChromosome()) { chromosome ->
//                    chromosome shouldHaveSameHashCodeAs chromosome
//                }
//            }
//
//            "return the same hash code if the chromosomes have the same genes" {
//                checkAll(Arb.charChromosome()) { chromosome ->
//                    val other = chromosome.withGenes(chromosome.genes)
//                    chromosome shouldHaveSameHashCodeAs other
//                }
//            }
//
//            "return a different hash code if the chromosomes have different genes" {
//                checkAll(
//                    Arb.charChromosome(),
//                    Arb.charChromosome()
//                ) { chromosome1, chromosome2 ->
//                    assume(chromosome1.genes != chromosome2.genes)
//                    chromosome1 shouldNot haveSameHashCodeAs(chromosome2)
//                }
//            }
//        }
//    }
//})