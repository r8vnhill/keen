/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.evolution.executors.construction

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class CoroutineConcurrentConstructorTest : FreeSpec({
    "A CoroutineConcurrentConstructor" - {
        "constructs the right amount of elements" {
            checkAll(arbCoroutineConcurrentConstructor<Int>(), Arb.int(0..1000)) { constructor, size ->
                val sequence = constructor(size) { it }
                sequence.size shouldBe size
            }
        }

        "constructs the right elements" {
            checkAll(arbCoroutineConcurrentConstructor<Int>(), Arb.int(0..1000)) { constructor, size ->
                val sequence1 = constructor(size) { it }
                sequence1 shouldBe (0..<size).toList()

                val sequence2 = constructor(size) { it * 2 }
                sequence2 shouldBe (0..<size).map { it * 2 }
            }
        }
    }
})

/**
 * Generates an arbitrary instance of [CoroutineConcurrentConstructor] with a randomly selected `CoroutineDispatcher`.
 *
 * @param T The type of elements that the [CoroutineConcurrentConstructor] will operate on.
 * @param dispatcherArb An `Arb<CoroutineDispatcher>` that generates random dispatchers. Defaults to a selection of
 *   [Dispatchers.Default], [Dispatchers.Unconfined].
 * @return An `Arb<CoroutineConcurrentConstructor<T>>` that generates instances of `CoroutineConcurrentConstructor`
 *   using random dispatchers.
 */
fun <T> arbCoroutineConcurrentConstructor(
    dispatcherArb: Arb<CoroutineDispatcher> = Arb.element(
        Dispatchers.Default,
        Dispatchers.Unconfined
    )
) = dispatcherArb.map {
    CoroutineConcurrentConstructor<T>(
        CoroutineScope(it)
    )
}
