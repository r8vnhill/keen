//[keen-core](../../../../index.md)/[cl.ravenhill.keen.evolution](../../index.md)/[EvolutionInterceptor](../index.md)/[Companion](index.md)/[after](after.md)

# after

[common]\
fun &lt;[T](after.md), [F](after.md), [R](after.md), [S](after.md)&gt; [after](after.md)(after: ([S](after.md)) -&gt; [S](after.md)): [EvolutionInterceptor](../index.md)&lt;[T](after.md), [F](after.md), [R](after.md), [S](after.md)&gt;

Returns an `EvolutionInterceptor` that only applies a given `after` function, leaving the `before` function as a no-op.

#### Parameters

common

| | |
|---|---|
| after | The function to apply to the state after the evolutionary step. |
