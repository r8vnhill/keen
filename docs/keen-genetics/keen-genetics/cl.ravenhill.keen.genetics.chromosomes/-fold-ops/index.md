//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics.chromosomes](../index.md)/[FoldOps](index.md)

# FoldOps

interface [FoldOps](index.md)&lt;[T](index.md), [G](index.md) : [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; : [Foldable](../../../../keen-core/keen-core/cl.ravenhill.keen.mixins/-foldable/index.md)&lt;[T](index.md)&gt; 

Provides folding operations for genes in a chromosome.

The `FoldOps` class implements the [Foldable](../../../../keen-core/keen-core/cl.ravenhill.keen.mixins/-foldable/index.md) interface, offering methods to fold (reduce) the values of genes within a chromosome. These folding operations are performed from left to right or right to left, allowing for accumulation of results across the chromosome's genes. This is useful for various operations, such as summing values, building strings, or aggregating results based on the gene values.

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the genes. |
| G | The type of gene in the chromosome, which must extend [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md). |

#### Inheritors

| |
|---|
| [Chromosome](../-chromosome/index.md) |

## Properties

| Name | Summary |
|---|---|
| [genes](genes.md) | [common]<br>abstract val [genes](genes.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[G](index.md)&gt;<br>The list of genes within the chromosome. |

## Functions

| Name | Summary |
|---|---|
| [fold](fold.md) | [common]<br>open override fun &lt;[R](fold.md)&gt; [fold](fold.md)(initial: [R](fold.md), operation: ([R](fold.md), [T](index.md)) -&gt; [R](fold.md)): [R](fold.md)<br>Folds the genes in the chromosome from left to right, accumulating a result. |
| [foldRight](fold-right.md) | [common]<br>open override fun &lt;[R](fold-right.md)&gt; [foldRight](fold-right.md)(initial: [R](fold-right.md), operation: ([T](index.md), [R](fold-right.md)) -&gt; [R](fold-right.md)): [R](fold-right.md)<br>Folds the genes in the chromosome from right to left, accumulating a result. |
