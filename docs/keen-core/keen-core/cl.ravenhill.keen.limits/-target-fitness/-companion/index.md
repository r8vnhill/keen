//[keen-core](../../../../index.md)/[cl.ravenhill.keen.limits](../../index.md)/[TargetFitness](../index.md)/[Companion](index.md)

# Companion

[common]\
object [Companion](index.md)

## Functions

| Name | Summary |
|---|---|
| [invoke](invoke.md) | [common]<br>operator fun &lt;[T](invoke.md), [F](invoke.md) : [Feature](../../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](invoke.md), [F](invoke.md)&gt;, [R](invoke.md) : [Representation](../../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](invoke.md), [F](invoke.md)&gt;, [S](invoke.md) : [EvolutionState](../../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](invoke.md), [F](invoke.md), [R](invoke.md), [S](invoke.md)&gt;&gt; [invoke](invoke.md)(targetFitness: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)): ([ListenerConfiguration](../../../cl.ravenhill.keen.listeners/-listener-configuration/index.md)&lt;[T](invoke.md), [F](invoke.md), [R](invoke.md)&gt;) -&gt; [TargetFitness](../index.md)&lt;[T](invoke.md), [F](invoke.md), [R](invoke.md), [S](invoke.md)&gt;<br>Creates a `TargetFitness` limit condition based on a specific fitness value.<br>[common]<br>operator fun &lt;[T](invoke.md), [F](invoke.md) : [Feature](../../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](invoke.md), [F](invoke.md)&gt;, [R](invoke.md) : [Representation](../../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](invoke.md), [F](invoke.md)&gt;, [S](invoke.md) : [EvolutionState](../../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](invoke.md), [F](invoke.md), [R](invoke.md), [S](invoke.md)&gt;&gt; [invoke](invoke.md)(targetFitness: ([Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)) -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): ([ListenerConfiguration](../../../cl.ravenhill.keen.listeners/-listener-configuration/index.md)&lt;[T](invoke.md), [F](invoke.md), [R](invoke.md)&gt;) -&gt; [TargetFitness](../index.md)&lt;[T](invoke.md), [F](invoke.md), [R](invoke.md), [S](invoke.md)&gt;<br>Creates a `TargetFitness` limit condition based on a custom fitness condition. |
