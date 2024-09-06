//[keen-core](../../../index.md)/[cl.ravenhill.keen.ranking](../index.md)/[IndividualRanker](index.md)/[comparator](comparator.md)

# comparator

[common]\
open val [comparator](comparator.md): [Comparator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-comparator/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;

A comparator that uses the ranker's comparison function to order individuals.

This property provides a standard `Comparator` instance based on the `invoke` method, which can be used to sort collections of individuals in a way that aligns with the ranker's logic.
