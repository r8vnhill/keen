package cl.ravenhill.keen.genetic


class Phenotype<DNA>(private val genotype: Genotype<DNA>, generation: Int) : GeneticMaterial {
    override fun verify(): Boolean = TODO()
    override fun toString() = "{ $genotype -> ${genotype.fitness} }"
}