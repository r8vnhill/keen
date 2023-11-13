package cl.ravenhill.keen.arbs.limits

import cl.ravenhill.keen.limits.GenerationCount
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary

fun Arb.Companion.limit() = arbitrary {
    GenerationCount(1)
}