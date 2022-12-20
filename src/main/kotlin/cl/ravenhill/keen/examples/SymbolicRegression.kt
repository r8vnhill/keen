package cl.ravenhill.keen.examples

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.genes.prog.Program
import cl.ravenhill.keen.prog.Add
import cl.ravenhill.keen.prog.Fun
import cl.ravenhill.keen.prog.Mul
import cl.ravenhill.keen.prog.Reduceable
import cl.ravenhill.keen.prog.Sub
import cl.ravenhill.keen.prog.Value
import cl.ravenhill.keen.prog.Variable
import cl.ravenhill.keen.util.Tree


private val operations = listOf(Add(), Sub(), Mul())

private val terminals = listOf(
    Variable("x", 0),
    Value { Core.rng.nextDouble() * 10 })

private fun fitness(genotype: Genotype<Tree<Reduceable<Double>>>) : Double {
    TODO()
}

fun main() {

}