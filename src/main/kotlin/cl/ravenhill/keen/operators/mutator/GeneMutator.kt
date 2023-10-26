/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.operators.mutator

import cl.ravenhill.keen.genetic.genes.Gene


/**
 * Represents a mutator that specializes in mutating individual genes.
 *
 * The [GeneMutator] interface provides functionality to apply mutations on single genes of a given type [G],
 * which encapsulates some form of genetic information represented by the type parameter [DNA].
 *
 * Implementations of this interface must specify how the mutations are to be applied and may use the [geneRate]
 * attribute to determine the likelihood or frequency of a particular gene mutation.
 *
 * @param DNA The type representing the genetic data or information.
 * @param G The type of gene that this mutator operates on, which holds [DNA] type data.
 *
 * @property geneRate A double value between 0.0 and 1.0 indicating the probability or rate at which a gene is mutated.
 *
 * @see Mutator
 * @see Gene
 *
 * @author <a href="https://www.github.com/r8vnhill">Ignacio Slater M.</a>
 * @since 2.0.0
 * @version 2.0.0
 */
interface GeneMutator<DNA, G> : Mutator<DNA, G> where G : Gene<DNA, G> {
    val geneRate: Double

    /**
     * Mutates the provided gene based on the implemented mutation strategy.
     *
     * @param gene The gene to be mutated.
     * @return [MutatorResult] encapsulating the original gene, the mutated gene, and any other mutation-related data.
     */
    fun mutateGene(gene: G): MutatorResult<DNA, G, G>
}
