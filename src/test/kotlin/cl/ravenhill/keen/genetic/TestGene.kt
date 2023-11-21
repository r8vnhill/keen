/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.genetic

import cl.ravenhill.keen.genetic.genes.Gene


/**
 * Represents a test implementation of the [Gene] interface with [Int] as its DNA type.
 *
 * @property dna The genetic material represented by an integer value.
 */
class TestGene(override val dna: Int) : Gene<Int, TestGene> {

    /**
     * Creates a new instance of [TestGene] with the provided DNA.
     *
     * @param dna The genetic material for the new gene instance.
     * @return A new [TestGene] instance with the specified DNA.
     */
    override fun withDna(dna: Int) = TestGene(dna)
}
