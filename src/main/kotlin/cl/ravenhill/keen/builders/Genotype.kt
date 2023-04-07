/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 * work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.builders

import cl.ravenhill.keen.genetic.Genotype
import cl.ravenhill.keen.genetic.chromosomes.Chromosome

class GenotypeScope<DNA> {
    val chromosomes = mutableListOf<Chromosome.Factory<DNA>>()
}

/**
 * Creates a new [Genotype] with the given [init] block.
 *
 * __Usage:__
 * ```
 * genotype {
 *     chromosome {
 *         booleans {
 *             size = 20
 *             truesProbability = 0.15
 *         }
 *     }
 * }
 * ```
 *
 * @param init A lambda block that allows configuring the genotype by specifying its chromosomes.
 *
 * @return A `Genotype` instance that contains the specified chromosomes.
 *
 * @see Genotype
 */
fun <DNA> genotype(init: GenotypeScope<DNA>.() -> Unit) =
    Genotype.Factory<DNA>().apply {
        chromosomes.addAll(GenotypeScope<DNA>().apply(init).chromosomes)
    }