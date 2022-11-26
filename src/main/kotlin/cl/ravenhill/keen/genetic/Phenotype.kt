package cl.ravenhill.keen.genetic


class Phenotype<DNA>(val genotype: Genotype<DNA>, generation: Int) : GeneticMaterial {
    private var fitness: Double? = null

    override fun verify(): Boolean = TODO()
    override fun toString() = "{ $genotype -> ${genotype.fitness} }"

    fun isEvaluated() = fitness != null

    fun isNotEvaluated() = !isEvaluated()
}