//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics.genotype](../index.md)/[Genotype](index.md)/[iterator](iterator.md)

# iterator

[common]\
open operator override fun [iterator](iterator.md)(): [Iterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)&lt;[Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;

Provides an iterator over the chromosomes in the genotype.

The `iterator` method allows for iterating over each chromosome within the genotype. This is useful for traversing the genetic structure of an individual, enabling operations that require sequential access to each chromosome.

## Usage:

This method can be used in loops and other iterative constructs to process each chromosome individually.

### Example:

```kotlin
val chromosome1 = MyChromosome(MyGene(1), MyGene(2))
val chromosome2 = MyChromosome(MyGene(3), MyGene(4))
val genotype = Genotype(chromosome1, chromosome2)
for (chromosome in genotype) {
    println(chromosome)
}
// Output will be:
// Chromosome(genes=[Gene(1), Gene(2)])
// Chromosome(genes=[Gene(3), Gene(4)])
```

#### Return

An iterator over the chromosomes in the genotype.
