//[keen-core](../../../index.md)/[cl.ravenhill.keen](../index.md)/[PopulationLike](index.md)/[map](map.md)

# map

[common]\
open fun [map](map.md)(f: ([Individual](../-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;) -&gt; [Individual](../-individual/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;): [Population](../-population/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;

Maps the individuals in the population using the provided function.

#### Return

A new population with the results of applying the function to each individual.

#### Parameters

common

| | |
|---|---|
| f | The function to apply to each individual in the population. |
