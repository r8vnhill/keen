//package cl.ravenhill.keen.prog
//
//import cl.ravenhill.keen.Core
//import cl.ravenhill.keen.prog.functions.*
//import cl.ravenhill.keen.prog.terminals.Terminal
//import cl.ravenhill.keen.prog.terminals.ephemeralConstant
//import cl.ravenhill.keen.prog.terminals.variable
//import cl.ravenhill.keen.util.program
//import io.kotest.property.Arb
//import io.kotest.property.arbitrary.*
//
//
///**
// * Generates a reduceable expression.
// */
//fun Arb.Companion.program() = arbitrary { rs ->
//    val terminals = Arb.terminals().bind()
//    val functions = Arb.functions().bind()
//    rs.random.program(Core.maxProgramDepth - 1, functions, terminals)
//}
//
///**
// * Generates an arbitrary list of functions.
// */
//fun Arb.Companion.functions(): Arb<List<Fun<Double>>> = arbitrary {
//    Arb.list(Arb.element(Add(), GreaterThan(), Mul(), If()), 1..100).bind().distinct()
//}
//
///**
// * Generates an arbitrary list of terminals.
// */
//fun Arb.Companion.terminals(): Arb<List<Terminal<Double>>> = arbitrary {
//    Arb.list(
//        Arb.choice(
//            Arb.ephemeralConstant(),
//            Arb.variable()
//        ), 1..100
//    ).bind().distinct()
//}
