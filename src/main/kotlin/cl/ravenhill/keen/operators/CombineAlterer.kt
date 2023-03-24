package cl.ravenhill.keen.operators

import cl.ravenhill.keen.Core.Dice
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.probability


/**
 * Alters a [Chromosome] by replacing two genes by the result of a given __*combiner*__ function.
 *
 * The [numOut] of this recombination implementation is two.
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
    probability: Double,
    chromosomeRate: Double = 0.5,
    private val geneRate: Double = 1.0
) : AbstractRecombinatorAlterer<DNA>(probability, 1, chromosomeRate = chromosomeRate) {

//    override fun recombine(
//        population: MutableList<Phenotype<DNA>>,
//        indices: List<Int>,
//        generation: Int
//    ): Int {
////        val ind1 = population[indices]
////        val ind2 = population[Dice.int(population.size)]
////        val genotype1 = ind1.genotype
////        val genotype2 = ind2.genotype
////        val chromosomeIdx = Dice.int(min(genotype1.size, genotype2.size))
////        val chromosome1 = genotype1.chromosomes.toMutableList()
////        val chromosome2 = genotype2.chromosomes
////        val mean = combine(chromosome1[chromosomeIdx], chromosome2[chromosomeIdx])
////        chromosome1[chromosomeIdx] = chromosome1[chromosomeIdx].duplicate(mean)
////        population[indices] = Phenotype(genotype1.duplicate(chromosome1), generation)
//        return 1
//    }

    /**
     * Returns a list of the genes that are the result of the combination of the genes of
     * the given chromosomes.
     */
    internal fun combine(a: Chromosome<DNA>, b: Chromosome<DNA>) = List(a.size) {
        if (Dice.probability() < chromosomeRate) {
            combiner(a[it], b[it])
        } else {
            a[it]
        }
    }

    override fun recombineChromosomes(chromosomes: List<Chromosome<DNA>>): List<Chromosome<DNA>> {
        val a = chromosomes[0]
        val b = chromosomes[1]
        // val combined = combiner()
        TODO("Not yet implemented")
    }
}