/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.engines

import cl.ravenhill.keen.evolution.engines.Evolver
import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.genes.Gene

abstract class AbstractGeneBasedAlgorithm<T, G> : Evolver<T, G, Genotype<T, G>> where G : Gene<T, G>
