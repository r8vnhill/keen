/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.assertions

import cl.ravenhill.keen.exceptions.AbsurdOperation
import io.kotest.assertions.throwables.shouldThrow

/**
 * Tests that the provided block of code throws an `AbsurdOperation` exception.
 *
 * This function is designed for use in test scenarios where `AbsurdOperation` is expected to be thrown. It verifies
 * that the execution of the given block leads to the specified exception. This is particularly useful when testing
 * theoretical or exceptional cases in the implementation, such as operations involving `NothingGene`.
 *
 * ## Usage:
 * Use this function in unit tests where `AbsurdOperation` is the expected outcome. The function encapsulates the
 * pattern of expecting an exception in a concise and readable form, making tests cleaner and more expressive.
 *
 * ### Example:
 * ```kotlin
 * // In a test suite
 * "attempting to duplicate NothingGene" should {
 *     "throw AbsurdOperation" {
 *         check that an Absurd Operation is thrown {
 *             NothingGene.duplicateWithValue( /* No valid value can be provided */ )
 *         }
 *     }
 * }
 * ```
 * In this test example, the function is used to assert that an `AbsurdOperation` exception is thrown when attempting
 * to duplicate a `NothingGene`. The syntax makes it clear what behavior is being tested and what the expected outcome
 * is.
 *
 * @param block The block of code that is expected to throw an `AbsurdOperation`. This block contains the operation
 *   or function call that should lead to the exception.
 * @throws AbsurdOperation if the provided block does indeed throw this exception, indicating the test passes.
 */
fun `check that an Absurd Operation is thrown`(block: () -> Unit) = shouldThrow<AbsurdOperation>(block)
