//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics.genotype](../index.md)/[Genotype](index.md)/[drop](drop.md)

# drop

[common]\
open override fun [drop](drop.md)(n: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): Either&lt;CompositeException, [Genotype](index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;

Removes the first [n](drop.md) chromosomes from the genotype, returning a new genotype with the remaining chromosomes.

The `drop` method removes a specified number of chromosomes from the beginning of the genotype. It validates that the number of chromosomes to drop is within the valid range (0 to the size of the genotype). If valid, a new genotype is returned; otherwise, an error is returned.

#### Return

An Either containing a new `Genotype` with the first [n](drop.md) chromosomes removed, or a CompositeException if the number of chromosomes to drop is out of range.

#### Parameters

common

| | |
|---|---|
| n | The number of chromosomes to remove from the beginning. |
