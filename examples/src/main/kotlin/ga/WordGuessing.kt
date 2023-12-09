package ga

import cl.ravenhill.keen.genetic.Genotype

private const val TARGET = "Sopaipilla"

private fun matches(genotype: Genotype<Char, CharGene>) = genotype.flatMap()
    .filterIndexed { index, char -> char == TARGET[index] }
    .size.toDouble()
