package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.Core.enforce
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.requirements.CollectionRequirement.NotBeEmpty
import cl.ravenhill.keen.requirements.IntRequirement.BeInRange
import java.util.Objects


/**
 * A set of [Chromosome] objects that encodes a collection of genetic data.
 *
 * A `Genotype` is a collection of `Chromosome` objects that represents a complete set of genetic
 * information.
 * Each `Chromosome` in the `Genotype` contains a sequence of genetic data, which can represent
 * anything from simple binary values to complex data structures.
 * The `Genotype` provides a way to access and manipulate this genetic data as a whole.
 *
 * @param DNA The type of the data that the chromosomes encode.
 *     The `DNA` type parameter specifies the type of the data that each `Chromosome` encodes.
 *     This can be any type that implements the `GeneticMaterial` interface, which defines the
 *     necessary methods for working with genetic data.
 *
 * @property chromosomes A list of chromosomes in the genotype.
 *     The `chromosomes` property is a read-only list of `Chromosome` objects that make up the
 *     `Genotype`.
 *     This list can be iterated over to access individual `Chromosome` objects, or it can be
 *     manipulated as a whole using the various methods provided by the `Genotype` class.
 */
class Genotype<DNA>(val chromosomes: List<Chromosome<DNA>>) : GeneticMaterial<DNA>,
        Iterable<Chromosome<DNA>> {

    init {
        enforce {
            chromosomes should NotBeEmpty()
        }
    }

    val size: Int = chromosomes.size

    // Inherit documentation from Verifyable
    override fun verify() = chromosomes.isNotEmpty() && chromosomes.all { it.verify() }

    // Inherit documentation from GeneticMaterial
    override fun flatten() =
        chromosomes.fold(mutableListOf<DNA>()) { acc, chromosome ->
            acc.apply { addAll(chromosome.flatten()) }
        }

    /**
     * Creates a new [Genotype] by duplicating the given list of [Chromosome] objects.
     */
    @Deprecated(
        "Use the copy constructor instead",
        ReplaceWith("Genotype(chromosomes)", "cl.ravenhill.keen.genetic.Genotype")
    )
    fun duplicate(chromosomes: List<Chromosome<DNA>>) = Genotype(chromosomes)

    /**
     * Returns the [Chromosome] at the given `index`.
     */
    operator fun get(index: Int): Chromosome<DNA> {
        enforce { index should BeInRange(0 until size) }
        return chromosomes[index]
    }

    // Inherit documentation from Iterable
    override fun iterator() = chromosomes.iterator()

    // region : toString, equals, hashCode
    // Inherit documentation from Any
    override fun equals(other: Any?) = when {
        this === other -> true
        other !is Genotype<*> -> false
        chromosomes != other.chromosomes -> false
        else -> true
    }

    // Inherit documentation from Any
    override fun hashCode() = Objects.hash(Genotype::class, chromosomes)

    // Inherit documentation from Any
    override fun toString() = " [ ${chromosomes.joinToString(" | ")} ] "
    // endregion

    /**
     * A builder for creating [Genotype] instances with a set of chromosomes.
     *
     * @param DNA The type of the data that the chromosomes encode.
     *
     * @property chromosomes The list of chromosome factories added to this builder.
     */
    class Factory<DNA> {

        lateinit var chromosomes: MutableList<Chromosome.Factory<DNA>>

        /**
         * Adds a chromosome factory to the builder.
         *
         * @param lazyFactory A function that returns a [Chromosome.Factory] instance for the
         *  chromosome.
         */
        fun chromosome(lazyFactory: () -> Chromosome.Factory<DNA>) {
            if (!this::chromosomes.isInitialized) {
                chromosomes = mutableListOf()
            }
            chromosomes.add(lazyFactory())
        }

        /**
         * Creates a new [Genotype] instance with the chromosomes added to the builder.
         */
        fun make(): Genotype<DNA> {
            enforce {
                requirement("Chromosomes should be initialized") {
                    this@Factory::chromosomes.isInitialized
                }
                if (this@Factory::chromosomes.isInitialized) {
                    chromosomes should NotBeEmpty()
                }
            }
            return Genotype(chromosomes.map { it.make() })
        }

        // Inherit documentation from Any
        override fun toString() = "GenotypeBuilder { " +
                "chromosomes: $chromosomes }"
    }
}
