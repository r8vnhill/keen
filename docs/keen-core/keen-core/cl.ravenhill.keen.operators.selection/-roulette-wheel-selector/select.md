//[keen-core](../../../index.md)/[cl.ravenhill.keen.operators.selection](../index.md)/[RouletteWheelSelector](index.md)/[select](select.md)

# select

[common]\
open suspend override fun [select](select.md)(population: [Population](../../cl.ravenhill.keen/-population/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;, count: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), ranker: [IndividualRanker](../../cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;): Either&lt;[SelectionException](../../cl.ravenhill.keen.exceptions/-selection-exception/index.md), [Population](../../cl.ravenhill.keen/-population/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;

Selects individuals from the population based on the roulette wheel selection mechanism.

The `select` method selects a specified number of individuals from the population based on their fitness values. The selection process can be influenced by the sorting strategy provided during initialization.

**Note**: This method is public to allow fine-tuning in the development of new algorithms and should be used primarily in that context. The recommended way to use this selector is through its [invoke](../../../../keen-core/cl.ravenhill.keen.operators.selection/-roulette-wheel-selector/invoke.md) operator.

#### Return

A list of selected individuals wrapped in an Either type, with a [SelectionException](../../cl.ravenhill.keen.exceptions/-selection-exception/index.md) on failure.

#### Parameters

common

| | |
|---|---|
| population | The current population of individuals from which to select. |
| count | The number of individuals to select. |
| ranker | The ranker used to evaluate and rank individuals within the population. |

#### Throws

| | |
|---|---|
| [SelectionException](../../cl.ravenhill.keen.exceptions/-selection-exception/index.md) | if the population is empty or if the selection process encounters an error. |
