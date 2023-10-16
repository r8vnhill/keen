/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.genetic.genes.Gene


/**
 * An implementation of the [Chromosome] interface that provides a basic implementation of the
 * [toString] method.
 *
 * @param DNA The type of the genes' values.
 * @param G The type of the genes.
 *
 * @property genes The genes of the chromosome, ordered from start to end.
 * @property size The number of genes in the chromosome.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
abstract class AbstractChromosome<DNA, G : Gene<DNA, G>>(override val genes: List<G>) :
    Chromosome<DNA, G> {

    /**
     * Returns a string representation of the chromosome, consisting of a comma-separated list
     * of the genes' string representations enclosed in square brackets.
     *
     * Example: [Gene1, Gene2, Gene3].
     */
    override fun toString() = genes.joinToString(separator = ", ", prefix = "[", postfix = "]")
}
