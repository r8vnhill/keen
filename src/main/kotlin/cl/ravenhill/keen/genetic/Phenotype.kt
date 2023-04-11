package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.isNotNan
import java.util.*

/**
 * Represents a phenotype, which is a combination of a genotype (collection of genetic data)
 * and its associated fitness.
 * A phenotype can be evaluated and compared to other phenotypes.
 *
 * @property genotype The genotype associated with the phenotype.
 * @property generation The generation in which the phenotype occurred.
 * @property fitness The fitness associated with the phenotype. Defaults to `Double.NaN`.
 *
 * @constructor Creates a new [Phenotype] instance with the given [genotype], [generation] and
 * [fitness].
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 1.0.0
 */
class Phenotype<DNA, G: Gene<DNA, G>>(
    val genotype: Genotype<DNA, G>,
    val generation: Int,
    val fitness: Double = Double.NaN
) : GeneticMaterial<DNA, G>, Comparable<Phenotype<DNA, G>> {

    // Inherit documentation from Verifyable
    override fun verify(): Boolean = genotype.verify() && fitness.isNotNan()

    // Inherit documentation from Comparable
    override fun compareTo(other: Phenotype<DNA, G>) =
        this.fitness compareTo other.fitness

    // Inherit documentation from Any
    override fun toString() = "{ $genotype -> $fitness }"

    /**
     * Returns a Boolean indicating whether the fitness of the phenotype has been evaluated.
     *
     * @return `true` if the fitness has been evaluated, `false` otherwise.
     */
    fun isEvaluated() = fitness.isNotNan()

    /**
     * Returns `true` if the fitness of the phenotype has not been evaluated.
     */
    fun isNotEvaluated() = !isEvaluated()

    /**
     * Creates a new [Phenotype] instance with the given fitness value.
     *
     * @param fitness The fitness value of the phenotype.
     * @return A new [Phenotype] instance with the given fitness value.
     */
    fun withFitness(fitness: Double) = Phenotype(genotype, generation, fitness)


    // Inherit documentation from GeneticMaterial
    override fun flatten() = genotype.flatten()

    // Inherit documentation from Any
    override fun equals(other: Any?) = other is Phenotype<*, *> && genotype == other.genotype

    // Inherit documentation from Any
    override fun hashCode() = Objects.hash(Phenotype::class, genotype)
}
