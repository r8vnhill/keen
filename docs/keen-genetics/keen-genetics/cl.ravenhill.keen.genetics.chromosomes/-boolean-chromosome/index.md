//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics.chromosomes](../index.md)/[BooleanChromosome](index.md)

# BooleanChromosome

data class [BooleanChromosome](index.md)(val genes: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md)&gt;) : [Chromosome](../-chromosome/index.md)&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), [BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md)&gt; 

Represents a chromosome composed of boolean genes in an evolutionary algorithm.

The `BooleanChromosome` class is a concrete implementation of the `Chromosome` interface, specifically designed to hold a list of `BooleanGene` instances. Each `BooleanChromosome` represents a sequence of binary genetic information, with each gene being either `True` or `False`. This structure is commonly used in genetic algorithms where binary encoding is employed, such as in genetic optimization problems.

## Usage:

The `BooleanChromosome` class can be used in evolutionary algorithms to represent individuals that have binary traits. It provides methods to duplicate the chromosome with a new set of genes, which is essential for operations like mutation, crossover, and selection within the genetic algorithm.

### Example 1: Creating a Boolean Chromosome

```kotlin
val gene1 = BooleanGene.True
val gene2 = BooleanGene.False
val chromosome = BooleanChromosome(listOf(gene1, gene2))
println(chromosome.genes) // Output: [True, False]
```

### Example 2: Duplicating a Chromosome with New Genes

```kotlin
val chromosome = BooleanChromosome(listOf(BooleanGene.True, BooleanGene.False))
val newGenes = listOf(BooleanGene.False, BooleanGene.True)
val newChromosome = chromosome.copyWithGenes(newGenes)
println(newChromosome.genes) // Output: [False, True]
```

#### Parameters

common

| | |
|---|---|
| genes | The list of boolean genes that make up the chromosome. Each gene in the list is either `True` or `False`, representing binary genetic information. |

## Constructors

| | |
|---|---|
| [BooleanChromosome](-boolean-chromosome.md) | [common]<br>constructor(genes: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md)&gt;) |

## Properties

| Name | Summary |
|---|---|
| [genes](genes.md) | [common]<br>open override val [genes](genes.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md)&gt; |
| [size](../-chromosome/size.md) | [common]<br>open override val [size](../-chromosome/size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The size of the chromosome, representing the number of genes it contains. |

## Functions

| Name | Summary |
|---|---|
| [contains](index.md#-162937532%2FFunctions%2F-1476930196) | [common]<br>open operator override fun [contains](index.md#-162937532%2FFunctions%2F-1476930196)(element: [BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Checks if the chromosome contains the specified gene. |
| [containsAll](index.md#915395467%2FFunctions%2F-1476930196) | [common]<br>open override fun [containsAll](index.md#915395467%2FFunctions%2F-1476930196)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Checks if the chromosome contains all the specified genes. |
| [copyWithGenes](copy-with-genes.md) | [common]<br>open override fun [copyWithGenes](copy-with-genes.md)(newGenes: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md)&gt;): [BooleanChromosome](index.md)<br>Creates a copy of the chromosome with a new list of genes. |
| [flatMap](index.md#-2061483543%2FFunctions%2F-1476930196) | [common]<br>open fun &lt;[R](index.md#-2061483543%2FFunctions%2F-1476930196)&gt; [flatMap](index.md#-2061483543%2FFunctions%2F-1476930196)(f: ([Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) -&gt; [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[R](index.md#-2061483543%2FFunctions%2F-1476930196)&gt;): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[R](index.md#-2061483543%2FFunctions%2F-1476930196)&gt; |
| [flatten](../-chromosome/flatten.md) | [common]<br>open override fun [flatten](../-chromosome/flatten.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)&gt;<br>Flattens the chromosome to a list containing the values of all its genes. |
| [fold](index.md#1960181681%2FFunctions%2F-1476930196) | [common]<br>open override fun &lt;[R](index.md#1960181681%2FFunctions%2F-1476930196)&gt; [fold](index.md#1960181681%2FFunctions%2F-1476930196)(initial: [R](index.md#1960181681%2FFunctions%2F-1476930196), operation: ([R](index.md#1960181681%2FFunctions%2F-1476930196), [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) -&gt; [R](index.md#1960181681%2FFunctions%2F-1476930196)): [R](index.md#1960181681%2FFunctions%2F-1476930196)<br>Folds the genes in the chromosome from left to right, accumulating a result. |
| [foldRight](index.md#-1116525193%2FFunctions%2F-1476930196) | [common]<br>open override fun &lt;[R](index.md#-1116525193%2FFunctions%2F-1476930196)&gt; [foldRight](index.md#-1116525193%2FFunctions%2F-1476930196)(initial: [R](index.md#-1116525193%2FFunctions%2F-1476930196), operation: ([Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), [R](index.md#-1116525193%2FFunctions%2F-1476930196)) -&gt; [R](index.md#-1116525193%2FFunctions%2F-1476930196)): [R](index.md#-1116525193%2FFunctions%2F-1476930196)<br>Folds the genes in the chromosome from right to left, accumulating a result. |
| [get](../-chromosome/get.md) | [common]<br>open operator fun [get](../-chromosome/get.md)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): Either&lt;CompositeException, [BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md)&gt;<br>Retrieves the gene at the specified index within the chromosome. |
| [isEmpty](../-chromosome/is-empty.md) | [common]<br>open override fun [isEmpty](../-chromosome/is-empty.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Checks if the chromosome is empty, meaning it contains no genes. |
| [iterator](../-chromosome/iterator.md) | [common]<br>open operator override fun [iterator](../-chromosome/iterator.md)(): [Iterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)&lt;[BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md)&gt;<br>Provides an iterator over the genes in the chromosome. |
| [toString](to-string.md) | [common]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Generates a string representation of the BooleanChromosome object based on the current toStringMode. |
| [verify](../-chromosome/verify.md) | [common]<br>open override fun [verify](../-chromosome/verify.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Verifies the correctness or validity of the chromosome. |
