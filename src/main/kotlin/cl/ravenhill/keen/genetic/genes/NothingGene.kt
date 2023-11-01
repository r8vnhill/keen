/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.IllegalOperationException
import cl.ravenhill.keen.genetic.genes.NothingGene.dna
import cl.ravenhill.keen.util.MultiStringFormat


/**
 * Represents a gene that intentionally lacks genetic information, typically used as a placeholder.
 *
 * The `NothingGene` is a specialized gene that signifies the absence of genetic information.
 * This gene is designed not to hold any DNA, and any attempt to access or modify its DNA will
 * result in an [IllegalOperationException].
 *
 * This class can be useful in scenarios where a gene's presence is necessary for typing or
 * architectural reasons, but where it's explicitly intended to remain devoid of information.
 *
 * @property dna Accessing this property will always throw [IllegalOperationException],
 *               indicating that the `NothingGene` does not possess DNA.
 */
data object NothingGene : Gene<Nothing, NothingGene>, MultiStringFormat {

    /**
     * Always throws [IllegalOperationException] due to the inherent lack of DNA in a `NothingGene`.
     *
     * @return Never returns a value; always throws an exception.
     * @throws IllegalOperationException Indicates that a `NothingGene` does not possess DNA.
     */
    override val dna: Nothing
        get() = throw IllegalOperationException { "A NothingGene has no DNA" }

    /**
     * Absurd method; inaccessible due to the inherent lack of DNA in a `NothingGene`.
     *
     * The exception will never be thrown, as the ``withDna`` method cannot be called with a
     * [Nothing] value.
     */
    override fun withDna(dna: Nothing) =
        throw IllegalOperationException { "A NothingGene cannot have DNA" }

    override fun toString() = "NothingGene"

    override fun toSimpleString() = toString()

    override fun toFullString() = toString()
}

