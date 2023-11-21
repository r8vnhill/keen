//package cl.ravenhill.keen.genetic.genes
//
//import cl.ravenhill.keen.genetic.genes.numerical.DoubleGene
//import cl.ravenhill.keen.genetic.genes.numerical.DoubleGeneData
//import cl.ravenhill.keen.genetic.genes.numerical.IntGene
//import cl.ravenhill.keen.orderedIntPair
//import io.kotest.property.Arb
//import io.kotest.property.arbitrary.arbitrary
//import io.kotest.property.arbitrary.double
//import io.kotest.property.arbitrary.int
//import io.kotest.property.arbitrary.next
//
//data class IntGeneData(val value: Int, val range: Pair<Int, Int>)
//
//@OptIn(ExperimentalStdlibApi::class)
//fun Arb.Companion.intGene(lo: Int = Int.MIN_VALUE, hi: Int = Int.MAX_VALUE) =
//    arbitrary {
//        val range = orderedIntPair(lo, hi).bind()
//        val value = int(range.first ..< range.second).bind()
//        IntGene(value, range)
//    }
//
//fun Arb.Companion.doubleGene() = arbitrary {
//    val min = double().next()
//    val max = double().next()
//    val range = if (min < max) min to max else max to min
//    val dna = double(range.first, range.second).next()
//    DoubleGene(dna, range)
//}