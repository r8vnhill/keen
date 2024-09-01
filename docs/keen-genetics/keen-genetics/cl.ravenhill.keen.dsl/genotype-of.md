//[keen-genetics](../../index.md)/[cl.ravenhill.keen.dsl](index.md)/[genotypeOf](genotype-of.md)

# genotypeOf

[common]\
fun &lt;[T](genotype-of.md), [G](genotype-of.md) : [Gene](../cl.ravenhill.keen.genetics.genes/-gene/index.md)&lt;[T](genotype-of.md), [G](genotype-of.md)&gt;&gt; [genotypeOf](genotype-of.md)(init: [GenotypeScope](-genotype-scope/index.md)&lt;[T](genotype-of.md), [G](genotype-of.md)&gt;.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [GenotypeFactory](../cl.ravenhill.keen.genetics/-genotype-factory/index.md)&lt;[T](genotype-of.md), [G](genotype-of.md)&gt;

Creates a new Genotype with the given [init](genotype-of.md) block.

Use this function to create a new Genotype instance with the specified chromosomes. The [init](genotype-of.md) block takes a [GenotypeScope](-genotype-scope/index.md) instance, which can be used to add chromosomes to the genotype. Chromosomes can be specified using the [chromosomeOf](chromosome-of.md) function, which takes a lambda that returns a Chromosome.Factory instance.

**Example usage:**

```kotlin
genotype {
    chromosome {
        booleans { }
    }
}
```

#### Return

A Genotype.Factory instance that contains the Chromosome.Factorys created by the [init](genotype-of.md) block.

#### Parameters

common

| | |
|---|---|
| init | A lambda block that allows configuring the genotype by specifying its chromosomes. |
