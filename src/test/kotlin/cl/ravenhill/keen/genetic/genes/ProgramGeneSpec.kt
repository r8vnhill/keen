package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.InvalidStateException
import cl.ravenhill.keen.prog.functions.*
import cl.ravenhill.keen.prog.terminals.EphemeralConstant
import cl.ravenhill.keen.prog.terminals.Variable
import cl.ravenhill.keen.util.nextChar
import cl.ravenhill.keen.util.program
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.checkAll

class ProgramGeneSpec : WordSpec({
    "Creating a program gene" should {
        afterAny {
            Core.maxProgramDepth = Core.DEFAULT_MAX_PROGRAM_DEPTH
        }
        "have it's dna stored as a breadth first list" {
            val add = Add()
            val a = EphemeralConstant { 1.0 }.also { add[0] = it }
            val b = Add().also { add[1] = it }
            val c = EphemeralConstant { 2.0 }.also { b[0] = it }
            val d = EphemeralConstant { 3.0 }.also { b[1] = it }
            val gene =
                ProgramGene(add, listOf(Add()), listOf(EphemeralConstant { 0.0 }))
            gene.children shouldBe listOf(add, a, b, c, d)
        }
        "throw an exception if the program depth is greater than the max depth" {
            Core.maxProgramDepth = 3
            val add = add(
                EphemeralConstant { 1.0 },
                add(
                    EphemeralConstant { 2.0 },
                    add(
                        EphemeralConstant { 3.0 },
                        EphemeralConstant { 4.0 }
                    )
                )
            )
            shouldThrow<InvalidStateException> {
                ProgramGene(add, listOf(Add()), listOf(EphemeralConstant { 0.0 }))
            }
        }
    }
    "Genetic operations" When {
        "duplicating" should {
            checkAll(Arb.programGene(5)) { gene ->
                val dna = gene.dna.deepCopy()
                println(dna)
            }
        }
    }
    "Getting the program's depth" should {
        "return the depth of the program" {
            val add = add(
                EphemeralConstant { 1.0 },
                add(
                    EphemeralConstant { 2.0 },
                    add(
                        EphemeralConstant { 3.0 },
                        EphemeralConstant { 4.0 }
                    )
                )
            )
            val gene =
                ProgramGene(add, listOf(Add()), listOf(EphemeralConstant { 0.0 }))
            gene.depth shouldBe 4
        }
        "return 0 if the program is a terminal" {
            val gene = ProgramGene(
                EphemeralConstant { 0.0 },
                listOf(Add()),
                listOf(EphemeralConstant { 0.0 })
            )
            gene.depth shouldBe 1
        }
    }
    "Invoking as a function" should {
        "reduce the program tree to a single value" {
            val add = add(
                EphemeralConstant { 1.0 },
                add(EphemeralConstant { 2.0 },
                    EphemeralConstant { 3.0 })
            )
            val gene =
                ProgramGene(add, listOf(Add()), listOf(EphemeralConstant { 0.0 }))
            gene() shouldBe 6.0
        }
    }
})

private fun Arb.Companion.programGene(
    maxDepth: Int,
) = arbitrary { rs ->
    val functions = listOf(Add(), GreaterThan(), If())
    val terminals = listOf(
        EphemeralConstant { rs.random.nextDouble() },
        Variable(rs.random.nextChar().toString(), 0)
    )
    val program = rs.random.program(maxDepth, functions, terminals)
    ProgramGene(program, functions, terminals)
}
