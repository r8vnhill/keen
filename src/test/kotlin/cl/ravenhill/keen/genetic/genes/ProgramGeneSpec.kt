//package cl.ravenhill.keen.genetic.genes
//
//import cl.ravenhill.keen.Core
//import cl.ravenhill.enforcer.IntRequirementException
//import cl.ravenhill.enforcer.EnforcementException
//import cl.ravenhill.keen.prog.functions
//import cl.ravenhill.keen.prog.functions.Add
//import cl.ravenhill.keen.prog.functions.add
//import cl.ravenhill.keen.prog.terminals
//import cl.ravenhill.keen.prog.terminals.EphemeralConstant
//import cl.ravenhill.keen.shouldBeOfClass
//import cl.ravenhill.keen.util.program
//import io.kotest.assertions.throwables.shouldThrow
//import io.kotest.core.spec.style.FreeSpec
//import io.kotest.matchers.shouldBe
//import io.kotest.matchers.shouldNotBe
//import io.kotest.property.Arb
//import io.kotest.property.arbitrary.arbitrary
//import io.kotest.property.assume
//import io.kotest.property.checkAll
//
//class ProgramGeneSpec : FreeSpec({
//    afterAny {
//        Core.maxProgramDepth = Core.DEFAULT_MAX_PROGRAM_DEPTH
//    }
//    "Creating a program gene" - {
//        "have it's dna stored as a breadth first list" {
//            val add = Add()
//            val a = EphemeralConstant { 1.0 }.also { add[0] = it }
//            val b = Add().also { add[1] = it }
//            val c = EphemeralConstant { 2.0 }.also { b[0] = it }
//            val d = EphemeralConstant { 3.0 }.also { b[1] = it }
//            val gene =
//                ProgramGene(add, listOf(Add()), listOf(EphemeralConstant { 0.0 }))
//            gene.children shouldBe listOf(add, a, b, c, d)
//        }
//        "throw an exception if the program depth is greater than the max depth" {
//            Core.maxProgramDepth = 3
//            val add = add(
//                EphemeralConstant { 1.0 },
//                add(
//                    EphemeralConstant { 2.0 },
//                    add(
//                        EphemeralConstant { 3.0 },
//                        EphemeralConstant { 4.0 }
//                    )
//                )
//            )
//            shouldThrow<EnforcementException> {
//                ProgramGene(add, listOf(Add()), listOf(EphemeralConstant { 0.0 }))
//            }.violations.first() shouldBeOfClass IntRequirementException::class
//        }
//    }
//    "Getting the program's depth" - {
//        "return the depth of the program" {
//            checkAll(Arb.programGene(5)) {
//                it.depth shouldBe it.dna.depth
//            }
//        }
//        "return 0 if the program is a terminal" {
//            val gene = ProgramGene(
//                EphemeralConstant { 0.0 },
//                listOf(Add()),
//                listOf(EphemeralConstant { 0.0 })
//            )
//            gene.depth shouldBe 1
//        }
//    }
//    "Invoking as a function" - {
//        "reduce the program tree to a single value" {
//            val add = add(
//                EphemeralConstant { 1.0 },
//                add(EphemeralConstant { 2.0 },
//                    EphemeralConstant { 3.0 })
//            )
//            val gene =
//                ProgramGene(add, listOf(Add()), listOf(EphemeralConstant { 0.0 }))
//            gene() shouldBe 6.0
//        }
//    }
//    "Equality should" - {
//        "be true for the same instance" {
//            checkAll(Arb.programGene(5)) {
//                it shouldBe it
//            }
//        }
//        "be true for two genes with the same dna" {
//            checkAll(Arb.programGene(5)) { gene ->
//                val other = ProgramGene(
//                    gene.dna,
//                    gene.functions,
//                    gene.terminals
//                )
//                gene shouldBe other
//            }
//        }
//        "return false for two genes with different dna" {
//            checkAll(Arb.programGene(5), Arb.programGene(5)) { gene, other ->
//                assume(gene.dna != other.dna)
//                gene shouldNotBe other
//            }
//        }
//    }
//    "Hashcode should" - {
//        "be the same for the same instance" {
//            checkAll(Arb.programGene(5)) {
//                it.hashCode() shouldBe it.hashCode()
//            }
//        }
//        "be the same for two genes with the same dna" {
//            checkAll(Arb.programGene(5)) { gene ->
//                val other = ProgramGene(
//                    gene.dna,
//                    gene.functions,
//                    gene.terminals
//                )
//                gene.hashCode() shouldBe other.hashCode()
//            }
//        }
//        "return different for two genes with different dna" {
//            checkAll(Arb.programGene(5), Arb.programGene(5)) { gene, other ->
//                assume(gene.dna != other.dna)
//                gene.hashCode() shouldNotBe other.hashCode()
//            }
//        }
//    }
//})
//
//fun Arb.Companion.programGene(
//    maxDepth: Int,
//) = arbitrary { rs ->
//    val functions = Arb.functions().bind()
//    val terminals = Arb.terminals().bind()
//    val program = rs.random.program(maxDepth, functions, terminals)
//    ProgramGene(program, functions, terminals)
//}
