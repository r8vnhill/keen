package cl.ravenhill.keen.prog


class Fun<T, R>(private val name: String, private val function: (List<T>) -> R) : Reduce<R> {
    var inputs: Array<Reduce<T>> = arrayOf()

    override fun reduce() = function(inputs.map { it.reduce() })

    override fun toString() = "$name(${inputs.joinToString(", ")})"
}

fun main() {
    val fun1 = Fun<Int, Int>("fun1") { it[0] + it[1] }
    val inputs = arrayOf(Value(1), Variable("a", 2))
    fun1.inputs = inputs
    println(fun1)
    println(fun1.reduce())
}