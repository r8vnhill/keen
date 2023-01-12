package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.genetic.genes.numerical.DoubleGeneData
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import cl.ravenhill.keen.orderedIntPair
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.int

data class IntGeneData(val value: Int, val range: Pair<Int, Int>)

fun Arb.Companion.intGene(lo: Int = Int.MIN_VALUE, hi: Int = Int.MAX_VALUE) =
    arbitrary {
        val range = orderedIntPair(lo, hi).bind()
        val value = int(range.first until range.second).bind()
        IntGeneData(value, range)
    }

fun Arb.Companion.doubleGene() = arbitrary { rs ->
    val min = rs.random.nextDouble()
    val max = rs.random.nextDouble()
    val range = if (min < max) min to max else max to min
    val dna = rs.random.nextDouble(range.first, range.second)
    DoubleGeneData(dna, range)
}