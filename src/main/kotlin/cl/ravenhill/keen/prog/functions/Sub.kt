//package cl.ravenhill.keen.prog.functions
//
//import cl.ravenhill.keen.prog.Reduceable
//
//
//class Sub : Reduceable<Double, Double> {
//    override val name: String = "sub"
//    override val arity: Int = 2
//    override val function: (Array<out Double>) -> Double = { args -> args[0] - args[1] }
//    lateinit var left: Reduceable<Double, Double>
//    lateinit var right: Reduceable<Double, Double>
//
//    override fun reduce(): Double {
//        return left.reduce() - right.reduce()
//    }
//
//    override fun toString() = "$left - $right"
//}