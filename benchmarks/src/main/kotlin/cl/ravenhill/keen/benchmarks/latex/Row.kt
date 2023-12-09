/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.benchmarks.latex

data class Rule(val startColumn: Int, val endColumn: Int) {
    override fun toString() = "\\cline{$startColumn-$endColumn}"
}

data class Row(
    val cells: List<String>,
    val topRules: List<Rule> = emptyList(),
    val bottomRules: List<Rule> = emptyList(),
    val spacing: String? = null,
    val precision: Int = 2,
) {

}
