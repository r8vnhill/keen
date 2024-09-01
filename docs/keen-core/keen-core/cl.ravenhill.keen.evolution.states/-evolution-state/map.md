//[keen-core](../../../index.md)/[cl.ravenhill.keen.evolution.states](../index.md)/[EvolutionState](index.md)/[map](map.md)

# map

[common]\
open fun [map](map.md)(f: ([Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;) -&gt; [Individual](../../cl.ravenhill.keen/-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;): [S](index.md)

Applies a transformation function to each individual in the population and returns a new state with the transformed population.

#### Return

A new `EvolutionState` instance with the transformed population.

#### Parameters

common

| | |
|---|---|
| f | The transformation function to apply to each individual. |
