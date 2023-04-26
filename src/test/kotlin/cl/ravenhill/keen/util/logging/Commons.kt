/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.util.logging

import cl.ravenhill.keen.InvalidStateException
import io.kotest.matchers.result.FailureMatcher
import io.kotest.matchers.should
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.file
import io.kotest.property.checkAll
import java.io.File


/**
 * Checks that attempting to add a child channel to an output channel returns a failure.
 *
 * @param builder A function that returns a new instance of the output channel being tested.
 */
suspend fun <T : OutputChannel<T>> `check that trying to add a child channel returns a failure`(
    builder: () -> T
) {
    checkAll(Arb.outputChannel()) { child ->
        val outputChannel = builder()
        outputChannel.add(child) should FailureMatcher(InvalidStateException(
            "${outputChannel::class.simpleName}"
        ) { "Cannot add channel to this channel." })
    }
}

/**
 * Provides arbitrary instances of [OutputChannel] for property-based testing.
 *
 * @see [stdoutOutputChannel]
 * @see [fileOutputChannel]
 * @see [bufferedOutputChannel]
 */
fun Arb.Companion.outputChannel() = arbitrary {
    element(
        stdoutOutputChannel().bind(),
        fileOutputChannel(file()).bind().first,
        bufferedOutputChannel().bind()
    ).bind()
}

/**
 * Provides arbitrary instances of [StdoutChannel] for property-based testing.
 */
fun Arb.Companion.stdoutOutputChannel() = arbitrary { StdoutChannel() }

/**
 * Returns an arbitrary that generates a [Pair] of a [FileOutputChannel] and a [String] containing
 * the filename.
 * The ``filename`` property of the [FileOutputChannel] is set to the filename in the generated
 * [File].
 *
 * @param file The arbitrary that generates the [File] used to set the ``filename`` property of the
 * [FileOutputChannel].
 * @return An arbitrary that generates a [Pair] of a [FileOutputChannel] and a [String] containing
 * the filename.
 */
fun Arb.Companion.fileOutputChannel(file: Arb<File>) = arbitrary {
    val filename = file.bind().name
    FileOutputChannel().apply { this.filename = filename } to filename
}

/**
 * Provides arbitrary instances of [BufferedOutputChannel] for property-based testing.
 */
fun Arb.Companion.bufferedOutputChannel() = arbitrary { BufferedOutputChannel() }
