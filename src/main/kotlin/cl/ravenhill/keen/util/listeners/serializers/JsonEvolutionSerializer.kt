/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.util.listeners.serializers

import cl.ravenhill.keen.genetic.genes.Gene
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.time.ExperimentalTime


/**
 * A serializer for evolution processes with JSON formatting.
 *
 * The `JsonEvolutionSerializer` class facilitates the conversion of evolution processes
 * to a JSON formatted string. The serialized JSON output includes details about the initialization
 * and the associated generations of the evolution process.
 *
 * The class uses the kotlinx.serialization library's `Json` format with pretty-print enabled
 * to produce human-readable JSON output.
 *
 * This class should be used in scenarios where the evolution results need to be stored
 * or transmitted in a JSON format.
 *
 * @param DNA The type of the entities being evolved.
 * @param G The gene type associated with the entities.
 *
 * @constructor Inherits functionalities from [AbstractEvolutionSerializer] and
 *              initializes a JSON format with pretty-printing.
 *
 * @property json A configured `Json` instance for serializing the evolution data.
 *
 * @see AbstractEvolutionSerializer
 *
 * @since 2.0.0
 * @version 2.0.0
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 */
@ExperimentalTime
class JsonEvolutionSerializer<DNA, G : Gene<DNA, G>> : AbstractEvolutionSerializer<DNA, G>() {

    private val json = Json {
        prettyPrint = true
    }

    /**
     * Converts the evolution process to a JSON formatted string.
     *
     * @return A string representation of the serialized evolution process in JSON format.
     */
    override fun toString() = "{\n" +
            indentText(
                "\"initialization\": ${json.encodeToString(evolution.initialization)},\n",
                4
            ) + indentText("\"generations\": ${json.encodeToString(generations)}\n", 4) +
            "}"

    /**
     * Saves the serialized evolution process to a file in JSON format.
     *
     * @param file The target file where the serialized JSON will be written.
     */
    fun saveToFile(file: File) {
        file.writeText(toString())
    }
}

/**
 * Indents a given text by prefixing each line with a specified number of spaces.
 *
 * This utility function assists in adding indentation to multi-line strings for better readability.
 *
 * @param text The string/text that needs to be indented.
 * @param spaces The number of spaces to prefix each line for indentation.
 *
 * @return The indented text.
 */
private fun indentText(text: String, spaces: Int): String {
    val indentation = " ".repeat(spaces)
    return text.lines().joinToString("\n") { "$indentation$it" }
}