//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics.genotype](../index.md)/[ContainOps](index.md)

# ContainOps

interface [ContainOps](index.md)&lt;[T](index.md), [G](index.md) : [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; : [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; 

Provides operations for checking the containment of chromosomes within a genotype.

The `ContainOps` interface extends the [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html) interface and provides basic functionality to check whether a genotype contains specific chromosomes or a collection of chromosomes. It operates on a list of [Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md) instances, offering methods for efficient containment checks.

## Usage:

This interface is typically mixed into classes representing genotypes or genetic structures in evolutionary algorithms. It allows you to verify the presence of certain chromosomes within the genotype.

### Example: Implementing ContainOps in a Genotype

```kotlin
class Genotype<T, G>(override val chromosomes: List<Chromosome<T, G>>) : ContainOps<T, G> where G : Gene<T, G> {
    // Other Genotype-specific methods and properties
}

val genotype = Genotype(listOf(chromosome1, chromosome2))
println(genotype.contains(chromosome1))  // Output: true
```

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the genes in the chromosomes. |
| G | The type of gene within the chromosomes, which must extend [Gene](../../cl.ravenhill.keen.genetics.genes/-gene/index.md). |

#### Inheritors

| |
|---|
| [Genotype](../-genotype/index.md) |

## Properties

| Name | Summary |
|---|---|
| [chromosomes](chromosomes.md) | [common]<br>abstract val [chromosomes](chromosomes.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;<br>The list of chromosomes within the genotype. |
| [size](index.md#-113084078%2FProperties%2F-1476930196) | [common]<br>abstract val [size](index.md#-113084078%2FProperties%2F-1476930196): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Functions

| Name | Summary |
|---|---|
| [contains](contains.md) | [common]<br>open operator override fun [contains](contains.md)(element: [Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Checks if the genotype contains the specified chromosome. |
| [containsAll](contains-all.md) | [common]<br>open override fun [containsAll](contains-all.md)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Checks if the genotype contains all the specified chromosomes. |
| [isEmpty](index.md#-719293276%2FFunctions%2F-1476930196) | [common]<br>abstract fun [isEmpty](index.md#-719293276%2FFunctions%2F-1476930196)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [iterator](index.md#-1438676347%2FFunctions%2F-1476930196) | [common]<br>abstract operator override fun [iterator](index.md#-1438676347%2FFunctions%2F-1476930196)(): [Iterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)&lt;[Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt; |
