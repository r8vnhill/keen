package cl.ravenhill.keen.ga.primefact

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ga.primefact.PrimeFactorizationProblem.candidateFactors
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.chromosomes.numeric.IntChromosome
import cl.ravenhill.keen.genetic.genes.numeric.IntGene
import cl.ravenhill.keen.operators.alteration.mutation.GeneMutator

private const val GENE_RATE = 0.3
private const val INDIVIDUAL_RATE = 0.3
private const val CHROMOSOME_RATE = 0.3

/**
 * An object that implements the `GeneMutator` interface for mutating genes within a chromosome in the context of
 * prime factorization.
 *
 * `PrimeMutator` provides methods to mutate individual genes and entire chromosomes, ensuring that the mutated genes
 * are prime numbers. This mutator is used within a genetic algorithm to introduce genetic diversity and explore new
 * solutions.
 */
object PrimeMutator : GeneMutator<Int, IntGene> {

    /**
     * Mutates a single gene by replacing it with a randomly selected prime number from the candidate factors.
     *
     * @param gene the gene to be mutated
     * @return a new `IntGene` instance with a randomly selected prime number
     */
    override fun mutateGene(gene: IntGene) = IntGene(candidateFactors.random(Domain.random))

    /**
     * Mutates an entire chromosome by applying the `mutateGene` method to each gene in the chromosome.
     *
     * @param chromosome the chromosome to be mutated
     * @return a new `IntChromosome` instance with mutated genes
     */
    override fun mutateChromosome(chromosome: Chromosome<Int, IntGene>) =
        IntChromosome(chromosome.map { mutateGene(it) })

    /**
     * The mutation rate for individual genes.
     */
    override val geneRate = GENE_RATE

    /**
     * The mutation rate for individuals.
     */
    override val individualRate = INDIVIDUAL_RATE

    /**
     * The mutation rate for chromosomes.
     */
    override val chromosomeRate = CHROMOSOME_RATE
}
