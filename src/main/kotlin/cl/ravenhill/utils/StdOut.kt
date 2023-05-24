/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.utils

import java.io.OutputStream
import java.io.PrintStream

/**
 * Runs a block of code with stdout turned off.
 * Restores stdout after execution.
 *
 * ## Examples
 * ### Example 1: Hiding standard output of a println
 * ```kotlin
 * runWithStdoutOff {
 *     println("You won't see this message in the console")
 * }
 * println("You will see this message in the console")
 * ```
 * In this example, the `println` inside the `runWithStdoutOff` function won't print anything to the
 * console, but the one after it will.
 *
 * @param block A block of code to run with stdout turned off.
 */
fun runWithStdoutOff(block: () -> Unit) {
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
