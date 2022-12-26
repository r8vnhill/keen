package cl.ravenhill.keen.prog.functions

import cl.ravenhill.keen.prog.Reduceable
import cl.ravenhill.keen.prog.terminals.EphemeralConstant
import cl.ravenhill.keen.prog.terminals.Variable

fun ifTrue(condition: Reduceable<Double>, left: Reduceable<Double>, right: Reduceable<Double>) =
    If().apply {
        set(0, condition)
        set(1, left)
        set(2, right)
    }

class If : AbstractFun<Double>() {
    override val arity = 3

    // The children are initialized with ephemeral constants to avoid nullability
    // issues. The initial values are set to 0.0 since that way the operation will
    // always return 0 (false).
    override val _children =
        mutableListOf<Reduceable<Double>>(
            EphemeralConstant { 0.0 },
            EphemeralConstant { 0.0 },
            EphemeralConstant { 0.0 })

    override fun copy() = If()

    override fun invoke(args: Array<out Double>): Double {
        val condition = _children[0](args)
        val left = _children[1](args)
        val right = _children[2](args)
        return if (condition > 0.0) left else right
    }

    override fun toString() =
        "(if (${_children[0]}) then ${_children[1]} else ${_children[2]})"
}

fun main() {
    val a = add(
        EphemeralConstant { 1.0 },
        add(EphemeralConstant { 2.0 },
            ifTrue(
                greaterThan(
                    Variable("x", 0),
                    EphemeralConstant { 10.0 }),
                EphemeralConstant { 3.0 },
                EphemeralConstant { 4.0 })))
    println(a)
    println(a(arrayOf(1.0)))
}