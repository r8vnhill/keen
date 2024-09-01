//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics](../index.md)/[Genotype](index.md)/[fold](fold.md)

# fold

[common]\
open override fun &lt;[R](fold.md)&gt; [fold](fold.md)(initial: [R](fold.md), operation: ([R](fold.md), [T](index.md)) -&gt; [R](fold.md)): [R](fold.md)

Folds the values of all genes in the genotype from left to right, accumulating a result.

The `fold` function allows you to reduce the entire genotype to a single value by applying a binary operation to an initial value and each gene's value within each chromosome, processing elements sequentially from the first gene in the first chromosome to the last gene in the last chromosome. This is particularly useful for operations that require aggregating or combining values across all genes in the genotype.

### Example: Summing Gene Values in the Genotype

Suppose you want to calculate the sum of all gene values across all chromosomes in the genotype:

```kotlin
val chromosome1 = IntChromosome(IntGene(1), IntGene(2))
val chromosome2 = IntChromosome(IntGene(3), IntGene(4))
val genotype = Genotype(chromosome1, chromosome2)
val totalGeneValue = genotype.fold(0) { acc, value -> acc + value }
println(totalGeneValue) // Output: 10
```

#### Return

The final accumulated result after processing all genes from left to right.

#### Parameters

common

| | |
|---|---|
| R | The type of the result produced by the fold operation. |
| initial | The initial value to start the accumulation with. |
| operation | The binary operation to apply to the accumulator and each gene's value. |
