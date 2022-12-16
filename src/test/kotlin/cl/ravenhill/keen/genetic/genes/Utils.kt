package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.genetic.genes.numerical.DoubleGeneData
import cl.ravenhill.keen.genetic.genes.numerical.IntGene
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary

data class IntGeneData(val value: Int, val range: Pair<Int, Int>) {
    fun toIntGene(): IntGene {
        return IntGene(value, range)
    }
}

fun Arb.Companion.intGene(lo: Int = Int.MIN_VALUE, hi: Int = Int.MAX_VALUE) =
    arbitrary { rs ->
        val r1 = rs.random.nextInt(lo, hi)
        val r2 = rs.random.nextInt(lo, hi)
        val range = if (r1 < r2) r1 to r2 else r2 to r1
        val value = rs.random.nextInt(range.first, range.second)
        IntGeneData(value, range)
    }

fun Arb.Companion.doubleGene() = arbitrary { rs ->
    val min = rs.random.nextDouble()
    val max = rs.random.nextDouble()
    val range = if (min < max) min to max else max to min
    val dna = rs.random.nextDouble(range.first, range.second)
    DoubleGeneData(dna, range)
}