/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.genetic.genes.Gene
import cl.ravenhill.keen.util.isNotNan
import java.util.*

/**
 * Represents a individual, which is a combination of a genotype (collection of genetic data)
 * and its associated fitness.
 * A individual can be evaluated and compared to other individuals.
 *
 * @property genotype The genotype associated with the individual.
 * @property fitness The fitness associated with the individual. Defaults to `Double.NaN`.
 * @property size The size of the genotype.
 *
 * @constructor Creates a new [Individual] instance with the given [genotype] and [fitness].
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @version 2.0.0
 * @since 1.0.0
 */
data class Individual<DNA, G : Gene<DNA, G>>(
    val genotype: Genotype<DNA, G>,
    val fitness: Double = Double.NaN
) : GeneticMaterial<DNA, G>, Comparable<Individual<*, *>> {

    val size: Int = genotype.size

    // Inherit documentation from Verifyable
    override fun verify(): Boolean = genotype.verify() && fitness.isNotNan()

    // Inherit documentation from Comparable
    override fun compareTo(other: Individual<*, *>) =
        this.fitness compareTo other.fitness

    /**
     * Returns a Boolean indicating whether the fitness of the individual has been evaluated.
     *
     * @return `true` if the fitness has been evaluated, `false` otherwise.
     */
    fun isEvaluated() = fitness.isNotNan()

    /**
     * Returns `true` if the fitness of the individual has not been evaluated.
     */
    fun isNotEvaluated() = !isEvaluated()

    /**
     * Creates a new [Individual] instance with the given fitness value.
     *
     * @param fitness The fitness value of the individual.
     * @return A new [Individual] instance with the given fitness value.
     */
    fun withFitness(fitness: Double) = Individual(genotype, fitness)

    /**
     * Creates a new [Individual] instance with the given genotype and fitness values.
     * The generation of the new individual will be the same as the current one.
     *
     * @param candidate The genotype of the new individual.
     * @param fitness The fitness value of the new individual.
     * @return A new [Individual] instance with the given genotype and fitness values.
     */
    fun withGenotype(candidate: Genotype<DNA, G>, fitness: Double) =
        Individual(candidate, fitness)

    // Inherit documentation from GeneticMaterial
    override fun flatMap(transform: (DNA) -> DNA) = genotype.flatMap(transform)

    override fun toSimpleString() = "${genotype.toSimpleString()} -> $fitness"

    override fun toString() =
        "Individual(genotype=${genotype.toSimpleString()}, fitness=$fitness)"

    override fun equals(other: Any?) = when {
        this === other -> true
        other !is Individual<*, *> -> false
        genotype != other.genotype -> false
        else -> true
    }

    override fun hashCode() = Objects.hash(Individual::class, genotype)
}
