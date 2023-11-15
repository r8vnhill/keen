package cl.ravenhill.keen.operators.crossover.pointbased

import cl.ravenhill.keen.arbs.datatypes.probability
import cl.ravenhill.keen.genetic.genes.ProgramGene
import cl.ravenhill.keen.prog.Program
import cl.ravenhill.keen.prog.Reduceable
import cl.ravenhill.keen.prog.terminals.Constant
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll

class SubtreeCrossoverTest : FreeSpec({

    "A Subtree Crossover operator" - {
        val a = Constant(1)
        val b = Constant(2)
        val c = Constant(3)
        val d = Constant(4)
        val e = Constant(5)

        "when replacing subtrees" - {
            "should replace the subtrees of the parents with the subtrees of the children" {}

            "should return the replacement if the parent is a single node" {}

            "should return the original parent if the replacement results in exceeding the maximum depth" {}

            "should throw an exception when" - {
                "the original node is not part of the parent" {}

                "the source node is empty" {}
            }
        }
    }
})
