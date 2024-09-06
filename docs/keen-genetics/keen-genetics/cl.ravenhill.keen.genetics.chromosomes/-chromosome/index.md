//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics.chromosomes](../index.md)/[Chromosome](index.md)

# Chromosome

interface [Chromosome](index.md)&lt;[T](index.md), [G](index.md) : [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; : [Representation](../../../../keen-core/keen-core/cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [G](index.md)&gt; , [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[G](index.md)&gt; , [FlatMappable](../../../../keen-core/keen-core/cl.ravenhill.keen.mixins/-flat-mappable/index.md)&lt;[T](index.md)&gt; , [ContainOps](../-contain-ops/index.md)&lt;[T](index.md), [G](index.md)&gt; , [FoldOps](../-fold-ops/index.md)&lt;[T](index.md), [G](index.md)&gt; 

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
| [genes](genes.md) | [common]<br>abstract override val [genes](genes.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[G](index.md)&gt;<br>The list of genes that make up the chromosome. |
| [size](size.md) | [common]<br>open override val [size](size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The size of the chromosome, representing the number of genes it contains. |

## Functions

| Name | Summary |
|---|---|
| [contains](index.md#61667886%2FFunctions%2F-1476930196) | [common]<br>abstract operator fun [contains](index.md#61667886%2FFunctions%2F-1476930196)(element: [G](index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [containsAll](index.md#40819325%2FFunctions%2F-1476930196) | [common]<br>abstract fun [containsAll](index.md#40819325%2FFunctions%2F-1476930196)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[G](index.md)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [copyWithGenes](copy-with-genes.md) | [common]<br>abstract fun [copyWithGenes](copy-with-genes.md)(newGenes: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[G](index.md)&gt;): [Chromosome](index.md)&lt;[T](index.md), [G](index.md)&gt;<br>Creates a new chromosome with a specified list of genes. |
| [drop](drop.md) | [common]<br>open override fun [drop](drop.md)(n: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): Either&lt;CompositeException, [Representation](../../../../keen-core/keen-core/cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; |
| [flatMap](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md#854982520%2FFunctions%2F-1476930196) | [common]<br>open fun &lt;[R](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md#854982520%2FFunctions%2F-1476930196)&gt; [flatMap](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md#854982520%2FFunctions%2F-1476930196)(f: ([T](index.md)) -&gt; [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[R](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md#854982520%2FFunctions%2F-1476930196)&gt;): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[R](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md#854982520%2FFunctions%2F-1476930196)&gt; |
| [flatten](flatten.md) | [common]<br>open override fun [flatten](flatten.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[T](index.md)&gt;<br>Flattens the chromosome to a list containing the values of all its genes. |
| [fold](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md#54685646%2FFunctions%2F-1476930196) | [common]<br>abstract fun &lt;[R](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md#54685646%2FFunctions%2F-1476930196)&gt; [fold](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md#54685646%2FFunctions%2F-1476930196)(initial: [R](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md#54685646%2FFunctions%2F-1476930196), operation: ([R](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md#54685646%2FFunctions%2F-1476930196), [T](index.md)) -&gt; [R](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md#54685646%2FFunctions%2F-1476930196)): [R](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md#54685646%2FFunctions%2F-1476930196) |
| [foldRight](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md#-446517528%2FFunctions%2F-1476930196) | [common]<br>abstract fun &lt;[R](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md#-446517528%2FFunctions%2F-1476930196)&gt; [foldRight](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md#-446517528%2FFunctions%2F-1476930196)(initial: [R](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md#-446517528%2FFunctions%2F-1476930196), operation: ([T](index.md), [R](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md#-446517528%2FFunctions%2F-1476930196)) -&gt; [R](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md#-446517528%2FFunctions%2F-1476930196)): [R](../../cl.ravenhill.keen.genetics.genotype/-genotype/index.md#-446517528%2FFunctions%2F-1476930196) |
| [get](get.md) | [common]<br>open operator fun [get](get.md)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): Either&lt;CompositeException, [G](index.md)&gt;<br>Retrieves the gene at the specified index within the chromosome. |
| [isEmpty](is-empty.md) | [common]<br>open override fun [isEmpty](is-empty.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Checks if the chromosome is empty, meaning it contains no genes. |
| [iterator](iterator.md) | [common]<br>open operator override fun [iterator](iterator.md)(): [Iterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)&lt;[G](index.md)&gt;<br>Provides an iterator over the genes in the chromosome. |
| [map](map.md) | [common]<br>open override fun [map](map.md)(transform: ([T](index.md)) -&gt; [T](index.md)): [Chromosome](index.md)&lt;[T](index.md), [G](index.md)&gt;<br>Transforms the genes in the chromosome by applying a given function to each gene's value. |
| [take](take.md) | [common]<br>open override fun [take](take.md)(n: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): Either&lt;[Exception](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html), [Representation](../../../../keen-core/keen-core/cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; |
| [verify](verify.md) | [common]<br>open override fun [verify](verify.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Verifies the correctness or validity of the chromosome. |
