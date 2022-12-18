package cl.ravenhill.keen.prog


open class Fun<T, R>(
    private val name: String,
    private val function: (List<T>) -> R
) : Reduceable<R> {
    var inputs: Array<Reduceable<T>> = arrayOf()

    override fun reduce() = function(inputs.map { it.reduce() })

    override fun toString() = "$name(${inputs.joinToString(", ")})"
}
