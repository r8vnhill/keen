package cl.ravenhill.keen.examples

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.prog.Add
import cl.ravenhill.keen.prog.Mul
import cl.ravenhill.keen.prog.Sub
import cl.ravenhill.keen.prog.Value
import cl.ravenhill.keen.prog.Variable


private val operations = listOf(Add(), Sub(), Mul())

private val terminals = listOf(
    Variable("x", 0),
    Value { Core.rng.nextDouble() * 10 })