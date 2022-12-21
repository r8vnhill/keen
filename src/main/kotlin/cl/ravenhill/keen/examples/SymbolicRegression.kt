package cl.ravenhill.keen.examples

import cl.ravenhill.keen.Builders.Chromosomes.program
import cl.ravenhill.keen.Builders.engine
import cl.ravenhill.keen.Builders.genotype
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.prog.op.Add
import cl.ravenhill.keen.prog.op.Mul
import cl.ravenhill.keen.prog.op.Sub
import cl.ravenhill.keen.prog.op.Value
import cl.ravenhill.keen.prog.op.Variable
import cl.ravenhill.keen.util.Tree


private var ops = listOf(Add, Sub, Mul)

private val terminals = listOf(
    Variable("x", 0),
    Value.EphemeralConstant { Math.random() * 10 })

private fun fitness(genotype: Genotype<Tree<Reduceable<Double>>>): Double {
    TODO()
}

fun main() {
    val engine = engine(::fitness, genotype {
        chromosome {
            program {
                operations = ops
            }
        }
    }) {}
}