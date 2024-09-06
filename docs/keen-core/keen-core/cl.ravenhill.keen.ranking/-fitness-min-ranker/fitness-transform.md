//[keen-core](../../../index.md)/[cl.ravenhill.keen.ranking](../index.md)/[FitnessMinRanker](index.md)/[fitnessTransform](fitness-transform.md)

# fitnessTransform

[common]\
open override fun [fitnessTransform](fitness-transform.md)(fitness: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;

Transforms a list of fitness values by inverting them relative to the sum of the fitness values.

#### Return

A list of transformed fitness values, where each value is the difference between the sum of all fitness values and the original fitness value.

#### Parameters

common

| | |
|---|---|
| fitness | The list of fitness values to transform. |
