//[keen-core](../../../../index.md)/[cl.ravenhill.keen.limits](../../index.md)/[Limit](../index.md)/[Companion](index.md)

# Companion

[common]\
object [Companion](index.md)

## Functions

| Name | Summary |
|---|---|
| [invoke](invoke.md) | [common]<br>operator fun &lt;[T](invoke.md), [F](invoke.md) : [Feature](../../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](invoke.md), [F](invoke.md)&gt;, [R](invoke.md) : [Representation](../../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](invoke.md), [F](invoke.md)&gt;, [S](invoke.md) : [EvolutionState](../../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](invoke.md), [F](invoke.md), [R](invoke.md), [S](invoke.md)&gt;, [L](invoke.md) : [Listener](../../../cl.ravenhill.keen.listeners/-listener/index.md)&gt; [invoke](invoke.md)(builder: ([ListenerConfiguration](../../../cl.ravenhill.keen.listeners/-listener-configuration/index.md)&lt;[T](invoke.md), [F](invoke.md), [R](invoke.md)&gt;) -&gt; [L](invoke.md), predicate: [L](invoke.md).([S](invoke.md)) -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): ([ListenerConfiguration](../../../cl.ravenhill.keen.listeners/-listener-configuration/index.md)&lt;[T](invoke.md), [F](invoke.md), [R](invoke.md)&gt;) -&gt; [Limit](../index.md)&lt;[T](invoke.md), [F](invoke.md), [R](invoke.md), [S](invoke.md), [L](invoke.md)&gt;<br>Creates a new [Limit](../index.md) instance using the provided listener configuration and predicate function. |
