//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.engines](../index.md)/[AlterationEngine](index.md)/[alter](alter.md)

# alter

[common]\
abstract suspend fun [alter](alter.md)(state: [S](index.md)): Either&lt;[AlterationException](../../cl.ravenhill.keen.exceptions/-alteration-exception/index.md), [S](index.md)&gt;

Performs genetic alterations on the population within the given evolutionary state.

The `alter` function is responsible for applying one or more genetic alteration operations to the individuals in the population. These alterations could include mutation, crossover, or other transformations that modify the genetic makeup of the population. The function is a `suspend` function, allowing it to be used in asynchronous contexts, which is particularly useful in large-scale evolutionary algorithms where alterations may involve complex computations.

#### Return

The new evolutionary state after applying the genetic alterations.

#### Parameters

common

| | |
|---|---|
| state | The current evolutionary state that contains the population to be altered. |
