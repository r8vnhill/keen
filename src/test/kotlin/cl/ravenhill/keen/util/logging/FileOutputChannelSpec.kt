/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.util.logging

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.chunked
import io.kotest.property.arbitrary.file
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll
import java.io.File

/**
 * A specification for the [FileOutputChannel] class.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class FileOutputChannelSpec : FreeSpec({
    /** The default output file path.   */
    val defaultOutputFilePath = "keen.log"

    "FileOutputChannel should" - {
        "have a default output file path" {
            FileOutputChannel().filename shouldBe defaultOutputFilePath
        }

        "be able to set the output file path" {
            checkAll(
                Arb.fileOutputChannel(Arb.file()),
                Arb.file()
            ) { (channel, filename), newFile ->
                channel.filename shouldBe filename
                channel.filename = newFile.name
                channel.filename shouldBe newFile.name
            }
        }

        "write to file" {
            checkAll(
                Arb.fileOutputChannel(Arb.file()),
                Arb.string().chunked(1..500)
            ) { (channel, filename), messages ->
                val file = File(filename)

            }
        }

        "return a failure when trying to add a child" {
            `check that trying to add a child channel returns a failure` {
                FileOutputChannel()
            }
        }
    }
})