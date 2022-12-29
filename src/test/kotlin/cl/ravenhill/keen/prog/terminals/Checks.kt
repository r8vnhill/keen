package cl.ravenhill.keen.prog.terminals

import cl.ravenhill.keen.util.Copyable
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.checkAll


/**
 * Property based test that checks that a copy function works as expected.
 */
suspend fun <T> checkCopy(generator: Arb<T>, copyFn: (T) -> T) {
    checkAll(generator) { generated ->
        copyFn(generated) shouldBe generated
    }
}

/**
 * Property based test that checks that an object should always be equal to itself.
 *
 * @param generator The [Arb]itrary generator of the object to test.
 */
suspend fun <T> `check that an object should always be equal to itself`(
    generator: Arb<T>
) {
    checkAll(generator) { generated ->
        generated shouldBe generated
    }
}

suspend fun <T> `check that an object should always be equal to a copy of itself`(
    generator: Arb<T>
) where T: Copyable<T> {
    checkAll(generator) { generated ->
        val copy = generated.deepCopy()
        generated shouldBe copy
    }
}

suspend fun <T : Terminal<*>> `check that a terminal should always have arity 0`(
    generator: Arb<T>
) {
    checkAll(generator) { generated ->
        generated.arity shouldBe 0
    }
}


suspend fun <T> `check that a terminal should always flatten to a list with itself`(
    generator: Arb<T>
) where T : Terminal<*> {
    checkAll(generator) {
        it.flatten() shouldBe listOf(it)
    }
}

suspend fun <T : Terminal<*>>
        `check that a terminal should always have an empty list of descendants`(
    generator: Arb<T>
) {
    checkAll(generator) { terminal ->
        terminal.descendants shouldBe emptyList()
    }
}