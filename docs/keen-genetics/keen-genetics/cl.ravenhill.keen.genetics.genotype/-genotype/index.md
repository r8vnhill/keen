//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics.genotype](../index.md)/[Genotype](index.md)

# Genotype

data class [Genotype](index.md)&lt;[T](index.md), [G](index.md) : [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;(val chromosomes: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;) : [Representation](../../../../keen-core/keen-core/cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [G](index.md)&gt; , [ContainOps](../-contain-ops/index.md)&lt;[T](index.md), [G](index.md)&gt; , [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; , [Foldable](../../../../keen-core/keen-core/cl.ravenhill.keen.mixins/-foldable/index.md)&lt;[T](index.md)&gt; 

Represents a genotype in the evolutionary computation framework.

The `Genotype` class encapsulates a collection of chromosomes, providing functionalities to access and manipulate the genetic information of an individual. It implements the [Representation](../../../../keen-core/keen-core/cl.ravenhill.keen.repr/-representation/index.md) and [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html) interfaces, allowing it to be used in various evolutionary operations.

## Usage:

The `Genotype` class is typically used in evolutionary algorithms to represent the structure and genetic composition of individuals in a population.

### Example 1: Creating a Genotype with a List of Chromosomes

```kotlin
val chromosome1 = MyChromosome(MyGene(1), MyGene(2), MyGene(3))
val chromosome2 = MyChromosome(MyGene(4), MyGene(5), MyGene(6))
val genotype = Genotype(listOf(chromosome1, chromosome2))
```

### Example 2: Creating a Genotype with Vararg Chromosomes

```kotlin
val chromosome1 = MyChromosome(listOf(MyGene(1), MyGene(2), MyGene(3)))
val chromosome2 = MyChromosome(listOf(MyGene(4), MyGene(5), MyGene(6)))
val genotype = Genotype(chromosome1, chromosome2)
```

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the genes. |
| G | The type of the gene, which must extend [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md). |

## Constructors

| | |
|---|---|
| [Genotype](-genotype.md) | [common]<br>constructor(vararg chromosomes: [Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;)<br>Secondary constructor for creating a `Genotype` instance using a vararg of chromosomes.<br>constructor(chromosomes: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;)<br>Creates a `Genotype` instance with the specified list of chromosomes. |

## Properties

| Name | Summary |
|---|---|
| [chromosomes](chromosomes.md) | [common]<br>open override val [chromosomes](chromosomes.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;<br>The list of chromosomes that make up the genotype. |
| [size](size.md) | [common]<br>open override val [size](size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The number of chromosomes in the genotype. |

## Functions

| Name | Summary |
|---|---|
| [contains](../-contain-ops/contains.md) | [common]<br>open operator override fun [contains](../-contain-ops/contains.md)(element: [Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Checks if the genotype contains the specified chromosome. |
| [containsAll](../-contain-ops/contains-all.md) | [common]<br>open override fun [containsAll](../-contain-ops/contains-all.md)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Checks if the genotype contains all the specified chromosomes. |
| [drop](drop.md) | [common]<br>open override fun [drop](drop.md)(n: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): Either&lt;CompositeException, [Genotype](index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;<br>Removes the first [n](drop.md) chromosomes from the genotype, returning a new genotype with the remaining chromosomes. |
| [flatMap](index.md#854982520%2FFunctions%2F-1476930196) | [common]<br>open fun &lt;[R](index.md#854982520%2FFunctions%2F-1476930196)&gt; [flatMap](index.md#854982520%2FFunctions%2F-1476930196)(f: ([T](index.md)) -&gt; [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[R](index.md#854982520%2FFunctions%2F-1476930196)&gt;): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[R](index.md#854982520%2FFunctions%2F-1476930196)&gt; |
| [flatten](flatten.md) | [common]<br>open override fun [flatten](flatten.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[T](index.md)&gt;<br>Flattens the genotype by collecting the value of all genes in all chromosomes. |
| [fold](index.md#54685646%2FFunctions%2F-1476930196) | [common]<br>open override fun &lt;[R](../../../../keen-genetics/cl.ravenhill.keen.genetics.genotype/-genotype/fold.md)&gt; [fold](index.md#54685646%2FFunctions%2F-1476930196)(initial: [R](../../../../keen-genetics/cl.ravenhill.keen.genetics.genotype/-genotype/fold.md), operation: ([R](../../../../keen-genetics/cl.ravenhill.keen.genetics.genotype/-genotype/fold.md), [T](index.md)) -&gt; [R](../../../../keen-genetics/cl.ravenhill.keen.genetics.genotype/-genotype/fold.md)): [R](../../../../keen-genetics/cl.ravenhill.keen.genetics.genotype/-genotype/fold.md) |
| [foldRight](index.md#-446517528%2FFunctions%2F-1476930196) | [common]<br>open override fun &lt;[R](../../../../keen-genetics/cl.ravenhill.keen.genetics.genotype/-genotype/fold-right.md)&gt; [foldRight](index.md#-446517528%2FFunctions%2F-1476930196)(initial: [R](../../../../keen-genetics/cl.ravenhill.keen.genetics.genotype/-genotype/fold-right.md), operation: ([T](index.md), [R](../../../../keen-genetics/cl.ravenhill.keen.genetics.genotype/-genotype/fold-right.md)) -&gt; [R](../../../../keen-genetics/cl.ravenhill.keen.genetics.genotype/-genotype/fold-right.md)): [R](../../../../keen-genetics/cl.ravenhill.keen.genetics.genotype/-genotype/fold-right.md) |
| [get](get.md) | [common]<br>operator fun [get](get.md)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): Either&lt;CompositeException, [Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;<br>Retrieves the chromosome at the specified index. |
| [isEmpty](is-empty.md) | [common]<br>open override fun [isEmpty](is-empty.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Checks if the genotype is empty (i.e., contains no chromosomes). |
| [iterator](iterator.md) | [common]<br>open operator override fun [iterator](iterator.md)(): [Iterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)&lt;[Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;<br>Provides an iterator over the chromosomes in the genotype. |
| [map](map.md) | [common]<br>open override fun [map](map.md)(transform: ([T](index.md)) -&gt; [T](index.md)): [Genotype](index.md)&lt;[T](index.md), [G](index.md)&gt;<br>Applies a transformation function to each gene in the genotype, producing a new genotype with the transformed genes. |
| [take](take.md) | [common]<br>open override fun [take](take.md)(n: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): Either&lt;CompositeException, [Genotype](index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;<br>Retains the first [n](take.md) chromosomes from the genotype, returning a new genotype with only the first [n](take.md) chromosomes. |
| [toString](to-string.md) | [common]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Returns a string representation of the genotype. |
| [verify](verify.md) | [common]<br>open override fun [verify](verify.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Verifies the validity of the genotype by checking all its chromosomes. |
