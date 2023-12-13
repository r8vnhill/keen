/*
 * Copyright (c) 2023, Ignacio Slater M.
 * 2-Clause BSD License.
 */


package cl.ravenhill.keen.arb.genetic.chromosomes

import cl.ravenhill.keen.arb.genetic.genes.DummyGene
import cl.ravenhill.keen.arb.genetic.genes.doubleGene
import cl.ravenhill.keen.arb.genetic.genes.gene
import cl.ravenhill.keen.arb.range
import cl.ravenhill.keen.evolution.executors.ConstructorExecutor
import cl.ravenhill.keen.evolution.executors.SequentialConstructor
import cl.ravenhill.keen.genetic.chromosomes.NothingChromosome
import cl.ravenhill.keen.genetic.chromosomes.Chromosome
import cl.ravenhill.keen.genetic.chromosomes.numeric.DoubleChromosome
import cl.ravenhill.keen.genetic.chromosomes.numeric.NumberChromosome
import cl.ravenhill.keen.genetic.genes.NothingGene
import cl.ravenhill.keen.genetic.genes.numeric.DoubleGene
import cl.ravenhill.keen.genetic.genes.numeric.NumberGene
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.boolean
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.double
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map

/**
 * A dummy implementation of the [Chromosome] interface for testing purposes.
 *
 * This class represents a simple chromosome composed of a list of [DummyGene] objects.
 * It is designed for use in unit tests or simulations where complex chromosomal behavior is
 * not required.
 *
 * @property genes The list of [DummyGene] instances that make up this chromosome.
 */
class ChromosomeImpl(override val genes: List<DummyGene>) : Chromosome<Int, DummyGene> {

    /**
     * Creates a copy of this [ChromosomeImpl] with the specified genes.
     *
     * This method allows for the creation of a new chromosome instance with a different
     * set of genes while maintaining the overall structure of the original chromosome.
     *
     * @param genes The new list of [DummyGene] instances for the chromosome.
     * @return A new [ChromosomeImpl] instance with the given genes.
     */
    override fun duplicateWithGenes(genes: List<DummyGene>) = ChromosomeImpl(genes.map { it.copy() })
}

/**
 * Generates an arbitrary [ChromosomeImpl] for property-based testing.
 *
 * This function creates instances of [ChromosomeImpl] with a specified number of [DummyGene]
 * objects. Each gene within the chromosome is generated randomly based on the provided [gene]
 * arbitrary. This function is particularly useful for testing genetic algorithms where
 * chromosome behavior needs to be simulated without complex logic.
 *
 * @param size An [Arb]<[Int]> generator for the size (number of genes) of the chromosome.
 *             Defaults to a range from 0 to 10.
 * @param gene An [Arb]<[DummyGene]> generator for the individual genes of the chromosome.
 *
 * @return An [Arb] that generates [ChromosomeImpl] instances with a randomized list of genes.
 */
fun Arb.Companion.chromosome(
    size: Arb<Int> = int(0..10),
    isValid: Arb<Boolean> = boolean(),
    gene: Arb<DummyGene> = Arb.gene(isValid = isValid),
) = arbitrary {
    ChromosomeImpl(List(size.bind()) { gene.bind() })
}

// TODO: Add documentation
fun Arb.Companion.nothingChromosome(
    size: Arb<Int> = int(0..10),
) = arbitrary {
    NothingChromosome(List(size.bind()) { NothingGene })
}

/**
 * Generates an arbitrary [DoubleChromosome] for property-based testing in genetic algorithms.
 *
 * This function creates instances of [DoubleChromosome] with a configurable number of [DoubleGene]s. The size of
 * the chromosome, i.e., the number of genes it contains, is determined by the `size` parameter. Each gene in the
 * chromosome is generated using the provided `gene` function, which produces instances of [DoubleGene].
 *
 * ## Usage:
 * This arbitrary generator is useful in property-based testing frameworks like Kotest for creating diverse instances
 * of [DoubleChromosome], enabling comprehensive testing across various genetic structures and scenarios.
 *
 * ### Example:
 * Generating a double chromosome with a specific number of genes:
 * ```kotlin
 * val geneArb = Arb.doubleGene(/* ... */)
 * val chromosomeArb = Arb.doubleChromosome(Arb.constant(5), geneArb) // Configuring exactly 5 genes
 * val chromosome = chromosomeArb.bind() // Resulting chromosome will have 5 genes
 * ```
 * In this example, `chromosomeArb` generates a [DoubleChromosome] with exactly five genes, each created
 * by the provided `geneArb`.
 *
 * @param size An [Arb]<[Int]> specifying the potential number of genes in the chromosome.
 *   Defaults to a range of 0 to 10, allowing chromosomes with up to 10 genes.
 * @param gene An [Arb] of [DoubleGene] for generating individual genes within the chromosome.
 *   Defaults to [doubleGene()], which generates arbitrary instances of [DoubleGene].
 * @return An [Arb] that generates instances of [DoubleChromosome] with a specified number of genes.
 */
fun Arb.Companion.doubleChromosome(
    size: Arb<Int> = int(0..5),
    gene: Arb<DoubleGene> = doubleGene(),
) = arbitrary {
    DoubleChromosome(List(size.bind()) { gene.bind() })
}

/**
 * Generates an arbitrary [Chromosome.Factory] for property-based testing.
 *
 * This function creates instances of [Chromosome.Factory] with a specified number of [DummyGene]
 * objects. Each gene within the chromosome is generated randomly based on the provided [gene]
 * arbitrary. This function is particularly useful for testing genetic algorithms where
 * chromosome behavior needs to be simulated without complex logic.
 *
 * @param size An [Arb]<[Int]> generator for the size (number of genes) of the chromosome.
 *             Defaults to a range from 0 to 10.
 * @param gene An [Arb]<[DummyGene]> generator for the individual genes of the chromosome.
 *
 * @return An [Arb] that generates [Chromosome.Factory] instances with a randomized list of genes.
 */
fun Arb.Companion.chromosomeFactory(
    size: Arb<Int> = int(0..10),
    isValid: Arb<Boolean> = boolean(),
    gene: Arb<DummyGene> = Arb.gene(isValid = isValid),
): Arb<Chromosome.Factory<Int, DummyGene>> = arbitrary {
    val boundSize = size.bind()
    val genes = List(boundSize) { gene.bind() }
    object : Chromosome.Factory<Int, DummyGene> {
        override var executor: ConstructorExecutor<DummyGene> = SequentialConstructor()
        override var size = boundSize
        override fun make() = ChromosomeImpl(executor(boundSize) { genes[it] })
    }
}

fun <T, G> Arb.Companion.numberChromosomeFactory(
    gene: Arb<G>,
    size: Arb<Int> = int(0..10),
): Arb<NumberChromosome.Factory<T, G>> where T : Number, T : Comparable<T>, G : NumberGene<T, G> = arbitrary {
    val boundSize = size.bind()
    val genes = List(boundSize) { gene.bind() }
    object : NumberChromosome.Factory<T, G> {
        override var executor: ConstructorExecutor<G> = SequentialConstructor()
        override var size = boundSize
        override val defaultRange: ClosedRange<T>
            get() = TODO("Not yet implemented")

        override fun make() = object : NumberChromosome<T, G> {
            override val genes: List<G>
                get() = genes

            override fun duplicateWithGenes(genes: List<G>): Nothing = throw NotImplementedError("Undefined")
        }

        override fun createChromosome(): Chromosome<T, G> {
            TODO("Not yet implemented")
        }

        override var ranges: MutableList<ClosedRange<T>> = mutableListOf()
        override var filters: MutableList<(T) -> Boolean> = mutableListOf()
    }
}

/**
 * Generates an arbitrary instance of [DoubleChromosome.Factory] for property-based testing.
 *
 * This function creates factories capable of producing [DoubleChromosome] instances. The factories are configured
 * with various ranges and filters, making it versatile for testing scenarios that require diverse chromosome
 * configurations. Each chromosome generated by the factory contains [DoubleGene] elements, which can have specific
 * ranges and filters applied to them.
 *
 * ## Parameters:
 * - **ranges**: An optional [Arb] of [MutableList]<[ClosedRange]<[Double]>> representing the possible ranges for
 *   the genes in the chromosome. Each range in the list corresponds to a gene in the chromosome. Defaults to a list
 *   of 0 to 5 random double ranges.
 * - **filters**: An optional [Arb] of [MutableList] containing filter functions for the genes. Each filter in the list
 *   corresponds to a gene in the chromosome. Defaults to a list of 0 to 5 constant true filter functions, meaning no
 *   filtering is applied.
 *
 * ## Usage:
 * This arbitrary generator is useful in scenarios where chromosomes with specific genetic configurations are needed
 * to robustly test various aspects of evolutionary algorithms. It allows for creating factories that produce
 * chromosomes with tailored ranges and filters for their genes.
 *
 * ### Example:
 * Generating a factory for creating [DoubleChromosome] instances with specific ranges and filters:
 * ```kotlin
 * val chromosomeFactoryArb = Arb.doubleChromosomeFactory(
 *     ranges = Arb.list(range(Arb.double(0.0, 1.0), Arb.double(1.0, 2.0)), 0..5).map { it.toMutableList() },
 *     filters = Arb.list(constant { value: Double -> value > 0.5 }, 0..5).map { it.toMutableList() }
 * )
 * val chromosomeFactory = chromosomeFactoryArb.bind() // Binds to a DoubleChromosome.Factory instance
 * val chromosome = chromosomeFactory.make() // Creates a DoubleChromosome instance
 * ```
 * In this example, `chromosomeFactoryArb` generates arbitrary factories configured with specific ranges and filters.
 * The `make` method of the bound factory then creates a [DoubleChromosome] instance with these configurations.
 *
 * @return An [Arb] that generates instances of [DoubleChromosome.Factory].
 */
fun Arb.Companion.doubleChromosomeFactory(
    size: Arb<Int> = int(1..5),
    ranges: ((size: Int) -> Arb<MutableList<ClosedRange<Double>>>)? = {
        list(
            range(double(), double()).filter { range ->
                range.start < range.endInclusive && range.start.isFinite() && range.endInclusive.isFinite()
            },
            it..it
        ).map { ls -> ls.toMutableList() }
    },
    filters: ((size: Int) -> Arb<MutableList<(Double) -> Boolean>>)? = {
        list(constant { _: Double -> true }, it..it).map { ls -> ls.toMutableList() }
    },
): Arb<DoubleChromosome.Factory> = arbitrary {
    DoubleChromosome.Factory().apply {
        this.size = size.bind()
        ranges?.let { this.ranges = it(this.size).bind() }
        filters?.let { this.filters = it(this.size).bind() }
    }
}
