package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.util.math.isNotNan

open class Phenotype<DNA>(
    open val genotype: Genotype<DNA>,
    private val generation: Int,
    open val fitness: Double = Double.NaN
) : GeneticMaterial<DNA> {

    override fun verify(): Boolean = TODO()
    override fun toString() = "{ $genotype -> $fitness }"

    open fun isEvaluated() = fitness.isNotNan()

    open fun isNotEvaluated() = !isEvaluated()

    open fun withFitness(fitness: Double) = Phenotype(genotype, generation, fitness)

    override fun flatten() = genotype.flatten()
}
