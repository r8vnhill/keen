//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics](../index.md)/[Genotype](index.md)/[foldRight](fold-right.md)

# foldRight

[common]\
open override fun &lt;[R](fold-right.md)&gt; [foldRight](fold-right.md)(initial: [R](fold-right.md), operation: ([T](index.md), [R](fold-right.md)) -&gt; [R](fold-right.md)): [R](fold-right.md)

Folds the values of all genes in the genotype from right to left, accumulating a result.

The `foldRight` function allows you to reduce the entire genotype to a single value by applying a binary operation to each gene's value within each chromosome and an initial value, processing elements from the last gene in the last chromosome to the first gene in the first chromosome. This is particularly useful for operations where the order of processing should start from the end of the genotype and move towards the beginning.

### Example: Creating a String Representation of Genes in Reverse Order

Suppose you want to build a string that represents the values of all genes across all chromosomes in reverse order:

```kotlin
val chromosome1 = CharChromosome(CharGene('A'), CharGene('B'))
val chromosome2 = CharChromosome(CharGene('C'), CharGene('D'))
val genotype = Genotype(chromosome1, chromosome2)
val reversedGeneString = genotype.foldRight("") { value, acc -> value + acc }
println(reversedGeneString) // Output: "DCBA"
```

## Efficiency Considerations:

- 
   **Folding Left (**`fold`**)**: Efficient for operations where the accumulation order follows the sequence of genes from first to last. Ideal for summing values or combining results in the natural order of the genotype.
- 
   **Folding Right (**`foldRight`**)**: More efficient for operations where accumulation should start from the last gene and proceed to the first, such as when building results that depend on the reverse order of the genes.

#### Return

The final accumulated result after processing all genes from right to left.

#### Parameters

common

| | |
|---|---|
| R | The type of the result produced by the fold operation. |
| initial | The initial value to start the accumulation with. |
| operation | The binary operation to apply to each gene's value and the accumulator. |
