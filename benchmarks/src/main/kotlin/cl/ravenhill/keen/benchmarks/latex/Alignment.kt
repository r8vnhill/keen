/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.benchmarks.latex

/**
 * Represents the concept of text alignment within a LaTeX document.
 *
 * This sealed interface is the base for different alignment types used in LaTeX tables and other formatted text areas.
 * Each implementation of this interface corresponds to a specific LaTeX alignment option.
 */
sealed interface Alignment {

    /**
     * Adds the given [Alignment] to the current [Alignment] and returns a list of [Alignment] objects.
     *
     * @param other The [Alignment] to be added.
     * @return A list of [Alignment] objects containing the current [Alignment] and the given [Alignment].
     */
    operator fun plus(other: Alignment): List<Alignment> = listOf(this, other)
}

/**
 * Represents center alignment in LaTeX.
 *
 * This object is used to specify that content should be centered horizontally. It is typically used in the context of
 * table columns or other structured layouts in LaTeX documents.
 *
 * Usage in LaTeX:
 * ```
 * \begin{tabular}{|c|...}
 *    ...
 * \end{tabular}
 * ```
 * Here, 'c' stands for center alignment in the column definition.
 */
data object Center : Alignment {
    override fun toString(): String = "c"
}

/**
 * Represents left alignment in LaTeX.
 *
 * This object is used to specify that content should be aligned to the left edge. It is commonly used in tables and
 * other text formatting contexts within LaTeX documents.
 *
 * Usage in LaTeX:
 * ```
 * \begin{tabular}{|l|...}
 *    ...
 * \end{tabular}
 * ```
 * Here, 'l' denotes left alignment for the column.
 */
data object Left : Alignment {
    override fun toString(): String = "l"
}

/**
 * Represents right alignment in LaTeX.
 *
 * This object is utilized to align content to the right edge. It finds its use in LaTeX tables and similar
 * formatting scenarios where right-aligned text is desired.
 *
 * Usage in LaTeX:
 * ```
 * \begin{tabular}{|r|...}
 *    ...
 * \end{tabular}
 * ```
 * In this example, 'r' is used to indicate right alignment for the column.
 */
data object Right : Alignment {
    override fun toString(): String = "r"
}

/**
 * Represents the pipe symbol used for vertical separation in LaTeX tables.
 *
 * This object is used to define vertical borders between columns in a LaTeX table. The pipe symbol (`|`) is a common
 * LaTeX notation used in table definitions to create vertical lines separating table columns.
 *
 * Usage in LaTeX:
 * ```
 * \begin{tabular}{|c|...}
 *    ...
 * \end{tabular}
 * ```
 * In this LaTeX table definition, the pipe symbols (`|`) are used to indicate vertical borders around a center-aligned
 * column (`c`). This object can be combined with other [Alignment] objects to define the layout of a LaTeX table.
 */
data object Pipe : Alignment {
    override fun toString(): String = "|"
}


/**
 * Creates a string representation of a list of [Alignment] objects, concatenating their string representations
 * without any separators.
 *
 * This extension function is particularly useful for generating a LaTeX table column alignment specification. Each
 * [Alignment] object in the list is converted to its string representation (e.g., "c" for [Center], "l" for [Left],
 * "r" for [Right], and "|" for [Pipe]), and these are concatenated in the order they appear in the list.
 *
 * Usage Example:
 * ```kotlin
 * val alignments = listOf(Left, Center, Right, Pipe, Center)
 * val latexAlignmentSpec = alignments.joinToString()
 * // latexAlignmentSpec will be "lcr|c"
 * ```
 * In this example, `joinToString()` is used to convert a list of [Alignment] objects into a string suitable for use
 * in a LaTeX tabular environment, resulting in a format specification of "lcr|c".
 *
 * @return A [String] representing the concatenated string representations of the [Alignment] objects in the list.
 */
fun List<Alignment>.joinToString() = joinToString("") { it.toString() }
