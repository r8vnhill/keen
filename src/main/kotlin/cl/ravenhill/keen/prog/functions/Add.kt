package cl.ravenhill.keen.prog.functions

import cl.ravenhill.keen.prog.Reduceable

class Add : Fun<Double> {

    override val arity: Int = 2

    lateinit var left: Reduceable<Double>
    lateinit var right: Reduceable<Double>

    override fun invoke(args: Array<out Double>) = left(args) + right(args)
}
