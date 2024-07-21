package cl.ravenhill.keen.ga.primefact

import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ga.primefact.PrimeFactorizationProblem.candidateFactors
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.chromosomes.numeric.IntChromosome
import cl.ravenhill.keen.genetic.genes.numeric.IntGene

/**
 * A factory class for creating prime number chromosomes in an evolutionary algorithm context.
 *
 * `PrimeChromosomeFactory` extends `Chromosome.AbstractFactory` and is responsible for generating chromosomes
 * composed of prime number genes. This factory produces `IntChromosome` instances where each gene is a randomly
 * selected prime number.
 *
 * @param size the number of genes in the chromosome
 * @property size the number of genes in the chromosome
 */
class PrimeChromosomeFactory(override var size: Int) : Chromosome.AbstractFactory<Int, IntGene>() {

    /**
     * Creates a chromosome with genes that are randomly selected prime numbers.
     *
     * @return an `IntChromosome` instance with prime number genes
     */
    override fun make() =
        IntChromosome((0..<size).map { IntGene(candidateFactors.random(Domain.random)) })
}
