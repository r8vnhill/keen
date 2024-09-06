//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics.chromosomes](../index.md)/[ChromosomeFactory](index.md)

# ChromosomeFactory

interface [ChromosomeFactory](index.md)&lt;[T](index.md), [G](index.md) : [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; : [RepresentationFactory](../../../../keen-core/keen-core/cl.ravenhill.keen.repr/-representation-factory/index.md)&lt;[T](index.md), [G](index.md), [Chromosome](../-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; 

Factory interface for creating chromosomes in an evolutionary algorithm.

The `ChromosomeFactory` interface defines the structure for factories that generate chromosomes, which are collections of genes. It extends the `RepresentationFactory` interface, specifically targeting the creation of `Chromosome` instances. The factory uses a `ConstructorExecutor` to handle the creation of the gene sequences that make up the chromosome.

## Usage:

This interface is intended for use in scenarios where specific chromosomes need to be generated within an evolutionary algorithm. It provides a flexible mechanism for customizing how the genes within a chromosome are constructed, by allowing the use of different `ConstructorExecutor` implementations.

### Example 1: Creating a Custom Chromosome Factory

```kotlin
class MyChromosomeFactory : ChromosomeFactory<Int, IntGene> {
    override var executor: ConstructorExecutor<IntGene> = MyCustomConstructor()

    override fun create(): Chromosome<Int, IntGene> {
        val genes = executor.invoke(10) { IntGene(it) }
        return IntChromosome(genes)
    }
}
```

## Recommendation:

It is generally recommended to use the `AbstractChromosomeFactory` class instead of directly implementing the `ChromosomeFactory` interface. The abstract class provides a default implementation for the `executor` property, making it easier to extend and customize the factory without needing to re-implement basic functionality. Using the abstract factory pattern also promotes consistency and reduces boilerplate code across different implementations.

## Benefits of Using `AbstractChromosomeFactory`:

- 
   **Reduced Boilerplate**: The abstract class provides a default `SequentialConstructor` for the `executor`, reducing the need for repetitive code.
- 
   **Extensibility**: By extending the abstract class, you can easily customize the behavior of the factory without having to start from scratch.
- 
   **Consistency**: Using the abstract factory pattern ensures that all factories share a common structure and initialization process, making the codebase more maintainable and understandable.

### Example 2: Extending `AbstractChromosomeFactory`

```kotlin
class MyChromosomeFactory : AbstractChromosomeFactory<Int, IntGene>() {
    override fun make(): Chromosome<Int, IntGene> {
        val genes = executor(10) { IntGene(it) }
        return IntChromosome(genes)
    }
}
```

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the genes in the chromosome. |
| G | The type of the gene, which must extend [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md). |

#### Inheritors

| |
|---|
| [AbstractChromosomeFactory](../-abstract-chromosome-factory/index.md) |

## Properties

| Name | Summary |
|---|---|
| [executor](executor.md) | [common]<br>abstract var [executor](executor.md): [ConstructorExecutor](../../../../keen-core/keen-core/cl.ravenhill.keen.evolution.executors.construction/-constructor-executor/index.md)&lt;[G](index.md)&gt;<br>The `ConstructorExecutor` used to generate the sequence of genes within the chromosome. |

## Functions

| Name | Summary |
|---|---|
| [invoke](../-abstract-chromosome-factory/index.md#254008802%2FFunctions%2F-1476930196) | [common]<br>abstract suspend operator fun [invoke](../-abstract-chromosome-factory/index.md#254008802%2FFunctions%2F-1476930196)(): Either&lt;[InitializationException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-initialization-exception/index.md), [Chromosome](../-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; |
