/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetics.chromosomes

import cl.ravenhill.jakt.Jakt.constraints
import cl.ravenhill.jakt.constraints.ints.BePositive
import cl.ravenhill.keen.genetics.genes.BooleanGene
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

class BooleanChromosomeFactory : AbstractChromosomeFactory<Boolean, BooleanGene>() {
    override fun invoke(size: Int, random: Random): Result<Chromosome<Boolean, BooleanGene>> = runCatching {
        constraints {
            "Cannot create a chromosome with a size less than 1" {
                size must BePositive
            }
        }
        runBlocking {
            BooleanChromosome(
                executor(size) { if (random.nextBoolean()) BooleanGene.True else BooleanGene.False }
            )
        }
    }
}
