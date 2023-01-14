package cl.ravenhill.keen

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import kotlin.reflect.KClass


/**
 * Matcher that checks if the given object is of the given class.
 */
infix fun Any.shouldBeOfClass(kClass: KClass<*>) = Matcher<Any> { value ->
    MatcherResult(
        value::class == kClass,
        { "$value should be an instance of $kClass" },
        { "$value should not be an instance of $kClass" }
    )
}

/**
 * Generates [Arb]itrary [Pair]s of [Int]s where the first element is less than or equal
 * to the second.
 *
 * __Usage:__
 * ```kotlin
 * checkAll(Arb.orderedIntPair()) { (a, b) ->
 *    a <= b shouldBe true
 *    a + b shouldBe b + a
 *    a * b shouldBe b * a
 *    a - b shouldBe -(b - a)
 *    a / b shouldBe 0
 *    a % b shouldBe a
 *    a * b + a - b / a % b shouldBe a * b
 * }
 * ```
 */
fun Arb.Companion.orderedIntPair(lo: Int = Int.MIN_VALUE, hi: Int = Int.MAX_VALUE) =
    arbitrary {
        val first = int(lo, hi).bind()
        val second = int(lo, hi).bind().let { if (it == first) it + 1 else it }
        if (first < second) first to second else second to first
    }

/**
 * Generates [Arb]itrary [Pair]s of [Double]s where the first element is less than or
 * equal to the second.
 */
fun Arb.Companion.orderedDoublePair(
    lo: Double = Double.MIN_VALUE,
    hi: Double = Double.MAX_VALUE
) =
    arbitrary {
        val first = double(lo, hi).next()
        val second = double(lo, hi).next().let { if (it == first) it + 1 else it }
        if (first < second) first to second else second to first
    }

/**
 * Generates an [Arb]itrary [Int] value outside the given ``intRange``.
 *
 * Behaviour when the given range encompasses the whole [Int] range is undefined.
 */
fun Arb.Companion.intOutsideRange(intRange: IntRange) = arbitrary {
    if (intRange.first == Int.MIN_VALUE) {
        int(intRange.last + 1, Int.MAX_VALUE).bind()
    } else {
        int(Int.MIN_VALUE, intRange.first - 1).bind()
    }
}

/**
 * Generates a new [Arb]itrary [Genotype] using a given arbitrary [Chromosome] factory.
 */
fun <T> Arb.Companion.genotype(
    chromosome: Arb<Chromosome.Factory<T>>,
    maxSize: Int = 100
) =
    arbitrary {
        val chromosomes = Arb.list(chromosome, 1..maxSize).bind()
        Genotype.Factory<T>().apply {
            chromosomes.forEach {
                chromosome { it }
            }
        }.make()
    }

/**
 * Generates a new [Arb]itrary [IntChromosome] factory.
 */
fun Arb.Companion.intChromosomeFactory(maxSize: Int = 100) = arbitrary {
    IntChromosome.Factory().apply {
        size = positiveInt(maxSize).bind()
        range = orderedIntPair().bind()
    }
}

/**
 * Generates an [Arb]itrary [Phenotype].
 */
fun <T> Arb.Companion.phenotype(
    chromosomeFactory: Arb<Chromosome.Factory<T>>,
    fitness: Double = double().next()
) = arbitrary {
    val genotype = genotype(chromosomeFactory).bind()
    val generation = positiveInt().bind()
    Phenotype(genotype, generation, fitness)
}