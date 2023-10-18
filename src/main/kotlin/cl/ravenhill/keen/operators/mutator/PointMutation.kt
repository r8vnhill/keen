package cl.ravenhill.keen.operators.mutator

import cl.ravenhill.enforcer.Enforcement.enforce
import cl.ravenhill.enforcer.requirements.DoubleRequirement
import cl.ravenhill.keen.Core
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.eq
import cl.ravenhill.keen.util.trees.Tree

class PointMutation<V, DNA : Tree<V, DNA>, G : Gene<DNA, G>>(
    override val probability: Double,
    val geneProbability: Double
) : Mutator<DNA, G> {

    init {
        enforce {
            "Mutation probabilities must be between 0.0 and 1.0" {
                probability must DoubleRequirement.BeInRange(0.0..1.0)
            }
        }
    }

    override fun mutateChromosome(chromosome: Chromosome<DNA, G>): MutatorResult<DNA, G, Chromosome<DNA, G>> {
        TODO()
//        val result = chromosome.genes.map { mutateGene(it) }
//        return MutatorResult(
//            chromosome.withGenes(result.map { it.mutated }),
//            result.sumOf { it.mutations }
//        )
    }

    private fun mutateGene(gene: G) = when {
        geneProbability eq 0.0 -> MutatorResult(gene)
        geneProbability eq 1.0 || Core.random.nextDouble() < geneProbability -> {
            val dna = gene.dna


        }
        else -> MutatorResult(gene)
    }
}