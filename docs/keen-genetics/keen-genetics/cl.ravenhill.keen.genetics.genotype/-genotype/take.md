//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics.genotype](../index.md)/[Genotype](index.md)/[take](take.md)

# take

[common]\
open override fun [take](take.md)(n: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): Either&lt;CompositeException, [Genotype](index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;

Retains the first [n](take.md) chromosomes from the genotype, returning a new genotype with only the first [n](take.md) chromosomes.

The `take` method retains a specified number of chromosomes from the beginning of the genotype. It validates that the number of chromosomes to take is within the valid range (0 to the size of the genotype). If valid, a new genotype is returned; otherwise, an error is returned.

#### Return

An Either containing a new `Genotype` with the first [n](take.md) chromosomes, or a CompositeException if the number of chromosomes to take is out of range.

#### Parameters

common

| | |
|---|---|
| n | The number of chromosomes to retain from the beginning. |
