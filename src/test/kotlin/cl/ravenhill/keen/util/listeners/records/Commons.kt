/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.util.listeners.records

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.nonNegativeInt
import kotlin.time.TimeMark
import kotlin.time.TimeSource


/**
 * Provides an arbitrary (Arb) instance that generates a [GenerationRecordData] object.
 *
 * The provided Arb object generates a GenerationRecordData object, which contains a non-negative
 * integer representing the generation number. This is useful when you need an arbitrary
 * generation record for testing or other purposes.
 *
 * @return An Arb instance that generates a [GenerationRecordData] object.
 */
internal fun Arb.Companion.generationRecord() = arbitrary {
    GenerationRecord(nonNegativeInt().bind()).apply {
        startTime = TimeSource.Monotonic.markNow()
    }
}
