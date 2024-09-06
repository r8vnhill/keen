//[keen-core](../../../index.md)/[cl.ravenhill.keen.ranking](../index.md)/[IndividualRanker](index.md)/[fitnessTransform](fitness-transform.md)

# fitnessTransform

[common]\
open fun [fitnessTransform](fitness-transform.md)(fitness: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)&gt;

Transforms a list of fitness values.

This method provides an optional transformation step that can be applied to a list of fitness values before they are used for comparison or sorting. Implementations can override this method to apply custom transformations such as scaling, normalization, or other adjustments to the fitness values.

#### Return

The transformed list of fitness values.

#### Parameters

common

| | |
|---|---|
| fitness | The list of fitness values to transform. |
