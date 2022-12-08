package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.util.math.isNotNan

/**
 * An individual of a population.
 *
 * A ``Phenotype`` is a collection of genetic material that represents a solution to a problem.
 *
 * @param DNA  The type of the phenotype's value.
 * @property genotype  The genotype of the phenotype.
 * @property generation The generation of the phenotype.
 * @property fitness The fitness of the phenotype.
 *
 * @constructor Creates a new phenotype with the given genotype, generation and fitness.
 */
open class Phenotype<DNA>(
    open val genotype: Genotype<DNA>,
    private val generation: Int,
    open val fitness: Double = Double.NaN
) : GeneticMaterial<DNA> {

    override fun verify(): Boolean = TODO()

    override fun toString() = "{ $genotype -> $fitness }"

    /**
     * Returns ``true`` if the fitness of the phenotype has been evaluated.
     */
    open fun isEvaluated() = fitness.isNotNan()

    /**
     * Returns ``true`` if the fitness of the genotype has not been evaluated.
     */
    open fun isNotEvaluated() = !isEvaluated()

    /**
     * Creates a new phenotype with a given fitness using the same genotype and generation.
     */
    open fun withFitness(fitness: Double) = Phenotype(genotype, generation, fitness)

    override fun flatten() = genotype.flatten()
}
