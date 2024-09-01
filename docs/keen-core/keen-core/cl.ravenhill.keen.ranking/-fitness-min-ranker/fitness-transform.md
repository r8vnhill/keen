//[keen-core](../../../index.md)/[cl.ravenhill.keen.ranking](../index.md)/[FitnessMinRanker](index.md)/[fitnessTransform](fitness-transform.md)

# fitnessTransform

[common]\
open override fun [fitnessTransform](fitness-transform.md)(fitness: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;

Transforms a list of fitness values by inverting them.

This method maps each fitness value to the difference between the sum of all fitness values and the individual fitness value. This transformation is useful in scenarios where minimizing fitness values is desired.

#### Return

The transformed list of fitness values.

#### Parameters

common

| | |
|---|---|
| fitness | The list of fitness values to transform. |
