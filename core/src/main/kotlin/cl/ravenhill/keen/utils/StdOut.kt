/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.utils

import java.io.OutputStream
import java.io.PrintStream


/**
 * Executes a given block of code with standard output (stdout) temporarily turned off.
 *
 * This function temporarily redirects the standard output to a null OutputStream, effectively silencing
 * any print statements or output generated within the provided block of code. After the block has been
 * executed, the standard output is restored to its original state. This is useful for cases where you want
 * to suppress output for certain operations without permanently altering the output stream.
 *
 *
 * ## Usage:
 * ```
 * runWithStdoutOff {
 *     println("This will not be printed to stdout.")
 *     // Other code
 * }
 * println("This will be printed to stdout.")
 * ```
 * In this example, the `println` inside the `runWithStdoutOff` block will not produce output in the console,
 * but the `println` outside the block will.
 *
 * @param block A lambda function representing the block of code to be executed with stdout turned off.
 */
inline fun runWithStdoutOff(block: () -> Unit) {
    val originalOut = System.out // Save the original stdout
    // Redirect stdout to a null OutputStream
    System.setOut(PrintStream(object : OutputStream() {
        override fun write(b: Int) {
            // Do nothing
        }
    }))
    try {
        block() // Execute the given block
    } finally {
        System.setOut(originalOut) // Restore stdout
    }
}
