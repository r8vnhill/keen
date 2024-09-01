//[keen-core](../../../../index.md)/[cl.ravenhill.keen.evolution](../../index.md)/[EvolutionInterceptor](../index.md)/[Companion](index.md)

# Companion

[common]\
object [Companion](index.md)

## Functions

| Name | Summary |
|---|---|
| [after](after.md) | [common]<br>fun &lt;[T](after.md), [F](after.md), [R](after.md), [S](after.md)&gt; [after](after.md)(after: ([S](after.md)) -&gt; [S](after.md)): [EvolutionInterceptor](../index.md)&lt;[T](after.md), [F](after.md), [R](after.md), [S](after.md)&gt;<br>Returns an `EvolutionInterceptor` that only applies a given `after` function, leaving the `before` function as a no-op. |
| [before](before.md) | [common]<br>fun &lt;[T](before.md), [F](before.md), [R](before.md), [S](before.md)&gt; [before](before.md)(before: ([S](before.md)) -&gt; [S](before.md)): [EvolutionInterceptor](../index.md)&lt;[T](before.md), [F](before.md), [R](before.md), [S](before.md)&gt;<br>Returns an `EvolutionInterceptor` that only applies a given `before` function, leaving the `after` function as a no-op. |
| [identity](identity.md) | [common]<br>fun &lt;[T](identity.md), [F](identity.md), [R](identity.md), [S](identity.md)&gt; [identity](identity.md)(): [EvolutionInterceptor](../index.md)&lt;[T](identity.md), [F](identity.md), [R](identity.md), [S](identity.md)&gt;<br>Returns an `EvolutionInterceptor` that applies no changes to the state before or after the evolutionary step. |
