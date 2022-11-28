package cl.ravenhill.keen.util.math

import cl.ravenhill.keen.InvalidReceiverException
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.genes.BoolGene
import cl.ravenhill.keen.util.indexes
import cl.ravenhill.keen.util.validateAtLeast
import cl.ravenhill.keen.util.validateRange
import cl.ravenhill.keen.util.validateSize
import kotlin.experimental.or

/**
 * Returns the minimum number of bytes required to store ``this`` number of bits.
 */
fun Int.toByteLength() = when {
    this < 0 -> throw InvalidReceiverException { "The number of bytes must be non negative." }
    this and 7 == 0 -> this ushr 3
    else -> (this ushr 3) + 1
}

/**
 * Create a new [ByteArray] which can store at least the number of bits as defined by the given
 * size parameter.
 * The returned byte array is initialized with ones according to the given ``onesProbability``..
 *
 * @param size the number of bits, the returned [ByteArray] can store.
 * @param onesProbability the ones probability of the returned byte array..
 */
fun byteArrayOf(size: Int, onesProbability: Double = 0.5) =
    ByteArray(size.toByteLength()).apply {
        Core.rng.indexes(onesProbability, size).forEach {
            this[it ushr 3] = this[it ushr 3] or ((1 shl (it and 7)).toByte())
        }
    }

/**
 * Creates a new [BitArray].
 */
fun bitArrayOf(size: Int, bitProbability: Double) =
    BitArray(byteArrayOf(size, bitProbability), size, 0)

/**
 * Fixed sized array of bits.
 *
 * @property bytes  the backing [ByteArray].
 * @property end    the end bit index (exclusive).
 * @property start  the start bit index (inclusive).
 * @property size   the size of the array.
 *
 * @author <a href="https://github.com/r8vnhill">R8V</a>
 */
class BitArray(private val bytes: ByteArray, private val end: Int, private val start: Int = 0) {
    private val size: Int = end - start

    init {
        bytes.size.validateSize(true to { "Byte array must have at least one element. " })
        end.validateAtLeast(0) { "End index [$end] must be at least 0. " }
        end.validateRange(start..bytes.size * Byte.SIZE_BITS) { "End index [$end] must be in range [$start, ${bytes.size * Byte.SIZE_BITS}]. " }
    }

    fun toBitGeneList(): List<BoolGene> {
        val genes = MutableList<BoolGene>(size) { BoolGene.False }
        for (i in genes.indices) {
            genes[genes.size - 1 - i] = if (get(i)) BoolGene.True else BoolGene.False
        }
        return genes
    }

    operator fun get(i: Int) =
        (start + i).let { bytes[it ushr 3].toInt() and (1 shl (it and 7)) != 0 }
}