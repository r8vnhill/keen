//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics.chromosomes](../index.md)/[ContainOps](index.md)

# ContainOps

interface [ContainOps](index.md)&lt;[T](index.md), [G](index.md) : [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; : [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[G](index.md)&gt; 

Provides containment operations for chromosomes in an evolutionary algorithm.

The `ContainOps` interface defines methods for checking the presence of genes within a chromosome. It offers basic operations for verifying whether the chromosome contains a specific gene or a collection of genes. This interface is useful in genetic algorithms where chromosomes are composed of multiple genes, and operations need to be performed to verify the integrity or structure of the chromosome.

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the genes. |
| G | The type of gene within the chromosome, which must extend [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md). |

#### Inheritors

| |
|---|
| [Chromosome](../-chromosome/index.md) |

## Properties

| Name | Summary |
|---|---|
| [genes](genes.md) | [common]<br>abstract val [genes](genes.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[G](index.md)&gt;<br>The list of genes contained in the chromosome. |
| [size](../../cl.ravenhill.keen.genetics.genotype/-contain-ops/index.md#-113084078%2FProperties%2F-1476930196) | [common]<br>abstract val [size](../../cl.ravenhill.keen.genetics.genotype/-contain-ops/index.md#-113084078%2FProperties%2F-1476930196): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [contains](contains.md) | [common]<br>open operator override fun [contains](contains.md)(element: [G](index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Checks if the chromosome contains the specified gene. |
| [containsAll](contains-all.md) | [common]<br>open override fun [containsAll](contains-all.md)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[G](index.md)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Checks if the chromosome contains all the specified genes. |
| [isEmpty](../../cl.ravenhill.keen.genetics.genotype/-contain-ops/index.md#-719293276%2FFunctions%2F-1476930196) | [common]<br>abstract fun [isEmpty](../../cl.ravenhill.keen.genetics.genotype/-contain-ops/index.md#-719293276%2FFunctions%2F-1476930196)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [iterator](../../cl.ravenhill.keen.genetics.genotype/-contain-ops/index.md#-1438676347%2FFunctions%2F-1476930196) | [common]<br>abstract operator override fun [iterator](../../cl.ravenhill.keen.genetics.genotype/-contain-ops/index.md#-1438676347%2FFunctions%2F-1476930196)(): [Iterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)&lt;[G](index.md)&gt; |
