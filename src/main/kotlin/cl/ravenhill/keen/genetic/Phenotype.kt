package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.util.math.isNotNan

typealias Individual<DNA> = Phenotype<DNA>

class Phenotype<DNA>(
    val genotype: Genotype<DNA>,
    private val generation: Int,
    val fitness: Double = Double.NaN
) : GeneticMaterial {

    override fun verify(): Boolean = TODO()
    override fun toString() = "{ $genotype -> $fitness }"

    fun isEvaluated() = fitness.isNotNan()

    fun isNotEvaluated() = !isEvaluated()

    fun withFitness(fitness: Double) = Phenotype(genotype, generation, fitness)
}
