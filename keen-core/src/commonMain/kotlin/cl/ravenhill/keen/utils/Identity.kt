/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.utils

/**
 * Returns the input value unchanged.
 *
 * The `identity` function is a simple utility that returns its input parameter `t` without modification. This function
 * is commonly used in functional programming as a default or no-op function, where an operation needs to be provided
 * but no transformation or processing of the input is required.
 *
 * @param T The type of the input value.
 * @param t The input value to be returned.
 * @return The input value `t` unchanged.
 */
fun <T> identity(t: T): T = t
