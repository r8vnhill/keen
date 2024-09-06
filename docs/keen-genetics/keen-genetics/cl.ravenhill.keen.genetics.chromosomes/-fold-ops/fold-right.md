//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics.chromosomes](../index.md)/[FoldOps](index.md)/[foldRight](fold-right.md)

# foldRight

[common]\
open override fun &lt;[R](fold-right.md)&gt; [foldRight](fold-right.md)(initial: [R](fold-right.md), operation: ([T](index.md), [R](fold-right.md)) -&gt; [R](fold-right.md)): [R](fold-right.md)

Folds the genes in the chromosome from right to left, accumulating a result.

This method starts with an initial value and processes each gene in the chromosome from right to left, applying the given binary operation to each gene's value and the accumulator. The result is accumulated step by step, starting from the last gene and moving toward the first.

### Example: Building a String Representation of Gene Values in Reverse Order

Suppose you have a chromosome where each gene holds a character, and you want to build a string that represents the gene values in reverse order:

```kotlin
val chromosome: Chromosome<Char, MyGene> = // obtain a chromosome instance
val reversedGeneString = chromosome.foldRight("") { value, acc -> value + acc }
println(reversedGeneString) // Output: the gene values concatenated in reverse order
```

## Efficiency Considerations:

- 
   **Folding Left (**`fold`**)**: Efficient when the order of operations naturally follows the structure's sequence. For example, summing values or processing genes in their natural order.
- 
   **Folding Right (**`foldRight`**)**: More efficient for right-associative operations, such as when building results from the last element or constructing a data structure that depends on the order starting from the end.

#### Return

The final accumulated result after processing all genes from right to left.

#### Parameters

common

| | |
|---|---|
| R | The type of the result produced by the fold operation. |
| initial | The initial value to start the accumulation with. |
| operation | The binary operation to apply to each gene's value and the accumulator. |
