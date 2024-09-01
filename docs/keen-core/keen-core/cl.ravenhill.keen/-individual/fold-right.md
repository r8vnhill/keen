//[keen-core](../../../index.md)/[cl.ravenhill.keen](../index.md)/[Individual](index.md)/[foldRight](fold-right.md)

# foldRight

[common]\
open override fun &lt;[R](fold-right.md)&gt; [foldRight](fold-right.md)(initial: [R](fold-right.md), operation: ([T](index.md), [R](fold-right.md)) -&gt; [R](fold-right.md)): [R](fold-right.md)

Folds the values in the individual's representation from right to left, accumulating a result.

The `foldRight` function allows you to reduce the individual's representation to a single value by applying a binary operation to each element in the representation and an initial value, processing elements from right to left. This is useful for operations where the order of processing is important and should start from the last element and move towards the first, such as building a result in reverse order.

### Example: Building a String Representation of Gene Values in Reverse Order

Suppose you have an individual with a genotype where each gene holds a character, and you want to build a string that represents the gene values in reverse order:

```kotlin
val gene1 = CharGene('A')
val gene2 = CharGene('B')
val chromosome = CharChromosome(gene1, gene2)
val representation = Genotype(chromosome)
val individual = Individual(representation)
val reversedGeneString = individual.foldRight("") { value, acc -> value + acc }
println(reversedGeneString) // Output: "BA"
```

## Efficiency Considerations:

- 
   **Folding Left (**`fold`**)**: Efficient when the accumulation order naturally follows the sequence of elements. Ideal for linear data structures like lists when you need to process elements in their original order.
- 
   **Folding Right (**`foldRight`**)**: Useful for right-associative operations, such as when the last element has more significance, or when building results in reverse order or from the end of a structure.

#### Return

The final accumulated result after processing all values from right to left.

#### Parameters

common

| | |
|---|---|
| R | The type of the result produced by the fold operation. |
| initial | The initial value to start the accumulation with. |
| operation | The binary operation to apply to each value in the representation and the accumulator. |
