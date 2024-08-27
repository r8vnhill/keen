/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.executors.construction

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.keen.exceptions.InvalidSizeException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

class CoroutineConcurrentConstructor<T>(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) : ConstructorExecutor<T> {

    override suspend operator fun invoke(size: Int, init: (index: Int) -> T): List<T> {
        constraints {
            "Cannot create a sequence with a negative size."(::InvalidSizeException) {
                size must BePositive
            }
        }

        return (0 until size).map { index ->
            scope.async {
                init(index)
            }
        }.awaitAll()
    }
}
