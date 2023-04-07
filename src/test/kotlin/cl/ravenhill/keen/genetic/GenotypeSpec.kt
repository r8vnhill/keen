//package cl.ravenhill.keen.genetic
//
//import cl.ravenhill.keen.*
//import io.kotest.assertions.throwables.shouldThrow
//import io.kotest.core.spec.style.WordSpec
//import io.kotest.matchers.shouldBe
//import io.kotest.property.Arb
//import io.kotest.property.arbitrary.list
//import io.kotest.property.checkAll
//
//
//class GenotypeSpec : WordSpec({
//    "Genotype Factory" When {
//        "adding a chromosome factory" should {
//            "initialize a new list if it has not been initialized" {
//                checkAll(Arb.intChromosomeFactory()) { arbFactory ->
//                    val factory = Genotype.Factory<Int>()
//                    shouldThrow<UninitializedPropertyAccessException> {
//                        factory.chromosomes
//                    }
//                    factory.chromosome { arbFactory }
//                    factory.chromosomes.size shouldBe 1
//                }
//            }
//            "add a new chromosome factory to the list" {
//                checkAll(Arb.list(Arb.intChromosomeFactory(), 1..100)) { arbFactories ->
//                    val factory = Genotype.Factory<Int>()
//                    arbFactories.forEach { factory.chromosome { it } }
//                    factory.chromosomes.size shouldBe arbFactories.size
//                }
//            }
//        }
//        "making a genotype" should {
//            "return a genotype with the given chromosome factories" {
//                checkAll(Arb.list(Arb.intChromosomeFactory(), 1..100)) { arbFactories ->
//                    val factory = Genotype.Factory<Int>()
//                    arbFactories.forEach { factory.chromosome { it } }
//                    val genotype = factory.make()
//                    genotype.chromosomes.size shouldBe arbFactories.size
//                }
//            }
//            "throw an exception if  the chromosome factories are not initialized" {
//                val factory = Genotype.Factory<Int>()
//                shouldThrow<EnforcementException> {
//                    factory.make()
//                }.violations.first() shouldBeOfClass UnfulfilledRequirementException::class
//            }
//        }
//    }
//    "Accessing by index" should {
//        "return the chromosome at the given index" {
//            checkAll(Arb.genotype(Arb.intChromosomeFactory())) { genotype ->
//                genotype.chromosomes.forEachIndexed { index, chromosome ->
//                    genotype[index] shouldBe chromosome
//                }
//            }
//        }
//        "throw an exception if the index is out of bounds" {
//            checkAll(
//                Arb.genotype(Arb.intChromosomeFactory()),
//                Arb.intOutsideRange(0..100)
//            ) { genotype, index ->
//                shouldThrow<EnforcementException> {
//                    genotype[index]
//                }.violations.first() shouldBeOfClass IntRequirementException::class
//            }
//        }
//    }
//    "Convert to sequence" should {
//        "return a sequence with the chromosomes of the genotype" {
//            checkAll(Arb.genotype(Arb.intChromosomeFactory())) { genotype ->
//                genotype.asSequence().forEachIndexed { index, chromosome ->
//                    genotype.chromosomes[index] shouldBe chromosome
//                }
//            }
//        }
//    }
//    "Duplicating" should {
//        "return a new genotype with the given chromosomes" {
//            checkAll(
//                Arb.genotype(Arb.intChromosomeFactory()),
//                Arb.genotype(Arb.intChromosomeFactory())
//            ) { genotype1, genotype2 ->
//                val new = genotype1.duplicate(genotype2.chromosomes)
//                new.chromosomes.size shouldBe genotype2.chromosomes.size
//                (new.chromosomes zip genotype2.chromosomes).forEach { (c1, c2) ->
//                    c1 shouldBe c2
//                }
//            }
//        }
//    }
//    "Flattening" should {
//        "return a flat list of the underlying genes" {
//            checkAll(Arb.list(Arb.intChromosomeFactory(), 1..100)) { arbFactories ->
//                val factory = Genotype.Factory<Int>()
//                arbFactories.forEach { factory.chromosome { it } }
//                val genotype = factory.make()
//                genotype.flatten().size shouldBe genotype.chromosomes.sumOf { it.size }
//                genotype.flatten() shouldBe genotype.chromosomes.flatMap { it.flatten() }
//            }
//        }
//    }
//    "Size" should {
//        "be equal to the number of chromosomes on the genotype" {
//            checkAll(Arb.genotype(Arb.intChromosomeFactory())) { genotype ->
//                genotype.size shouldBe genotype.chromosomes.size
//            }
//        }
//    }
//    "Verifying" should {
//        "return true if the genotype is valid" {
//            checkAll(
//                Arb.genotype(Arb.intChromosomeFactory())
//            ) { genotype ->
//                genotype.verify() shouldBe true
//            }
//        }
//    }
//})
