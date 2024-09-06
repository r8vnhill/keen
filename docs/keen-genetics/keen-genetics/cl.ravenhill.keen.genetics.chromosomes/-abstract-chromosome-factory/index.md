//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics.chromosomes](../index.md)/[AbstractChromosomeFactory](index.md)

# AbstractChromosomeFactory

abstract class [AbstractChromosomeFactory](index.md)&lt;[T](index.md), [G](index.md) : [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; : [ChromosomeFactory](../-chromosome-factory/index.md)&lt;[T](index.md), [G](index.md)&gt; 

Abstract factory class for creating chromosomes in an evolutionary algorithm.

The `AbstractChromosomeFactory` class provides a base implementation for the `ChromosomeFactory` interface, offering a default `SequentialConstructor` for the `executor` property. This class is designed to be extended by concrete factory implementations, simplifying the process of creating custom chromosome factories while promoting code reuse and consistency.

## Usage:

Extend this class to create a custom chromosome factory. The default `SequentialConstructor` can be replaced with a different `ConstructorExecutor` if needed, allowing for flexible and varied construction strategies for the genes within the chromosome.

### Example 1: Extending `AbstractChromosomeFactory`

```kotlin
class MyChromosomeFactory : AbstractChromosomeFactory<Int, IntGene>() {
    override fun create(): Chromosome<Int, IntGene> {
        val genes = executor.invoke(10) { IntGene(it) }
        return IntChromosome(genes)
    }
}
```

## Benefits of Using `AbstractChromosomeFactory`:

- 
   **Default Implementation**: The abstract class provides a sensible default for the `executor`, which reduces the amount of code you need to write when creating a new factory.
- 
   **Ease of Extension**: This class is designed for easy extension, allowing developers to focus on the specific details of chromosome creation without worrying about the boilerplate.
- 
   **Promotes Consistency**: By using this abstract class, you ensure that all chromosome factories in your application share a common structure, making your codebase more maintainable.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the genes in the chromosome. |
| G | The type of the gene, which must extend [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md). |

#### Inheritors

| |
|---|
| [BooleanChromosomeFactory](../-boolean-chromosome-factory/index.md) |

## Constructors

| | |
|---|---|
| [AbstractChromosomeFactory](-abstract-chromosome-factory.md) | [common]<br>constructor() |

## Properties

| Name | Summary |
|---|---|
| [executor](executor.md) | [common]<br>open override var [executor](executor.md): [ConstructorExecutor](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.executors.construction/-constructor-executor/index.md)&lt;[G](index.md)&gt;<br>The `ConstructorExecutor` used to generate the sequence of genes within the chromosome. Defaults to a `SequentialConstructor`. |
| [size](size.md) | [common]<br>var [size](size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The size of the chromosome to be created. This property must be initialized before invoking the factory. |

## Functions

| Name | Summary |
|---|---|
| [invoke](index.md#254008802%2FFunctions%2F-1476930196) | [common]<br>abstract suspend operator fun [invoke](index.md#254008802%2FFunctions%2F-1476930196)(): Either&lt;[InitializationException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-initialization-exception/index.md), [Chromosome](../-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; |
