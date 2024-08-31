/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners

import arrow.core.getOrElse
import cl.ravenhill.jakt.constrainedTo
import cl.ravenhill.jakt.constraints.ints.BePositive
import kotlinx.coroutines.CoroutineDispatcher
import org.jline.terminal.TerminalBuilder

/**
 * Retrieves the current width of the system's terminal console.
 *
 * The `consoleWidth` property provides the width of the terminal console in characters, allowing developers to
 * dynamically adjust output formatting based on the available console width.
 *
 * @return The width of the terminal in characters.
 * @throws SecurityException  if a security manager exists and its `checkPermission` method doesn't allow setting of the
 *   specified property.
 */
internal actual suspend fun consoleWidth(dispatcher: CoroutineDispatcher): Int {
    System.setProperty("org.jline.terminal.dumb", "true")
    return TerminalBuilder.builder()
        .system(true)
        .build()
        .width
        .constrainedTo { "The terminal width must be greater than 0" { it must BePositive } }
        .getOrElse { 80 }
        .takeIf { it > 40 }
        ?: 40
}
