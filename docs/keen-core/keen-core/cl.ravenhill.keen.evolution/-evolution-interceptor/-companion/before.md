//[keen-core](../../../../index.md)/[cl.ravenhill.keen.evolution](../../index.md)/[EvolutionInterceptor](../index.md)/[Companion](index.md)/[before](before.md)

# before

[common]\
fun &lt;[T](before.md), [F](before.md), [R](before.md), [S](before.md)&gt; [before](before.md)(before: ([S](before.md)) -&gt; [S](before.md)): [EvolutionInterceptor](../index.md)&lt;[T](before.md), [F](before.md), [R](before.md), [S](before.md)&gt;

Returns an `EvolutionInterceptor` that only applies a given `before` function, leaving the `after` function as a no-op.

#### Parameters

common

| | |
|---|---|
| before | The function to apply to the state before the evolutionary step. |
