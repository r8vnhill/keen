package cl.ravenhill.keen.util.math

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.InvalidReceiverException
import cl.ravenhill.keen.genetic.genes.BoolGene
import cl.ravenhill.keen.requirements.IntRequirement.BeAtLeast
import cl.ravenhill.keen.requirements.IntRequirement.BeInRange
import cl.ravenhill.keen.util.indices
import kotlin.experimental.or

/***************************************************************************************************
 * This code defines functions to work with bit arrays.
 * The estimateByteLength() function calculates the number of bytes required to represent an integer
 * as a bit string.
 * The byteArrayOf() function creates a byte array that can store a given number of bits with a
 * specified probability of ones.
 * The bitArrayOf() function creates a new BitArray of a given size and probability distribution.
 * The BitArray class represents a fixed-size bit array with a given range of bits from a byte
 * array.
 * It has functions to return a list of boolean genes corresponding to the bits in the array and to
 * get the value of a specific bit.
 **************************************************************************************************/

/**
 * Returns the number of bytes required to represent this [Int] as a bit string, rounded up to the
 * nearest byte.
 *
 * @throws InvalidReceiverException if the receiver [Int] is negative.
 */
fun Int.estimateByteLength() = when {
    this < 0 -> throw InvalidReceiverException { "The number of bytes must be non negative." }
    // If the value is a multiple of 8, then the result is simply the value divided by 8
    this and 7 == 0 -> this ushr 3
    // Otherwise, the result is the value divided by 8, plus 1
    else -> (this ushr 3) + 1
}

/**
 * Create a new [ByteArray] which can store at least the number of bits as defined by the given
 * size parameter.
 * The returned byte array is initialized with ones according to the given ``onesProbability``.
 *
 * @param size the number of bits, the returned [ByteArray] can store.
 * @param onesProbability the ones probability of the returned byte array..
 */
fun byteArrayOf(size: Int, onesProbability: Double = 0.5) =
    ByteArray(size.estimateByteLength()).apply {
        Core.random.indices(onesProbability, size).forEach {
            this[it ushr 3] = this[it ushr 3] or ((1 shl (it and 7)).toByte())
        }
    }

/**
 * Creates a new [BitArray] with the specified [size] and [bitProbability].
 * The [size] parameter determines the number of bits in the bit array, and the [bitProbability]
 * parameter determines the probability of each bit being set to 1.
 *
 * @param size The number of bits in the bit array.
 * @param bitProbability The probability of each bit being set to 1.
 *  This value must be between 0 and 1.
 *
 * @return A new [BitArray] with the specified size and probability distribution.
 */
fun bitArrayOf(size: Int, bitProbability: Double) =
    BitArray(byteArrayOf(size, bitProbability), size, 0)

/**
 * Represents a fixed-size bit array with a given range of bits from a byte array.
 *
 * @param bytes the byte array containing the bits.
 * @param end the index of the bit after the last bit in this BitArray.
 * @param start the index of the first bit in this BitArray.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 1.0.0
 */
class BitArray(private val bytes: ByteArray, private val end: Int, private val start: Int = 0) {
    private val size: Int = end - start

    init {
        enforce {
            bytes.size should BeAtLeast(1) { "Byte array must have at least one element. " }
            end should BeAtLeast(0) { "End index [$end] must be at least 0. " }
            end should BeInRange(start..bytes.size * Byte.SIZE_BITS) {
                "End index [$end] must be in range [$start, ${bytes.size * Byte.SIZE_BITS}]. "
            }
        }
    }

    /**
     * Returns the list of Boolean genes corresponding to the bits in this BitArray.
     * The genes are returned in reverse order because the least significant bit in the bit array
     * corresponds to the first gene in the genetic sequence, and the most significant bit
     * corresponds to the last gene.
     */
    fun toBoolGeneList() = List(size) { index ->
        if (get(index)) BoolGene.True else BoolGene.False
    }.reversed()

    /**
     * Returns the value of the i-th bit in this BitArray.
     * The i-th bit is accessed by first finding the byte that contains it, then performing
     * a bitwise AND operation with a mask that has a 1 in the i-th bit position and 0's
     * in all other positions. If the result of this operation is non-zero, then the i-th
     * bit is set to 1, otherwise it is set to 0.
     */
    operator fun get(i: Int): Boolean {
        val index = start + i
        // Find the index of the byte containing the i-th bit
        val byteIndex = index ushr 3
        // Create a mask with a 1 in the i-th bit position and 0's in all other positions
        val mask = 1 shl (index and 7)
        // Perform a bitwise AND operation between the byte and the mask
        // If the result is non-zero, then the i-th bit is set to 1, otherwise it is set to 0
        return bytes[byteIndex].toInt() and mask != 0
    }
}
