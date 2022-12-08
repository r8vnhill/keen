/*
 * "Keen" (c) by R8V.
 * "Keen" is licensed under a
 * Creative Commons Attribution 4.0 International License.
 * You should have received a copy of the license along with this
 *  work. If not, see <https://creativecommons.org/licenses/by/4.0/>.
 */


package cl.ravenhill.keen.genetic.genes.numerical

import cl.ravenhill.keen.Core.rng
import cl.ravenhill.keen.genetic.chromosomes.numerical.IntChromosome
import cl.ravenhill.keen.genetic.genes.ComparableGene
import kotlin.random.asKotlinRandom

/**
 * [NumberGene] which holds a 32-bit integer number.
 *
 * @property dna The value of the gene.
 * @property range The range of the gene.
 * @property filter A filter function to apply to the gene's value.
 *
 * @see IntChromosome
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 */
class IntGene(
    override val dna: Int,
    private val range: IntRange,
    private val filter: (Int) -> Boolean
) : NumberGene<Int>, ComparableGene<Int> {

    // http://aggregate.org/MAGIC/#Average%20of%20Integers
    override fun mean(gene: NumberGene<Int>) =
        duplicate((gene.dna and dna) + ((gene.dna xor dna) shr 1))

    override fun toDouble() = dna.toDouble()
    override fun toInt(): Int {
        TODO("Not yet implemented")
    }

    override fun mutate() =
        duplicate(range.filter { filter(it) }.random(rng))

    override fun duplicate(dna: Int) = IntGene(dna, range, filter)

    override fun verify() = dna in range && filter(dna)

    override fun toString() = "$dna"
}