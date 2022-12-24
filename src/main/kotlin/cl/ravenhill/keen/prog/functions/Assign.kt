//package cl.ravenhill.keen.prog.functions
//
//import cl.ravenhill.keen.prog.Reduceable
//import cl.ravenhill.keen.prog.terminal.Variable
//
//
//class Assign<T : Any>(val child: Variable<T>, val value: T) :
//        Reduceable<T, T> {
//    override val name: String = "assign"
//    override val arity: Int = 1
//
//    override val function: (Array<out T>) -> T = {
//        value
//    }
//
//    override fun reduce() = value
//    override fun toString() = "$name($child, $value)"
//}
