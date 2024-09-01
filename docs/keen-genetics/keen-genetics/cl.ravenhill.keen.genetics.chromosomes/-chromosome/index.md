//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics.chromosomes](../index.md)/[Chromosome](index.md)

# Chromosome

interface [Chromosome](index.md)&lt;[T](index.md), [G](index.md) : [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; : [Representation](../../../../keen-core/keen-core/cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [G](index.md)&gt; , [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[G](index.md)&gt; , [FlatMappable](../../../../keen-core/keen-core/cl.ravenhill.keen.mixins/-flat-mappable/index.md)&lt;[T](index.md)&gt; 

Represents a chromosome in the Keen evolutionary computation framework.

The `Chromosome` interface defines the structure and behavior of a chromosome, which is a collection of genes that encode a solution or candidate in evolutionary algorithms. It extends the [Representation](../../../../keen-core/keen-core/cl.ravenhill.keen.repr/-representation/index.md), [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html), and [FlatMappable](../../../../keen-core/keen-core/cl.ravenhill.keen.mixins/-flat-mappable/index.md) interfaces, providing additional functionality specific to chromosomes, such as verification, duplication, and flattening.

## Usage:

Implement this interface to define the behavior and properties of chromosomes in evolutionary algorithms. Classes implementing this interface must provide the `genes` property, which represents the sequence of genes that make up the chromosome. Implementing classes can also override methods like `verify` and `flatten` to customize the chromosome's behavior.

### Example 1: Implementing a Chromosome

```kotlin
data class MyChromosome(
    override val genes: List<MyGene>
) : Chromosome<Int, MyGene> {
    override fun copyWithGenes(newGenes: List<MyGene>) = copy(genes = newGenes)
}
```

### Example 2: Verifying a Chromosome

```kotlin
val chromosome = MyChromosome(listOf(MyGene(1) { random -> random.nextInt(0, 10) }))
val isValid = chromosome.verify()
```

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the genes. |
| G | The type of the gene, which must extend [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md). |

#### Inheritors

| |
|---|
| [BooleanChromosome](../-boolean-chromosome/index.md) |

## Properties

| Name | Summary |
|---|---|
| [genes](genes.md) | [common]<br>abstract val [genes](genes.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[G](index.md)&gt;<br>The list of genes that make up the chromosome. |
| [size](size.md) | [common]<br>open override val [size](size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The size of the chromosome, representing the number of genes it contains. |

## Functions

| Name | Summary |
|---|---|
| [contains](contains.md) | [common]<br>open operator override fun [contains](contains.md)(element: [G](index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Checks if the chromosome contains the specified gene. |
| [containsAll](contains-all.md) | [common]<br>open override fun [containsAll](contains-all.md)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[G](index.md)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Checks if the chromosome contains all the specified genes. |
| [copyWithGenes](copy-with-genes.md) | [common]<br>abstract fun [copyWithGenes](copy-with-genes.md)(newGenes: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[G](index.md)&gt;): [Chromosome](index.md)&lt;[T](index.md), [G](index.md)&gt;<br>Creates a new chromosome with a specified list of genes. |
| [flatMap](index.md#854982520%2FFunctions%2F-1476930196) | [common]<br>open fun &lt;[R](index.md#854982520%2FFunctions%2F-1476930196)&gt; [flatMap](index.md#854982520%2FFunctions%2F-1476930196)(f: ([T](index.md)) -&gt; [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[R](index.md#854982520%2FFunctions%2F-1476930196)&gt;): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[R](index.md#854982520%2FFunctions%2F-1476930196)&gt; |
| [flatten](flatten.md) | [common]<br>open override fun [flatten](flatten.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[T](index.md)&gt;<br>Flattens the chromosome to a list containing the values of all its genes. |
| [fold](fold.md) | [common]<br>open override fun &lt;[R](fold.md)&gt; [fold](fold.md)(initial: [R](fold.md), operation: ([R](fold.md), [T](index.md)) -&gt; [R](fold.md)): [R](fold.md)<br>Folds the genes in the chromosome from left to right, accumulating a result. |
| [foldRight](fold-right.md) | [common]<br>open override fun &lt;[R](fold-right.md)&gt; [foldRight](fold-right.md)(initial: [R](fold-right.md), operation: ([T](index.md), [R](fold-right.md)) -&gt; [R](fold-right.md)): [R](fold-right.md)<br>Folds the genes in the chromosome from right to left, accumulating a result. |
| [get](get.md) | [common]<br>open operator fun [get](get.md)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): Either&lt;CompositeException, [G](index.md)&gt;<br>Retrieves the gene at the specified index within the chromosome. |
| [isEmpty](is-empty.md) | [common]<br>open override fun [isEmpty](is-empty.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Checks if the chromosome is empty, meaning it contains no genes. |
| [iterator](iterator.md) | [common]<br>open operator override fun [iterator](iterator.md)(): [Iterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)&lt;[G](index.md)&gt;<br>Provides an iterator over the genes in the chromosome. |
| [verify](verify.md) | [common]<br>open override fun [verify](verify.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Verifies the correctness or validity of the chromosome. |
