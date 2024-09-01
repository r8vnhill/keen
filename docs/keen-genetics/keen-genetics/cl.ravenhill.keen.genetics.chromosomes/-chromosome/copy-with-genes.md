//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics.chromosomes](../index.md)/[Chromosome](index.md)/[copyWithGenes](copy-with-genes.md)

# copyWithGenes

[common]\
abstract fun [copyWithGenes](copy-with-genes.md)(newGenes: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[G](index.md)&gt;): [Chromosome](index.md)&lt;[T](index.md), [G](index.md)&gt;

Creates a new chromosome with a specified list of genes.

This method returns a new instance of the chromosome with the provided list of genes, preserving the other properties of the chromosome.

#### Return

A new `Chromosome` instance with the specified genes.

#### Parameters

common

| | |
|---|---|
| newGenes | The list of genes to use in the new chromosome. |
