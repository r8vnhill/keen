//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics](../index.md)/[GenotypeFactory](index.md)

# GenotypeFactory

class [GenotypeFactory](index.md)&lt;[T](index.md), [G](index.md) : [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;(val executor: [ConstructorExecutor](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.executors.construction/-constructor-executor/index.md)&lt;[Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; = Domain.defaultConstructor()) : [RepresentationFactory](../../../../keen-core/keen-core/cl.ravenhill.keen.repr/-representation-factory/index.md)&lt;[T](index.md), [G](index.md), [Genotype](../-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; 

Factory for creating genotypes in an evolutionary algorithm.

The `GenotypeFactory` class is responsible for constructing instances of `Genotype` by assembling a collection of chromosomes. Each chromosome is generated using a `ChromosomeFactory`, which in turn utilizes a `ConstructorExecutor` to create the gene sequences that make up the chromosome. This class is designed to be used in evolutionary algorithms where the structure and variability of genotypes play a crucial role in the evolutionary process.

## Key Features:

- 
   **Chromosome Factories**: The `GenotypeFactory` maintains a list of `ChromosomeFactory` instances, each responsible for creating a specific chromosome. The number of chromosomes is determined by the size of this list.
- 
   **Constructor Executor**: The `executor` property allows for concurrent or sequential generation of chromosomes, depending on the specific implementation of `ConstructorExecutor` being used. By default, this is set to `CoroutineConcurrentConstructor`, which uses Kotlin coroutines for concurrent construction.
- 
   **Size Property**: The `size` property represents the number of chromosomes to be generated for the genotype. It must be initialized before invoking the factory.

## Usage:

The `GenotypeFactory` class is intended to be used within the initialization phase of an evolutionary algorithm, where it generates new genotypes for individuals in the population. It provides a flexible and extensible approach to genotype creation, allowing for customization of chromosome generation and the use of concurrent execution.

### Example: Creating a Genotype with a Custom Factory

```kotlin
val chromosomeFactory1 = MyChromosomeFactory<Int, MyGene>()
val chromosomeFactory2 = MyChromosomeFactory<Int, MyGene>()

val genotypeFactory = GenotypeFactory<Int, MyGene>().apply {
    chromosomes.add(chromosomeFactory1)
    chromosomes.add(chromosomeFactory2)
    size = 2
}

runBlocking {
    val genotype = genotypeFactory().getOrElse { throw it }
    println(genotype)
}
```

#### Return

A [Genotype](../-genotype/index.md) instance if successful, wrapped in an Either type to handle potential initialization failures.

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the genes in the chromosomes. |
| G | The type of gene, which must extend [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md). |
| executor | The `ConstructorExecutor` used to generate the chromosomes for the genotype. Defaults to `CoroutineConcurrentConstructor`. |

#### Throws

| | |
|---|---|
| [InitializationException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-initialization-exception/index.md) | If any error occurs during the creation of the genotype, such as a failure in chromosome construction. |

## Constructors

| | |
|---|---|
| [GenotypeFactory](-genotype-factory.md) | [common]<br>constructor(executor: [ConstructorExecutor](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.executors.construction/-constructor-executor/index.md)&lt;[Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; = Domain.defaultConstructor()) |

## Properties

| Name | Summary |
|---|---|
| [chromosomes](chromosomes.md) | [common]<br>val [chromosomes](chromosomes.md): [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[ChromosomeFactory](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome-factory/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;<br>A mutable list of `ChromosomeFactory` instances used to generate the chromosomes in the genotype. |
| [executor](executor.md) | [common]<br>val [executor](executor.md): [ConstructorExecutor](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.executors.construction/-constructor-executor/index.md)&lt;[Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; |
| [size](size.md) | [common]<br>open override var [size](size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The number of chromosomes to generate for the genotype. This must be initialized before invoking the factory. |

## Functions

| Name | Summary |
|---|---|
| [invoke](invoke.md) | [common]<br>open suspend operator override fun [invoke](invoke.md)(): Either&lt;[InitializationException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-initialization-exception/index.md), [Genotype](../-genotype/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;<br>Generates a new genotype by assembling a collection of chromosomes. |
