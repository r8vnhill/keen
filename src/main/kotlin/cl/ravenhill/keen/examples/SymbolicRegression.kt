package cl.ravenhill.keen.examples

import cl.ravenhill.keen.Builders.Chromosomes.program
import cl.ravenhill.keen.Builders.engine
import cl.ravenhill.keen.Builders.genotype
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.prog.Add
import cl.ravenhill.keen.prog.Mul
import cl.ravenhill.keen.prog.Reduceable
import cl.ravenhill.keen.prog.Sub
import cl.ravenhill.keen.prog.Value
import cl.ravenhill.keen.prog.Variable
import cl.ravenhill.keen.prog.op.MathOps
import cl.ravenhill.keen.util.Tree


private var ops = listOf(MathOps.Add)

private val terminals = listOf(
    Variable("x", 0),
    Value { Core.rng.nextDouble() * 10 })

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