//package cl.ravenhill.keen.prog.functions
//
//import cl.ravenhill.keen.prog.Reduceable
//import cl.ravenhill.keen.prog.terminals.EphemeralConstant
//import java.util.*
//
//fun greaterThan(left: Reduceable<Double>, right: Reduceable<Double>) =
//    GreaterThan().apply {
//        set(0, left)
//        set(1, right)
//    }
//
//class GreaterThan : AbstractFun<Double>() {
//    override val arity = 2
//
//    // The children are initialized with ephemeral constants to avoid nullability
//    // issues. The initial values are set to 0.0 since that way the operation will
//    // always return false.
//    override val _children =
//        mutableListOf<Reduceable<Double>>(EphemeralConstant { 0.0 },
//            EphemeralConstant { 0.0 })
//
//    override fun copy() = GreaterThan()
//
//    override fun invoke(args: Array<out Double>): Double {
//        val left = _children[0](args)
//        val right = _children[1](args)
//        return if (left > right) 1.0 else 0.0
//    }
//
//    // region : equals, hashCode, toString
//    override fun equals(other: Any?) = when {
//        this === other -> true
//        other !is GreaterThan -> false
//        else -> _children == other._children
//    }
//
//    override fun hashCode() = Objects.hash(GreaterThan::class, _children)
//
//    override fun toString() = "(${_children[0]} > ${_children[1]})"
//    // endregion
//}