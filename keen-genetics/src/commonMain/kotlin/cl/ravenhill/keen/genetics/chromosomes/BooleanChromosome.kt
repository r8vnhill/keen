/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetics.chromosomes

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cl.ravenhill.jakt.constrainedTo
import cl.ravenhill.jakt.constraints.collections.BeEmpty
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.ToStringMode.DEFAULT
import cl.ravenhill.keen.ToStringMode.SIMPLE
import cl.ravenhill.keen.exceptions.InitializationException
import cl.ravenhill.keen.genetics.genes.BooleanGene
import cl.ravenhill.keen.utils.roundUpToMultipleOf

/**
 * The size of each chunk in the binary string representation of a `BooleanChromosome`.
 *
 * The value of `CHUNK_SIZE` is set to 4, meaning that every four genes will be grouped together in the string
 * representation, separated by spaces.
 */
private const val CHUNK_SIZE = 4

class BooleanChromosome private constructor(override val genes: List<BooleanGene>) : Chromosome<Boolean, BooleanGene> {

    override fun copyWithGenes(newGenes: List<BooleanGene>) = BooleanChromosome(newGenes)

    override fun toString(): String {
        when (Domain.toStringMode) {
            SIMPLE -> {
                val stringSize = size roundUpToMultipleOf CHUNK_SIZE
                val paddingZeroes = "0".repeat(stringSize - size)
                return (paddingZeroes + joinToString("") { if (it.value) "1" else "0" })
                    .chunked(CHUNK_SIZE).joinToString(" ")
            }

            DEFAULT -> return "BooleanChromosome(genes=$genes)"
        }
    }

    companion object {
        operator fun invoke(genes: List<BooleanGene>): Either<InitializationException, BooleanChromosome> =
            genes.constrainedTo {
                "The list of genes must not be empty" { it mustNot BeEmpty }
            }.fold(
                { InitializationException("Failed to create BooleanChromosome", it).left() },
                { BooleanChromosome(it).right() }
            )
    }
}
