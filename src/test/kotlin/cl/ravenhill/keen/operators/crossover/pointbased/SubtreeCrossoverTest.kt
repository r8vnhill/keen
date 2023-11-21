package cl.ravenhill.keen.operators.crossover.pointbased

import cl.ravenhill.keen.arbs.datatypes.probability
import cl.ravenhill.keen.genetic.genes.ProgramGene
import cl.ravenhill.keen.prog.Program
import cl.ravenhill.keen.prog.Reducible
import cl.ravenhill.keen.prog.functions.Fun
import cl.ravenhill.keen.prog.terminals.Constant
import io.kotest.assertions.fail
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.checkAll

class SubtreeCrossoverTest : FreeSpec({

    "A Subtree Crossover operator" - {
        val constantA = Constant(1.0)
        val constantB = Constant(2.0)
        val constantC = Constant(3.0)
        val constantD = Constant(4.0)
        val constantE = Constant(5.0)
        val functionF = Fun<Double>("+", 2) { args -> args[0] + args[1] }
        val functionG = Fun<Double>("*", 2) { args -> args[0] * args[1] }
        val functionH = Fun<Double>("-", 2) { args -> args[0] - args[1] }
        val functions = listOf(functionF, functionG, functionH)
        val terminals = listOf(constantA, constantB, constantC, constantD, constantE)

        val programA = Program(constantA)
        val programB = Program(constantB)
        val programC = Program(constantC)
        val programD = Program(constantD)
        val programE = Program(constantE)
        val programF = Program(functionF, listOf(programA, programB))
        val programG = Program(functionG, listOf(programC, programD))
        val programH = Program(functionH, listOf(programE, programF))

        val geneA = ProgramGene(programA, functions, terminals)
        val geneB = ProgramGene(programB, functions, terminals)
        val geneC = ProgramGene(programC, functions, terminals)
        val geneD = ProgramGene(programD, functions, terminals)
        val geneE = ProgramGene(programE, functions, terminals)
        val geneF = ProgramGene(programF, functions, terminals)
        val geneG = ProgramGene(programG, functions, terminals)
        val geneH = ProgramGene(programH, functions, terminals)

        "should have an exclusivity property that" - {
            "defaults to false" {
                checkAll(Arb.probability(), Arb.probability()) { chromosomeRate, geneRate ->
                    val crossover = SubtreeCrossover<Reducible<Double>, Program<Double>, ProgramGene<Double>>(
                        chromosomeRate = chromosomeRate,
                        geneRate = geneRate
                    )
                    crossover.exclusivity.shouldBeFalse()
                }
            }

            "returns the value provided in the constructor" {
                checkAll(Arb.boolean(), Arb.probability(), Arb.probability()) { exclusivity, chromosomeRate, geneRate ->
                    val crossover = SubtreeCrossover<Reducible<Double>, Program<Double>, ProgramGene<Double>>(
                        chromosomeRate = chromosomeRate,
                        geneRate = geneRate,
                        exclusivity = exclusivity
                    )
                    crossover.exclusivity shouldBe exclusivity
                }
            }
        }

        "should have a chromosome rate property that" - {
            "defaults to 1.0" {
                checkAll(Arb.boolean(), Arb.probability()) { exclusivity, geneRate ->
                    val crossover = SubtreeCrossover<Reducible<Double>, Program<Double>, ProgramGene<Double>>(
                        geneRate = geneRate,
                        exclusivity = exclusivity
                    )
                    crossover.chromosomeRate shouldBe 1.0
                }
            }

            "returns the value provided in the constructor" {
                checkAll(Arb.boolean(), Arb.probability(), Arb.probability()) { exclusivity, chromosomeRate, geneRate ->
                    val crossover = SubtreeCrossover<Reducible<Double>, Program<Double>, ProgramGene<Double>>(
                        chromosomeRate = chromosomeRate,
                        geneRate = geneRate,
                        exclusivity = exclusivity
                    )
                    crossover.chromosomeRate shouldBe chromosomeRate
                }
            }
        }

        "should have a gene rate property that" - {
            "defaults to 1.0" {
                checkAll(Arb.boolean(), Arb.probability()) { exclusivity, chromosomeRate ->
                    val crossover = SubtreeCrossover<Reducible<Double>, Program<Double>, ProgramGene<Double>>(
                        chromosomeRate = chromosomeRate,
                        exclusivity = exclusivity
                    )
                    crossover.geneRate shouldBe 1.0
                }
            }

            "returns the value provided in the constructor" {
                checkAll(Arb.boolean(), Arb.probability(), Arb.probability()) { exclusivity, chromosomeRate, geneRate ->
                    val crossover = SubtreeCrossover<Reducible<Double>, Program<Double>, ProgramGene<Double>>(
                        chromosomeRate = chromosomeRate,
                        geneRate = geneRate,
                        exclusivity = exclusivity
                    )
                    crossover.geneRate shouldBe geneRate
                }
            }
        }

        "when replacing subtrees" - {
            "should return the replacement if the parent is a single node" {
                val crossover = SubtreeCrossover<Reducible<Double>, Program<Double>, ProgramGene<Double>>()
                val result = crossover.replaceSubtreeAndCheckHeight(geneA, programA, programG)
                result shouldBe programG
            }

            "should replace the subtrees of the parents with the subtrees of the children" {
                fail("Not implemented")
            }

            "should return the original parent if the replacement results in exceeding the maximum depth" {
                fail("Not implemented")
            }

            "should throw an exception when" - {
                "the original node is not part of the parent" {
                    fail("Not implemented")
                }

                "the source node is empty" {
                    fail("Not implemented")
                }
            }
        }
    }
})
