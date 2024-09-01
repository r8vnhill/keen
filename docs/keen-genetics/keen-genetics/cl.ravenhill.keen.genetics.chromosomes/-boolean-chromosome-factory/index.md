//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics.chromosomes](../index.md)/[BooleanChromosomeFactory](index.md)

# BooleanChromosomeFactory

[common]\
class [BooleanChromosomeFactory](index.md) : [AbstractChromosomeFactory](../-abstract-chromosome-factory/index.md)&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), [BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md)&gt; 

Factory class for creating [BooleanChromosome](../-boolean-chromosome/index.md) instances in an evolutionary algorithm.

The `BooleanChromosomeFactory` class is a concrete implementation of the [AbstractChromosomeFactory](../-abstract-chromosome-factory/index.md) designed for generating chromosomes composed of boolean genes ([BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md)). This factory leverages a [ConstructorExecutor](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.executors.construction/-constructor-executor/index.md) to create the gene sequence and supports asynchronous, non-blocking chromosome generation. Additionally, the factory allows customization of the probability that a gene will be `True` through the [trueRate](true-rate.md) property.

## Usage:

This class is intended for use within evolutionary algorithms that require boolean chromosomes. The factory generates a chromosome of a specified size, where each gene is randomly set to either `True` or `False` based on the provided `Random` instance and the configured `trueRate`. The size of the chromosome must be a positive integer, and this constraint is strictly enforced by the factory.

### Example: Creating a Boolean Chromosome

```kotlin
val factory = BooleanChromosomeFactory().apply {
    size = 10
    trueRate = 0.7
}
val result = factory.invoke(Random())
result.onSuccess { chromosome ->
    println("Generated chromosome: $chromosome")
}.onFailure { exception ->
    println("Failed to generate chromosome: ${exception.message}")
}
```

## Constructors

| | |
|---|---|
| [BooleanChromosomeFactory](-boolean-chromosome-factory.md) | [common]<br>constructor()<br>Initializes a new instance of `BooleanChromosomeFactory` with the default settings from the `AbstractChromosomeFactory`. |

## Properties

| Name | Summary |
|---|---|
| [executor](../-abstract-chromosome-factory/executor.md) | [common]<br>open override var [executor](../-abstract-chromosome-factory/executor.md): [ConstructorExecutor](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.executors.construction/-constructor-executor/index.md)&lt;[BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md)&gt;<br>The `ConstructorExecutor` used to generate the sequence of genes within the chromosome. Defaults to a `SequentialConstructor`. |
| [size](../-abstract-chromosome-factory/size.md) | [common]<br>open override var [size](../-abstract-chromosome-factory/size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [trueRate](true-rate.md) | [common]<br>var [trueRate](true-rate.md): [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)<br>The probability that a gene in the chromosome will be `True`. |

## Functions

| Name | Summary |
|---|---|
| [invoke](invoke.md) | [common]<br>open suspend operator override fun [invoke](invoke.md)(): Either&lt;[InitializationException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-initialization-exception/index.md), [Chromosome](../-chromosome/index.md)&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), [BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md)&gt;&gt;<br>Asynchronously creates a `BooleanChromosome` of the specified size. |
