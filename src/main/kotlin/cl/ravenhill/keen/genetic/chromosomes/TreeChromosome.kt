package cl.ravenhill.keen.genetic.chromosomes

import cl.ravenhill.keen.genetic.genes.Gene

/**
 * [Chromosome] for tree based [Gene]s.
 *
 * @param DNA   The type of the genes' values.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface TreeChromosome<DNA : Any> : Chromosome<DNA>