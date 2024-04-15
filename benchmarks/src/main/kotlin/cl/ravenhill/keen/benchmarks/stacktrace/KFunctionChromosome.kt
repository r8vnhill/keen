/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.benchmarks.stacktrace

import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import kotlin.reflect.KFunction


data class KFunctionChromosome(override val genes: List<KFunctionGene>) : Chromosome<KFunction<*>, KFunctionGene> {
    override fun duplicateWithGenes(genes: List<KFunctionGene>) = KFunctionChromosome(genes)
    override fun toString() = genes.joinToString("\n")
    class Factory(override var size: Int, val geneFactory: () -> KFunctionGene) :
        Chromosome.AbstractFactory<KFunction<*>, KFunctionGene>() {
        override fun make() = KFunctionChromosome((0..<size).map { geneFactory() })
    }
}
