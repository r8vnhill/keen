package cl.ravenhill.keen.evolution.config

import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.operators.alteration.Alterer

data class AlterationConfiguration<T, G>(val alterers: List<Alterer<T, G, Genotype<T, G>>>) where G : Gene<T, G>
