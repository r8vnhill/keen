/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.listeners

import cl.ravenhill.keen.Domain
import kotlinx.coroutines.CoroutineDispatcher

/**
 * Provides the width of the console in characters.
 *
 * The `consoleWidth` property is expected to return the width of the console or terminal in characters, allowing
 * developers to dynamically adjust text output based on the available space.
 *
 * ## Platform-Specific Implementation:
 * This property is declared using Kotlin's `expect/actual` mechanism, meaning that the actual implementation of how
 * the console width is determined will vary depending on the platform (e.g., JVM, JavaScript, Native). Each platform
 * should provide its own `actual` implementation that accurately retrieves the console width for that environment.
 */
internal expect suspend fun consoleWidth(dispatcher: CoroutineDispatcher = Domain.dispatcher): Int