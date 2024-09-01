/*
 * Copyright (c) 2024, Ignacio Slater M.
 * 2-Clause BSD License.
 */

package cl.ravenhill.keen.genetics

import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right
import cl.ravenhill.keen.Domain
import cl.ravenhill.keen.evolution.executors.construction.ConstructorExecutor
import cl.ravenhill.keen.exceptions.InitializationException
import cl.ravenhill.keen.genetics.chromosomes.Chromosome
import cl.ravenhill.keen.genetics.chromosomes.ChromosomeFactory
import cl.ravenhill.keen.genetics.genes.Gene
import cl.ravenhill.keen.repr.RepresentationFactory
import kotlin.properties.Delegates

/**
 * Factory for creating genotypes in an evolutionary algorithm.
 *
 * The `GenotypeFactory` class is responsible for constructing instances of `Genotype` by assembling a collection of
 * chromosomes. Each chromosome is generated using a [ChromosomeFactory], which in turn utilizes a [ConstructorExecutor]
 * to create the gene sequences that make up the chromosome. This class is designed to be used in evolutionary
 * algorithms where the structure and variability of genotypes play a crucial role in the evolutionary process.
 *
 * ## Usage:
 * The `GenotypeFactory` class is intended to be used within the initialization phase of an evolutionary algorithm,
 * where it generates new genotypes for individuals in the population. It provides a flexible and extensible approach
 * to genotype creation, allowing for customization of chromosome generation and the use of concurrent execution.
 *
 * ### Example: Creating a Genotype with a Custom Factory
 * ```kotlin
 * val chromosomeFactory1 = MyChromosomeFactory<Int, MyGene>()
 * val chromosomeFactory2 = MyChromosomeFactory<Int, MyGene>()
 *
 * val genotypeFactory = GenotypeFactory<Int, MyGene>().apply {
 *     chromosomes.add(chromosomeFactory1)
 *     chromosomes.add(chromosomeFactory2)
 * }
 *
 * runBlocking {
 *     val genotype = genotypeFactory().getOrElse { throw it }
 *     println(genotype)
 * }
 * ```
 *
 * @param T The type of value held by the genes in the chromosomes.
 * @param G The type of gene, which must extend [Gene].
 * @param executor The `ConstructorExecutor` used to generate the chromosomes for the genotype. Defaults to
 *   [Domain.defaultConstructor].
 * @property chromosomes A mutable list of `ChromosomeFactory` instances used to generate the chromosomes in the
 *   genotype.
 * @return A [Genotype] instance if successful, wrapped in an [Either] type to handle potential initialization failures.
 */
class GenotypeFactory<T, G>(
    val executor: ConstructorExecutor<Chromosome<T, G>> = Domain.defaultConstructor()
) : RepresentationFactory<T, G, Genotype<T, G>> where G : Gene<T, G> {

    /**
     * A mutable list of `ChromosomeFactory` instances used to generate the chromosomes in the genotype.
     */
    val chromosomes: MutableList<ChromosomeFactory<T, G>> = mutableListOf()

    /**
     * Generates a new genotype by assembling a collection of chromosomes.
     *
     * This method is responsible for creating a [Genotype] instance by invoking each [ChromosomeFactory] in the
     * [chromosomes] list. The creation process is managed by the `ConstructorExecutor`, which can handle the generation
     * of chromosomes concurrently or sequentially depending on its implementation.
     *
     * @return An [Either] containing a [Genotype] instance if successful, or an [InitializationException] if an error
     *   occurs during the construction process.
     */
    override suspend fun invoke(): Either<InitializationException, Genotype<T, G>> = try {
        val chromosomes = executor(chromosomes.size) { index ->
            chromosomes[index]().getOrElse { throw it }
        }
        Genotype(chromosomes).right()
    } catch (e: InitializationException) {
        e.left()    // This should never happen
    }
}
