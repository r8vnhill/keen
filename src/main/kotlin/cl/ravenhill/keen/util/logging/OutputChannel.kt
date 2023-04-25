package cl.ravenhill.keen.util.logging

import cl.ravenhill.keen.InvalidStateException
import cl.ravenhill.keen.util.Clearable
import java.io.File

/***************************************************************************************************
 * This code defines several classes and interfaces for creating and managing output channels that
 * can write messages to different sources.
 * The `OutputChannel` interface defines a method for writing messages, while the
 * `CompositeOutputChannel` class allows the creation of a composite channel that can write to
 * multiple channels simultaneously.
 * The `BufferedOutputChannel` class implements a buffered output channel that stores all messages
 * written to it in a `StringBuilder`.
 * The `StdoutChannel` and `FileOutputChannel` classes define output channels that write to the
 * standard output and a file, respectively.
 * The code also includes builder functions for creating and adding new channels to the Logger
 * class.
 **************************************************************************************************/

/**
 * Channel where messages can be written to.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
interface OutputChannel {
    /** Writes a message to the channel.    */
    fun write(message: String)

    /**
     * Adds a new output channel to this channel.
     *
     * @param outputChannel The output channel to add.
     * @return A result indicating if the operation was successful or not.
     */
    fun add(outputChannel: OutputChannel): Result<Boolean> =
        Result.failure(InvalidStateException("${this::class.simpleName}") {
            "Cannot add channel to this channel."
        })
}

/**
 * A composite output channel that can contain multiple output channels.
 * This channel will write to all the channels it contains.
 *
 * @property outputChannels The output channels that this channel will contain.
 * @constructor Creates a new composite output channel.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
class CompositeOutputChannel(vararg outputChannels: OutputChannel) : OutputChannel {
    private val outputChannels = outputChannels.toMutableList()

    override fun write(message: String) =
        outputChannels.parallelStream().forEach { it.write(message) }

    override fun add(outputChannel: OutputChannel) =
        outputChannels.add(outputChannel).let { Result.success(it) }

    companion object {
        /**
         * Builder for [CompositeOutputChannel].
         */
        val builder: Builder
            get() = Builder()
    }

    /**
     * Builder for [CompositeOutputChannel].
     *
     * @property outputStreams The output channels that this builder will contain.
     * @constructor Creates a new builder for [CompositeOutputChannel].
     *
     * @author <a href="https://www.github.com/r8vnhill">R8V</a>
     * @version 2.0.0
     * @since 2.0.0
     */
    class Builder {
        private val outputStreams = mutableListOf<OutputChannel>()

        /**
         * Adds a new output channel to this builder.
         */
        fun add(outputStream: OutputChannel) = apply { outputStreams.add(outputStream) }
    }
}

/**
 * A buffered output channel that will store all the messages written to it.
 * This uses a [StringBuilder] to store the messages.
 *
 * @property buffer The buffer where the messages will be stored.
 * @constructor Creates a new buffered output channel.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
class BufferedOutputChannel : OutputChannel, Clearable {
    private val buffer = StringBuilder()

    override fun write(message: String) {
        buffer.append(message)
    }

    override fun toString() = buffer.toString()

    override fun clear() {
        buffer.clear()
    }
}

/**
 * Creates a new stdout output channel.
 *
 * @param builder The builder for this channel.
 * @return A new stdout output channel.
 */
fun Logger.stdoutChannel() = outputChannel.add(StdoutChannel())

/**
 * An output channel that will write to the standard output.
 *
 * @constructor Creates a new output channel that will write to the standard output.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
class StdoutChannel : OutputChannel {
    override fun write(message: String) {
        if (message.isNotBlank()) {
            println(message)
        }
    }
}

fun Logger.fileChannel(builder: FileOutputChannel.() -> Unit) =
    outputChannel.add(FileOutputChannel().also { builder(it) })

/**
 * An output channel that will write into a file.
 */
class FileOutputChannel : OutputChannel {
    var filename: String = "keen.log"

    override fun write(message: String) {
        if (message.isNotBlank()) {
            File(filename).appendText("$message${System.lineSeparator()}", Charsets.UTF_8)
        }
    }
}