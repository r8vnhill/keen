/*
 * Copyright (c) 2023, R8V.
 * BSD Zero Clause License.
 */


package cl.ravenhill.keen.genetic.genes

import cl.ravenhill.keen.IllegalOperationException


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
class NothingGene : Gene<Nothing, NothingGene> {

    /**
     * Always throws [IllegalOperationException] due to the inherent lack of DNA in a `NothingGene`.
     *
     * @return Never returns a value; always throws an exception.
     * @throws IllegalOperationException Indicates that a `NothingGene` does not possess DNA.
     */
    override val dna: Nothing
        get() = throw IllegalOperationException { "A NothingGene has no DNA" }

    /**
     * Always throws [IllegalOperationException] as assigning DNA to a `NothingGene` is not supported.
     *
     * @param dna A non-existent DNA value, which results in an exception being thrown.
     * @return Never returns a value; always throws an exception.
     * @throws IllegalOperationException Indicates that a `NothingGene` cannot be assigned DNA.
     */
    override fun withDna(dna: Nothing): NothingGene {
        throw IllegalOperationException { "A NothingGene cannot have DNA" }
    }
}

