package cl.ravenhill.keen.prog.terminals

import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.checkAll


suspend fun <T> checkCopy(generator: Arb<T>, copyFn: (T) -> T) {
    checkAll(generator) { generated ->
        copyFn(generated) shouldBe generated
    }
}