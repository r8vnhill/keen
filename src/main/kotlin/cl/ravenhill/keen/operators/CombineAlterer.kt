package cl.ravenhill.keen.operators

import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.Phenotype
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import kotlin.math.min


/**
 * Alters a [Chromosome] by replacing two genes by the result of a given __*combiner*__ function.
 *
 * The [order] of this recombination implementation is two.
 *
 * @property combiner The function that combines two genes.
 * @param probability The probability of this recombination to be applied.
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 0.9
 * @version 0.9
 */
open class CombineAlterer<DNA>(
    private val combiner: (Gene<DNA>, Gene<DNA>) -> Gene<DNA>,
    probability: Double
) : AbstractRecombinatorAlterer<DNA>(probability, 2) {

    override fun recombine(
        population: MutableList<Phenotype<DNA>>,
        individuals: IntArray,
        generation: Int
    ): Int {
        val ind1 = population[individuals[0]]
        val ind2 = population[individuals[1]]
        val genotype1 = ind1.genotype
        val genotype2 = ind2.genotype
        val chromosomeIdx = Core.rng.nextInt(min(genotype1.size, genotype2.size))
        val chromosome1 = genotype1.chromosomes.toMutableList()
        val chromosome2 = genotype2.chromosomes
        val mean = combine(chromosome1[chromosomeIdx], chromosome2[chromosomeIdx])
        chromosome1[chromosomeIdx] = chromosome1[chromosomeIdx].duplicate(mean)
        population[individuals[0]] = Phenotype(genotype1.duplicate(chromosome1), generation)
        return 1
    }

    private fun combine(a: Chromosome<DNA>, b: Chromosome<DNA>) = List(a.size) {
        combiner(a[it], b[it])
    }
}