/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package keen

import io.kotest.property.PropTestListener
import kotlinx.coroutines.Dispatchers
import kotlin.random.Random

/**
 * A test listener that resets the [Domain] settings to their default values after each test.
 */
object ResetDomainListener : PropTestListener {
    /**
     * Resets the [Domain] settings to their default values.
     */
    override suspend fun afterTest() {
        Domain.random = Random.Default
        Domain.equalityThreshold = Domain.DEFAULT_EQUALITY_THRESHOLD
        Domain.toStringMode = ToStringMode.DEFAULT
        Domain.dispatcher = Dispatchers.Default
    }
}
