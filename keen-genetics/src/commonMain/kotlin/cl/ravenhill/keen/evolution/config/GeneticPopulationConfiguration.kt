package cl.ravenhill.keen.evolution.config

import cl.ravenhill.keen.genetics.Genotype
import cl.ravenhill.keen.genetics.GenotypeFactory
import cl.ravenhill.keen.genetics.genes.Gene

data class GeneticPopulationConfiguration<T, G>(
    val genotypeFactory: GenotypeFactory<T, G>,
    val populationSize: Int
) where G : Gene<T, G>
