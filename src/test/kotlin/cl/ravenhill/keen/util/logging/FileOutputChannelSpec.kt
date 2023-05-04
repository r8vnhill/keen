/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.util.logging

import cl.ravenhill.keen.shouldBeEqualIgnoringBreaks
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.file.shouldExist
import io.kotest.matchers.file.shouldNotExist
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeBlank
import io.kotest.property.Arb
import io.kotest.property.arbitrary.file
import io.kotest.property.arbitrary.string
import io.kotest.property.assume
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

        "create an output file if it doesn't exist when writing a non-blank message" {
            checkAll(
                Arb.fileOutputChannel(Arb.filename()),
                Arb.string()
            ) { (channel, filename), message ->
                assume {
                    File(filename).shouldNotExist()
                    message.shouldNotBeBlank()
                }
                val file = File(filename)
                try {
                    channel.write(message)
                    file.shouldExist()
                    file.readText() shouldBeEqualIgnoringBreaks message
                } finally {
                    if (file.exists()) {
                        file.delete()
                    }
                }
            }
        }

        "write to an existing file" {
            checkAll(
                Arb.fileOutputChannel(Arb.filename()),
                Arb.string()
            ) { (channel, filename), message ->
                assume {
                    File(filename).shouldNotExist()
                    message.shouldNotBeBlank()
                }
                val file = File(filename)
                try {
                    file.writeText("Hello, world!\n")
                    channel.write(message)
                    file.shouldExist()
                    file.readText() shouldBeEqualIgnoringBreaks "Hello, world!\n$message"
                } finally {
                    if (file.exists()) {
                        file.delete()
                    }
                }
            }
        }

        "be able to set the output file path" {
            checkAll(
                Arb.fileOutputChannel(Arb.filename()),
                Arb.file()
            ) { (channel, filename), newFile ->
                channel.filename shouldBe filename
                channel.filename = newFile.name
                channel.filename shouldBe newFile.name
            }
        }

        "return a failure when trying to add a child" {
            `check that trying to add a child channel returns a failure` {
                FileOutputChannel()
            }
        }
    }
})