package cl.ravenhill.keen.evolution.executors.construction

interface ConstructorExecutor<T> {
    fun invoke(size: Int, init: (index: Int) -> T): List<T>
}
