/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen

import io.kotest.property.PropTestListener
import kotlin.random.Random


object ResetDomainListener : PropTestListener {
    override suspend fun afterTest() {
        Domain.random = Random.Default
        Domain.equalityThreshold = Domain.DEFAULT_EQUALITY_THRESHOLD
        Domain.toStringMode = ToStringMode.DEFAULT
    }
}
