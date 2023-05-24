/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.utils

import com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEmpty
import io.kotest.property.Arb
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

/**
 * Test class for verifying the behaviour of stdout-related functionality.
 * It tests various scenarios related to standard output (stdout) and ensures the expected
 * behaviour is observed.
 *
 * @see runWithStdoutOff
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class StdOutTest : FreeSpec({
    "Running a block of code with stdout turned off should not print anything to the console" {
        checkAll(Arb.list(Arb.string())) { strings ->
            tapSystemOut {
                // Printing each string without stdout turned off
                strings.forEach { print(it) }
            } shouldBe strings.joinToString("")
            tapSystemOut {
                runWithStdoutOff {
                    // Printing each string with stdout turned off
                    strings.forEach { print(it) }
                }
            }.shouldBeEmpty()
        }
    }
})
