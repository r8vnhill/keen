/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetics.chromosomes

import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.mixins.FlatMappable
import cl.ravenhill.keen.repr.Representation

interface Chromosome<T, G> : Representation<T, G>, FlatMappable<T> where G : Gene<T, G>
