//[keen-core](../../../index.md)/[cl.ravenhill.keen](../index.md)/[Individual](index.md)/[fold](fold.md)

# fold

[common]\
open override fun &lt;[R](fold.md)&gt; [fold](fold.md)(initial: [R](fold.md), operation: ([R](fold.md), [T](index.md)) -&gt; [R](fold.md)): [R](fold.md)

Folds the values in the individual's representation from left to right, accumulating a result.

The `fold` function allows you to reduce the individual's representation to a single value by applying a binary operation to an initial value and each element in the representation, processing elements from left to right. This is useful for operations like summing values, combining elements, or any aggregation task where the order of processing follows the sequence of elements in the representation.

### Example: Summing Gene Values in the Representation

Suppose you have an individual with a genotype where each gene holds an integer value, and you want to calculate the sum of these values:

```kotlin
val gene1: IntGene = IntGene(1, 0..10)
val gene2 = IntGene(2, 0..10)
val chromosome = IntChromosome(gene1, gene2)
val representation = Genotype(chromosome)
val individual = Individual(representation)
val sumOfGeneValues = individual.fold(0) { acc, value -> acc + value }
println(sumOfGeneValues) // Output: 3
```

#### Return

The final accumulated result after processing all values from left to right.

#### Parameters

common

| | |
|---|---|
| R | The type of the result produced by the fold operation. |
| initial | The initial value to start the accumulation with. |
| operation | The binary operation to apply to the accumulator and each value in the representation. |
