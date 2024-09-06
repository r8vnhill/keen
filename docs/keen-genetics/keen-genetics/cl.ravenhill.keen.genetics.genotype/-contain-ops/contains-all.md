//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics.genotype](../index.md)/[ContainOps](index.md)/[containsAll](contains-all.md)

# containsAll

[common]\
open override fun [containsAll](contains-all.md)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

Checks if the genotype contains all the specified chromosomes.

The `containsAll` method verifies whether all chromosomes in the provided collection are present within the genotype. This is useful for checking if a specific subset of chromosomes exists in the genotype.

#### Return

`true` if the genotype contains all the specified chromosomes, `false` otherwise.

#### Parameters

common

| | |
|---|---|
| elements | The collection of chromosomes to check for containment. |
