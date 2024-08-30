package cl.ravenhill.keen.evolution.executors.construction

interface ConstructorExecutor<T> {
    suspend operator fun invoke(size: Int, init: suspend (index: Int) -> T): List<T>
}
