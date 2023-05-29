/**
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.util.logging

import cl.ravenhill.keen.InvalidStateException
import io.kotest.matchers.result.FailureMatcher
import io.kotest.matchers.should
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.stringPattern
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
        fileOutputChannel(filename()).bind().first,
        bufferedOutputChannel().bind(),
    ).bind()
}


/**
 * Returns an arbitrary generator for [CompositeOutputChannel].
 *
 * The returned generator creates a composite output channel using an arbitrary array of output channels,
 * generated by calling [outputChannel] and creating an array with its values.
 *
 * @return An arbitrary generator for [CompositeOutputChannel].
 *
 * @see [outputChannel]
 */
fun Arb.Companion.compositeOutputChannel(range: IntRange = 1..100) = arbitrary {
    CompositeOutputChannel(*array(outputChannel(), range).bind())
}

/**
 * Creates an arbitrary generator of an array of type [T], based on a given [gen] generator of type
 * [T].
 *
 * The function uses reified type parameter [T] to allow accessing the type at runtime, which is
 * required to create the array with the proper type.
 * The returned generator creates an array of arbitrary length containing elements generated by
 * [gen].
 *
 * @param T The type of the elements of the array.
 * @param gen The generator used to create the elements of the array.
 * @return An arbitrary generator of an array of type [T].
 */
private inline fun <reified T> Arb.Companion.array(gen: Arb<T>, range: IntRange = 0..100) =
    arbitrary { list(gen, range).bind().toTypedArray() }

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
 * @param filename The arbitrary that generates the [File] used to set the ``filename`` property of the
 * [FileOutputChannel].
 * @return An arbitrary that generates a [Pair] of a [FileOutputChannel] and a [String] containing
 * the filename.
 */
fun Arb.Companion.fileOutputChannel(filename: Arb<String>) = arbitrary {
    filename.bind().let {
        FileOutputChannel().apply { this.filename = it } to it
    }
}

/**
 * Provides arbitrary instances of [BufferedOutputChannel] for property-based testing.
 */
fun Arb.Companion.bufferedOutputChannel() = arbitrary { BufferedOutputChannel() }


/**
 * Returns an [Arb] that generates arbitrary valid filenames.
 *
 * The generated filenames have the following format:
 * [a-zA-Z0-9_-]+\\.\\w{1,5}
 *
 * That is, they consist of one or more alphanumeric characters, dashes, or underscores,
 * followed by a dot and a file extension consisting of 1 to 5 alphanumeric characters.
 *
 * @return an [Arb] that generates arbitrary valid filenames.
 */
fun Arb.Companion.filename(): Arb<String> = arbitrary {
    stringPattern("[a-zA-Z0-9_-]+\\.\\w{1,5}").bind()
}