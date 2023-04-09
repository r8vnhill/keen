package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.Core

/**
 * A boolean gene.
 */
sealed class BoolGene : Gene<Boolean, BoolGene> {

    /**
     * Returns the boolean value of the gene.
     */
    fun toBool() = dna

    /**
     * Returns the integer value of the gene.
     */
    fun toInt() = if (dna) 1 else 0

    override fun toString() = "$dna"

    override fun mutate() = super.mutate()

    override fun generator() = Core.random.nextBoolean()

    override fun withDna(dna: Boolean) = if (dna) {
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