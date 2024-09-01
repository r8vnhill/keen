//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.states](../index.md)/[EvolutionState](index.md)/[fold](fold.md)

# fold

[common]\
open override fun &lt;[R](fold.md)&gt; [fold](fold.md)(initial: [R](fold.md), operation: ([R](fold.md), [T](index.md)) -&gt; [R](fold.md)): [R](fold.md)

Folds the values in the population from left to right, accumulating a result.

The `fold` function allows you to reduce the entire population to a single value by applying a binary operation to an initial value and each element (i.e., each individual) in the population. The operation is applied sequentially from the first individual to the last, which makes it suitable for operations where the order of accumulation follows the sequence of individuals.

#### Return

The final accumulated result after processing all individuals from left to right.

#### Parameters

common

| | |
|---|---|
| R | The type of the result produced by the fold operation. |
| initial | The initial value to start the accumulation with. |
| operation | The binary operation to apply to the accumulator and each value in the population. |
