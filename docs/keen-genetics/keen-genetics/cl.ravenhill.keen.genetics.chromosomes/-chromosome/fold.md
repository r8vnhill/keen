//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics.chromosomes](../index.md)/[Chromosome](index.md)/[fold](fold.md)

# fold

[common]\
open override fun &lt;[R](fold.md)&gt; [fold](fold.md)(initial: [R](fold.md), operation: ([R](fold.md), [T](index.md)) -&gt; [R](fold.md)): [R](fold.md)

Folds the genes in the chromosome from left to right, accumulating a result.

This method starts with an initial value and processes each gene in the chromosome from left to right (i.e., in the order they appear), applying the given binary operation to the current accumulator and each gene's value. The result is accumulated step by step until the final result is obtained.

### Example: Calculating the Sum of Gene Values

Suppose you have a chromosome where each gene holds an integer value, and you want to calculate the sum of these values:

```kotlin
// Chromosome: [1, 2, 3, 4, 5]
val chromosome: Chromosome<Int, MyGene> = // obtain a chromosome instance
val sumOfGeneValues = chromosome.fold(0) { acc, value -> acc + value }
println(sumOfGeneValues) // Output: 15
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
