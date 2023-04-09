package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.genetic.GeneticMaterial
import cl.ravenhill.keen.util.SelfReferential

/**
 * Represents a gene, which is a unit of heredity that encodes a single trait of an organism.
 *
 * @property dna The gene's value.
 * @constructor Creates a new [Gene] instance with the given [dna].
 *
 * @author <a href="https://www.github.com/r8vnhill">R8V</a>
 * @since 1.0.0
 * @version 2.0.0
 */
interface Gene<DNA, G: Gene<DNA, G>> : GeneticMaterial<DNA, G>, SelfReferential<G> {

    val dna: DNA

    /**
     * Creates a new gene with a mutated value.
     */
    fun mutate(): G = withDna(generator())

    /**
     * Generates a new random value for this gene.
     */
    fun generator(): DNA = dna

    /**
     * Creates a new gene with the given value.
     *
     * @param dna The value for the new gene.
     * @return A new [Gene] instance with the given [dna] value.
     */
    fun withDna(dna: DNA): G

    // Documentation inherited from GeneticMaterial
    override fun flatten() = listOf(dna)
}