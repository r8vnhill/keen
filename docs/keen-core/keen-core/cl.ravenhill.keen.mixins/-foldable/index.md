//[keen-core](../../../index.md)/[cl.ravenhill.keen.mixins](../index.md)/[Foldable](index.md)

# Foldable

interface [Foldable](index.md)&lt;[T](index.md)&gt;

Represents a structure that can be folded or reduced to a single value in evolutionary algorithms.

The `Foldable` interface defines operations for folding a collection of elements into a single value by iteratively applying a binary operation. This is particularly useful in evolutionary algorithms for aggregating or processing data, such as summing the values of genes in a chromosome or combining results of genetic operations.

## Usage:

Implement this interface for data structures in evolutionary algorithms that need to be folded, such as chromosomes or genotypes. The `fold` and `foldRight` methods allow you to reduce these structures to a single value, starting from an initial value and processing each element according to a specified operation.

### Example 1: Folding to Calculate Sum of Gene Values

```kotlin
interface Chromosome<T, G> : Foldable<G> where G : Gene<T, G> {

    val genes: List<G>

    override fun <R> fold(initial: R, operation: (R, G) -> R): R {
        var result = initial
        for (gene in genes) {
            result = operation(result, gene)
        }
        return result
    }

    override fun <R> foldRight(initial: R, operation: (G, R) -> R): R {
        var result = initial
        for (gene in genes.reversed()) {
            result = operation(gene, result)
        }
        return result
    }
    // ... other methods and properties ...
}

// Example usage
val chromosome = object : Chromosome<Int, IntGene>
val sumOfGeneValues = chromosome.fold(0) { acc, gene -> acc + gene.value }  // Left fold, sum = 6
val sumOfGeneValuesRight = chromosome.foldRight(0) { gene, acc -> gene.value + acc }  // Right fold, sum = 6
```

### Example 2: Folding to Combine Genetic Information

```kotlin
val combinedString = chromosome.fold("") { acc, gene -> acc + gene.toString() }  // Left fold, combine gene strings
val combinedStringRight = chromosome.foldRight("") { gene, acc -> gene.toString() + acc }  // Right fold
```

## Efficiency Considerations:

- 
   **Folding Left (**`fold`**)**: Efficient for left-associative operations and when processing elements in the order they appear. Ideal for linear data structures like lists when accumulation starts from the beginning.
- 
   **Folding Right (**`foldRight`**)**: More efficient when combining results in a right-associative manner, such as when processing elements in reverse order. This can be beneficial when working with operations that depend on the last element or when building up results from the end of a structure.

#### Parameters

common

| | |
|---|---|
| T | The type of elements contained in the structure. |

#### Inheritors

| |
|---|
| [Individual](../../cl.ravenhill.keen/-individual/index.md) |
| [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md) |
| [Representation](../../cl.ravenhill.keen.repr/-representation/index.md) |

## Functions

| Name | Summary |
|---|---|
| [fold](fold.md) | [common]<br>abstract fun &lt;[R](fold.md)&gt; [fold](fold.md)(initial: [R](fold.md), operation: ([R](fold.md), [T](index.md)) -&gt; [R](fold.md)): [R](fold.md)<br>Folds the elements of the structure from left to right, accumulating a result. |
| [foldRight](fold-right.md) | [common]<br>abstract fun &lt;[R](fold-right.md)&gt; [foldRight](fold-right.md)(initial: [R](fold-right.md), operation: ([T](index.md), [R](fold-right.md)) -&gt; [R](fold-right.md)): [R](fold-right.md)<br>Folds the elements of the structure from right to left, accumulating a result. |
