package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.Core

/**
 * A boolean gene.
 */
sealed class BoolGene : Gene<Boolean> {

    /**
     * Returns the boolean value of the gene.
     */
    fun toBool() = dna

    /**
     * Returns the integer value of the gene.
     */
    fun toInt() = if (dna) 1 else 0

    override fun toString() = "$dna"

    override fun mutate() = super.mutate() as BoolGene

    override fun generator() = Core.rng.nextBoolean()

    override fun duplicate(dna: Boolean) = if (dna) {
        True
    } else {
        False
    }

    /**
     * A true gene.
     */
    object True : BoolGene() {
        override val dna = true
    }

    /**
     * A false gene.
     */
    object False : BoolGene() {
        override val dna = false
    }
}