/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.benchmarks.latex


private fun formatNumber(number: Number): String {
    val (a, b) = "%e".format(number).split("e")

    return buildString {
        append("$")
        append(formatDecimal(a, 2))
        append(" \\times 10^{")
        append(formatExponent(b))
        append("}$")
    }
}

private fun formatExponent(e: String): String = when {
    e.length == 1 -> e
    e.startsWith("+") -> formatExponent(e.drop(1))
    e.startsWith("-") -> "-${formatExponent(e.drop(1))}"
    e.startsWith("0") -> formatExponent(e.drop(1))
    else -> e
}

/**
 * Formats a decimal number as a string, truncating the fractional part to a specified length.
 *
 * @param n The decimal number in string format to be formatted. It should be in the form of "a.b", where `a` is the
 *   integer part and `b` is the fractional part.
 * @param truncate The maximum length of the fractional part after truncation.
 * @return A string representing the formatted decimal number.
 */
fun formatDecimal(n: String, truncate: Int): String {
    val (a, b) = n.split(".")
    return when {
        b.length > truncate -> "$a.${b.take(truncate)}"
        else -> "$a.$b"
    }
}

/**
 * Formats data into a LaTeX cell format with optional alignment and multicolumn support.
 *
 * This function takes any data type and formats it into a string suitable for use in a LaTeX table cell.
 * Special formatting is applied to numbers, which are converted to scientific notation. The function also
 * supports specifying the alignment of the cell content and can extend the cell across multiple columns.
 *
 * ## Functionality:
 * - For `Number` types, the data is formatted in scientific notation (e.g., "x.xx × 10^y").
 * - For other data types, `toString()` is used for conversion.
 * - Supports multicolumn cells in LaTeX by using the `\\multicolumn` command.
 *
 * ### Example:
 * ```kotlin
 * println(cell(12345.6789, listOf(Alignment.CENTER)))
 * // Output: "1.234568e+04 × 10^4" (in scientific notation)
 *
 * println(cell("Sample Text", listOf(Alignment.LEFT, Alignment.RIGHT), 2))
 * // Output: "\\multicolumn{2}{LEFT,RIGHT}{Sample Text}" (as a multicolumn cell with specified alignments)
 * ```
 *
 * @param data The data to be formatted into the cell. It can be of any type.
 * @param alignment A list of `Alignment` values specifying the cell's alignment.
 *                  This is used when the cell spans multiple columns.
 * @param length The number of columns the cell should span. Default is 1 (single column).
 * @return A `String` representing the formatted cell content, ready to be used in a LaTeX table.
 */
fun cell(data: Any, length: Int = 1, vararg alignment: Alignment): String {
    val formattedData = when (data) {
        is Number -> formatNumber(data)

        else -> data.toString()
    }
    return when (length) {
        1 -> formattedData
        else -> "\\multicolumn{$length}{${alignment.joinToString()}}{$formattedData}"
    }
}
