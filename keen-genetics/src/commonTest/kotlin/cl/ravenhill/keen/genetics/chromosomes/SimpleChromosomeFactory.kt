/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetics.chromosomes

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cl.ravenhill.keen.evolution.executors.construction.ConstructorExecutor
import cl.ravenhill.keen.evolution.executors.construction.CoroutineConcurrentConstructor
import cl.ravenhill.keen.evolution.executors.construction.SequentialConstructor
import cl.ravenhill.keen.exceptions.InitializationException
import cl.ravenhill.keen.genetics.genes.SimpleGene
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.element
import io.kotest.property.arbitrary.int

/**
 * A factory for creating simple chromosomes composed of integer-based genes.
 */
class SimpleChromosomeFactory : AbstractChromosomeFactory<Int, SimpleGene>() {

    /**
     * Generates a simple chromosome composed of [size] integer genes.
     *
     * @return An [Either] containing the created [SimpleChromosome] on the right, or an [InitializationException] on
     *   the left if the chromosome creation fails.
     */
    override suspend fun invoke() = runCatching {
        val genes = executor(size) { SimpleGene(it) }
        SimpleChromosome(genes)
    }.fold(
        onSuccess = { it.right() },
        onFailure = { InitializationException("Failed to create a SimpleChromosome", it).left() }
    )
}

/**
 * Generates an arbitrary instance of a `SimpleChromosomeFactory` for property-based testing.
 *
 * @param sizeArb An optional [Arb] generator for the chromosome size. Defaults to an arbitrary integer between 0 and
 *   100.
 * @param constructorExecutorArb An optional [Arb] generator for the `ConstructorExecutor` to be used. If null, the
 *   factory uses its default executor.
 * @return An [Arb] generator for creating instances of `SimpleChromosomeFactory`.
 */
fun arbSimpleChromosomeFactory(
    sizeArb: Arb<Int>? = Arb.int(0..100),
    constructorExecutorArb: Arb<ConstructorExecutor<SimpleGene>>? = Arb.element(
        CoroutineConcurrentConstructor(),
        SequentialConstructor()
    )
) = arbitrary {
    SimpleChromosomeFactory().apply {
        sizeArb?.let { size = it.bind() }
        constructorExecutorArb?.let { executor = it.bind() }
    }
}
