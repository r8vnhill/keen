//package cl.ravenhill.keen.prog
//
//import io.kotest.matchers.shouldBe
//import io.kotest.property.Arb
//import io.kotest.property.checkAll
//
//suspend fun <T : Reduceable<*>>
//        `check that a reduceable should always be created without a parent`(
//    generator: Arb<T>
//) {
//    checkAll(generator) { generated ->
//        generated.parent shouldBe null
//    }
//}