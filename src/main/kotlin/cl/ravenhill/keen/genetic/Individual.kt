/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.util.math.isNotNan


class Individual<DNA>(
    override val genotype: Genotype<DNA>,
    private val generation: Int,
    override val fitness: Double = Double.NaN
) : Phenotype<DNA>(genotype, generation, fitness) {

    override fun verify(): Boolean = TODO()
    override fun toString() = "{ $genotype -> $fitness }"

    override fun isEvaluated() = fitness.isNotNan()

    override fun isNotEvaluated() = !isEvaluated()

    override fun withFitness(fitness: Double) = Individual(genotype, generation, fitness)

    override fun flatten() = genotype.flatten()
}