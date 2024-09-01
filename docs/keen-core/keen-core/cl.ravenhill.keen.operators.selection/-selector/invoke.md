//[keen-core](../../../index.md)/[cl.ravenhill.keen.operators.selection](../index.md)/[Selector](index.md)/[invoke](invoke.md)

# invoke

[common]\
open suspend operator override fun &lt;[S](invoke.md) : [EvolutionState](../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](index.md), [F](index.md), [R](index.md), [S](invoke.md)&gt;&gt; [invoke](invoke.md)(state: [S](invoke.md), outputSize: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), buildState: ([List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;) -&gt; [S](invoke.md)): Either&lt;[SelectionException](../../cl.ravenhill.keen.exceptions/-selection-exception/index.md), [S](invoke.md)&gt;

Applies the selection process to the given evolutionary state, producing a new state with the selected individuals.

This function is responsible for verifying that the population is not empty and that the selection count is non-negative. It then invokes the `select` method to perform the selection and ensures that the output size matches the expected size. The method returns an `Either` type, indicating either a successful state or a `SelectionException` if an error occurs.

#### Return

An `Either` containing the new evolutionary state on success, or a `SelectionException` on failure.

#### Parameters

common

| | |
|---|---|
| S | The type of the evolutionary state. |
| state | The current evolutionary state. |
| outputSize | The number of individuals to select. |
| buildState | A function that builds a new state from the selected individuals. |
