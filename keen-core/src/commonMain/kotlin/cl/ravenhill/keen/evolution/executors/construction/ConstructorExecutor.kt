package cl.ravenhill.keen.evolution.executors.construction

interface ConstructorExecutor<T> {
    suspend fun invoke(size: Int, init: (index: Int) -> T): List<T>
}
