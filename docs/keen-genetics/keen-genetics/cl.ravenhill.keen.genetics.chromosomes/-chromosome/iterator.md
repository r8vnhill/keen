//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics.chromosomes](../index.md)/[Chromosome](index.md)/[iterator](iterator.md)

# iterator

[common]\
open operator override fun [iterator](iterator.md)(): [Iterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)&lt;[G](index.md)&gt;

Provides an iterator over the genes in the chromosome.

This method allows for sequential traversal of the genes within the chromosome.

## Usage:

The iterator can be used in a `for` loop or other iterative constructs to process each gene in the chromosome.

### Example 1: Iterating Over Genes

```kotlin
val chromosome: Chromosome<Int, MyGene> = // obtain a chromosome instance
for (gene in chromosome) {
    println(gene)
}
```

### Example 2: Using the Iterator Directly

```kotlin
val chromosome: Chromosome<Int, MyGene> = // obtain a chromosome instance
val iterator = chromosome.iterator()
while (iterator.hasNext()) {
    val gene = iterator.next()
    println(gene)
}
```

#### Return

An iterator over the genes in the chromosome.
