package cl.ravenhill.keen.internals

import cl.ravenhill.keen.util.validateAtLeast
import cl.ravenhill.keen.util.validateRange
import cl.ravenhill.keen.util.validateSize


class BitArray(private val bytes: ByteArray, end: Int, start: Int) {
    init {
        bytes.size.validateSize(true to { "Byte array must have at least one element. " })
        end.validateAtLeast(0) { "End index [$end] must be at least 0. " }
        end.validateRange(start..bytes.size * Byte.SIZE_BITS)
    }
}