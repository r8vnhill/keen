/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners

import arrow.core.getOrElse
import cl.ravenhill.jakt.constrainedTo
import cl.ravenhill.keen.Domain
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.await
import kotlinx.coroutines.withContext
import kotlin.js.Promise

internal actual suspend fun consoleWidth(dispatcher: CoroutineDispatcher): Int = withContext(dispatcher) {
    try {
        // Dynamically import the terminal-size module
        val terminalSizePromise = js("import('terminal-size')") as Promise<dynamic>
        val terminalSize = terminalSizePromise.await()
        // Ensure the size object has a width property
        val width = terminalSize.width as? Int ?: Domain.fallbackConsoleWidth
        width.constrainedTo { "The terminal width must be greater than 0" { it must cl.ravenhill.jakt.constraints.ints.BePositive } }
            .getOrElse { Domain.fallbackConsoleWidth }
            .takeIf { it > 40 }
            ?: 40
    } catch (e: dynamic) {
        console.error("Error during dynamic import or retrieving size: ${e.message}")
        Domain.fallbackConsoleWidth // Fallback width
    }
}