package cl.ravenhill.keen.prog


data class Variable<T>(val name: String, var value: T) : Reduceable<T> {
    override fun reduce() = value
    override fun toString() = "$name = $value"
}