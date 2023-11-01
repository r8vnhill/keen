/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

@file:Suppress("MemberVisibilityCanBePrivate")

package cl.ravenhill.kuro

import cl.ravenhill.keen.InvalidStateException
import cl.ravenhill.keen.util.Clearable
import cl.ravenhill.keen.util.SelfReferential
import com.ibm.icu.impl.TextTrieMap.Output
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
 * An output channel to which messages can be written.
 *
 * An output channel is an entity that represents a target where a message can be written to.
 * Implementations of this interface define how messages are written to a specific output channel.
 *
 * @param T the concrete type of the output channel.
 *
 * @since 2.0.0
 * @version 2.0.0
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 */
interface OutputChannel<T : OutputChannel<T>> : Clearable<OutputChannel<T>>, SelfReferential<T> {

    /**
     * Writes a message to the output channel.
     *
     * @param message the message to be written to the output channel.
     * @return the same instance of the output channel that was used to write the message.
     */
    fun write(message: String): T

    /**
     * Adds a new output channel to this channel.
     *
     * @param outputChannel the output channel to add.
     * @return a result indicating if the operation was successful or not.
     * @throws InvalidStateException if the operation was not successful.
     */
    fun add(outputChannel: OutputChannel<*>): Result<Boolean> =
        Result.failure(InvalidStateException("${this::class.simpleName}") {
            "Cannot add channel to this channel."
        })

    /**
     * Clears the current state of the object to prepare it for reuse.
     * This function should be called before the object is used again, as it resets any properties
     * or resources that were used in the previous operation.
     *
     * This implementation of `clear` simply returns the object instance, indicating that it is now
     * in a cleared state without actually doing anything else.
     * Subclasses should override this function if they need to perform additional clearing logic.
     *
     * @return This object instance, indicating that it has been cleared and is ready for reuse.
     */
    override fun clear() = this
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
class CompositeOutputChannel(vararg outputChannels: OutputChannel<*>) :
        OutputChannel<CompositeOutputChannel>, Iterable<OutputChannel<*>> {

    /**
     * The output channels that this channel contains.
     */
    private val _outputChannels = outputChannels.toMutableList()
    val outputChannels: List<OutputChannel<*>>
        get() = _outputChannels

    /// Documentation inherited from [OutputChannel].
    override fun write(message: String): CompositeOutputChannel {
        _outputChannels.forEach { it.write(message) }
        return this
    }

    /// Documentation inherited from [OutputChannel].
    override fun add(outputChannel: OutputChannel<*>) =
        _outputChannels.add(outputChannel).let { Result.success(it) }

    /// Documentation inherited from [Iterable].
    override fun iterator() = _outputChannels.iterator()

    /**
     * Gets the output channel at the specified ``index``.
     */
    operator fun get(index: Int) = _outputChannels[index]
}

/**
 * Adds a new [BufferedOutputChannel] to the `outputChannel` of the logger.
 */
fun Logger.bufferedOutputChannel() = compositeChannel.add(BufferedOutputChannel())

/**
 * A buffered output channel that will store all the messages written to it.
 * This uses a [StringBuilder] to store the messages.
 *
 * @constructor Creates a new buffered output channel.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
class BufferedOutputChannel : OutputChannel<BufferedOutputChannel> {

    /**
     * The buffer where the messages will be stored.
     */
    private val buffer = StringBuilder()

    /// Documentation inherited from [OutputChannel].
    override fun write(message: String): BufferedOutputChannel {
        buffer.append(message)
        return this
    }

    /// Documentation inherited from [Clearable].
    override fun clear(): BufferedOutputChannel {
        buffer.clear()
        return this
    }

    /// Documentation inherited from [Any].
    override fun toString() = buffer.toString()
}

/**
 * Creates a new stdout output channel.
 *
 * @return A new stdout output channel.
 */
fun Logger.stdoutChannel() = compositeChannel.add(StdoutChannel())

/**
 * An output channel that will write to the standard output.
 *
 * @constructor Creates a new output channel that will write to the standard output.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 2.0.0
 */
class StdoutChannel : OutputChannel<StdoutChannel> {
    override fun write(message: String): StdoutChannel {
        if (message.isNotBlank()) {
            println(message)
        }
        return this
    }
}

fun Logger.fileChannel(builder: FileOutputChannel.() -> Unit) =
    compositeChannel.add(FileOutputChannel().also { builder(it) })

/**
 * An output channel that will write into a file.
 *
 * @property filename The name of the file where the messages will be written to.
 * @constructor Creates a new output channel that will write into a file.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
class FileOutputChannel : OutputChannel<FileOutputChannel> {
    var filename: String = "keen.log"
        set(value) {
            field = value
            file = File(value)
        }

    /**
     * The file where the messages will be written to.
     */
    private var file = File(filename)

    /// Documentation inherited from [OutputChannel].
    override fun write(message: String): FileOutputChannel {
        if (message.isNotBlank()) {
            file.appendText("$message${System.lineSeparator()}", Charsets.UTF_8)
        }
        return this
    }
}