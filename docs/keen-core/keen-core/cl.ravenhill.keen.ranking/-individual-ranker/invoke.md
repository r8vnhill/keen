//[keen-core](../../../index.md)/[cl.ravenhill.keen.ranking](../index.md)/[IndividualRanker](index.md)/[invoke](invoke.md)

# invoke

[common]\
abstract operator fun [invoke](invoke.md)(first: [Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;, second: [Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)

Compares two individuals based on their fitness.

#### Return

A negative integer, zero, or a positive integer if the first individual's fitness is less than, equal to, or greater than the second individual's fitness, respectively.

#### Parameters

common

| | |
|---|---|
| first | The first individual to compare. |
| second | The second individual to compare. |
