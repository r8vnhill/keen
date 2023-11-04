/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arbs.datatypes

import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.byte
import io.kotest.property.arbitrary.char
import io.kotest.property.arbitrary.choice
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.float
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.long
import io.kotest.property.arbitrary.short
import io.kotest.property.arbitrary.string

/**
 * Returns an arbitrary generator that produces values of [Any] type.
 * The generated values can be of the following types:
 * [String], [Int], [Long], [Double], [Float], [Boolean], [Char], [Byte], [Short].
 *
 * All the types are generated with the default generators provided by _Kotest_.
 */
fun Arb.Companion.any() = arbitrary {
    choice(
        string(),
        int(),
        long(),
        double(),
        float(),
        boolean(),
        char(),
        byte(),
        short()
    ).bind()
}