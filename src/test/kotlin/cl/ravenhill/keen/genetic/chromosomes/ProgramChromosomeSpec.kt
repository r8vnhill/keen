//package cl.ravenhill.keen.genetic.chromosomes
//
//import cl.ravenhill.enforcer.IntRequirementException
//import cl.ravenhill.enforcer.EnforcementException
//import cl.ravenhill.keen.genetic.genes.ProgramGene
//import cl.ravenhill.keen.prog.functions
//import cl.ravenhill.keen.prog.functions.addition
//import cl.ravenhill.keen.prog.functions.greaterThan
//import cl.ravenhill.keen.prog.functions.ifThenElse
//import cl.ravenhill.keen.prog.functions.multiplication
//import cl.ravenhill.keen.prog.terminals
//import cl.ravenhill.keen.prog.terminals.ephemeralConstant
//import cl.ravenhill.keen.prog.terminals.variable
//import cl.ravenhill.keen.shouldBeOfClass
//import io.kotest.assertions.throwables.shouldThrow
//import io.kotest.core.spec.style.FreeSpec
//import io.kotest.matchers.shouldBe
//import io.kotest.matchers.shouldNotBe
//import io.kotest.property.Arb
//import io.kotest.property.arbitrary.*
//import io.kotest.property.assume
//import io.kotest.property.checkAll
//
//
//class ProgramChromosomeSpec : FreeSpec({
//    "Given a Chromosome Factory when" - {
//        "setting the Size should" - {
//            "be set if Positive" {
//                checkAll(Arb.positiveInt()) { size ->
//                    val chromosome = ProgramChromosome.Factory<Double>().apply {
//                        this.size = size
//                    }
//                    chromosome.size shouldBe size
//                }
//            }
//            "throw an Exception if Non-Positive" {
//                checkAll(Arb.nonPositiveInt()) { size ->
//                    shouldThrow<EnforcementException> {
//                        ProgramChromosome.Factory<Double>().apply {
//                            this.size = size
//                        }
//                    }.violations.first() shouldBeOfClass IntRequirementException::class
//                }
//            }
//        }
//        "a Function should be able to be Added" {
//            checkAll(
//                Arb.list(
//                    Arb.choice(
//                        Arb.addition(),
//                        Arb.greaterThan(),
//                        Arb.multiplication(),
//                        Arb.ifThenElse()
//                    )
//                )
//            ) { functions ->
//                val factory = ProgramChromosome.Factory<Double>()
//                factory.functions.size shouldBe 0
//                for (f in functions) {
//                    factory.function { f }
//                }
//                factory.functions.size shouldBe functions.distinct().size
//            }
//        }
//        "a Terminal should be able to be added" {
//            checkAll(
//                Arb.list(
//                    Arb.choice(
//                        Arb.ephemeralConstant(),
//                        Arb.variable()
//                    )
//                )
//            ) { terminals ->
//                val factory = ProgramChromosome.Factory<Double>()
//                factory.terminals.size shouldBe 0
//                for (t in terminals) {
//                    factory.terminal { t }
//                }
//                factory.terminals.size shouldBe terminals.distinct().size
//            }
//        }
//        "throw an exception if no functions nor terminals are added" {
//            shouldThrow<EnforcementException> {
//                ProgramChromosome.Factory<Double>().make()
//            }.violations.first() shouldBeOfClass IntRequirementException::class
//        }
//    }
//    "Duplicating a new one with the given genes" {
//        checkAll(Arb.programChromosome(), Arb.programChromosome()) { duplicator, source ->
//            val duplicated = duplicator.duplicate(source.genes)
//            duplicated.genes.size shouldBe source.genes.size
//            (duplicated.genes zip source.genes).forEach { (d, s) ->
//                d shouldBe s
//            }
//        }
//    }
//    "Equality should" - {
//        "be true for the same instance" {
//            checkAll(Arb.programChromosome()) { chromosome ->
//                chromosome shouldBe chromosome
//            }
//        }
//        "be true for two instances with the same genes" {
//            checkAll(Arb.programChromosome()) { chromosome ->
//                val duplicated = chromosome.duplicate(chromosome.genes)
//                chromosome shouldBe duplicated
//            }
//        }
//        "be false for two instances with different genes" {
//            checkAll(Arb.programChromosome(), Arb.programChromosome()) { a, b ->
//                assume(a.genes != b.genes)
//                a shouldNotBe b
//            }
//        }
//    }
//    "Hashing should" - {
//        "be the same for the same instance" {
//            checkAll(Arb.programChromosome()) { chromosome ->
//                chromosome.hashCode() shouldBe chromosome.hashCode()
//            }
//        }
//        "be the same for two instances with the same genes" {
//            checkAll(Arb.programChromosome()) { chromosome ->
//                val duplicated = chromosome.duplicate(chromosome.genes)
//                chromosome.hashCode() shouldBe duplicated.hashCode()
//            }
//        }
//        "be different for two instances with different genes" {
//            checkAll(Arb.programChromosome(), Arb.programChromosome()) { a, b ->
//                assume(a.genes != b.genes)
//                a.hashCode() shouldNotBe b.hashCode()
//            }
//        }
//    }
//    "Verifying a chromosome should return" - {
//        "True if it is valid according to the validator" {
//            checkAll(Arb.programChromosome()) { chromosome ->
//                chromosome.verify() shouldBe true
//            }
//        }
//        "False if it is not valid according to the validator" {
//            checkAll(Arb.programChromosome { false }) { chromosome ->
//                chromosome.verify() shouldBe false
//            }
//        }
//    }
//})
//
///**
// * Creates an [Arb]itrary [ProgramChromosome] with the given [validator].
// */
//fun Arb.Companion.programChromosome(
//    validator: (ProgramGene<Double>) -> Boolean = { true }
//) = arbitrary {
//    val size = Arb.positiveInt(100).bind()
//    val functions = Arb.functions().bind()
//    val terminals = Arb.terminals().bind()
//    ProgramChromosome.Factory<Double>().apply {
//        this.size = size
//        for (f in functions) {
//            this.function { f }
//        }
//        for (t in terminals) {
//            this.terminal { t }
//        }
//        this.validator = validator
//    }.make()
//}