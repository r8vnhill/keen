//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics.chromosomes](../index.md)/[BooleanChromosome](index.md)/[copyWithGenes](copy-with-genes.md)

# copyWithGenes

[common]\
open override fun [copyWithGenes](copy-with-genes.md)(newGenes: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[BooleanGene](../../cl.ravenhill.keen.genetics.genes/-boolean-gene/index.md)&gt;): [BooleanChromosome](index.md)

Creates a copy of the chromosome with a new list of genes.

The `copyWithGenes` method is used to generate a new `BooleanChromosome` with a specified set of genes. This method is crucial in genetic algorithms for operations like crossover, where a new chromosome is created by combining genes from parent chromosomes.

### Example:

```kotlin
val chromosome = BooleanChromosome(listOf(BooleanGene.True, BooleanGene.False))
val newGenes = listOf(BooleanGene.False, BooleanGene.True)
val newChromosome = chromosome.copyWithGenes(newGenes)
println(newChromosome.genes) // Output: [False, True]
```

#### Return

A new `BooleanChromosome` instance with the specified genes.

#### Parameters

common

| | |
|---|---|
| newGenes | The new list of `BooleanGene` instances to replace the current genes in the chromosome. |
