//[keen-core](../../../../index.md)/[cl.ravenhill.keen.listeners.summary](../../index.md)/[EvolutionSummary](../index.md)/[Companion](index.md)/[invoke](invoke.md)

# invoke

[common]\
operator fun &lt;[T](invoke.md), [F](invoke.md) : [Feature](../../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](invoke.md), [F](invoke.md)&gt;, [R](invoke.md) : [Representation](../../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](invoke.md), [F](invoke.md)&gt;&gt; [invoke](invoke.md)(): ([ListenerConfiguration](../../../cl.ravenhill.keen.listeners/-listener-configuration/index.md)&lt;[T](invoke.md), [F](invoke.md), [R](invoke.md)&gt;) -&gt; [EvolutionSummary](../index.md)&lt;[T](invoke.md), [F](invoke.md), [R](invoke.md), out [EvolutionState](../../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](invoke.md), [F](invoke.md), [R](invoke.md), *&gt;&gt;

Factory function to create an `EvolutionSummary` listener.

The `invoke` function provides a convenient way to instantiate an `EvolutionSummary` listener using a functional approach. It returns a lambda function that takes a `ListenerConfiguration` and produces a new `EvolutionSummary` instance.

#### Return

A lambda function that takes a `ListenerConfiguration` and returns an `EvolutionSummary` instance.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features. |
| F | The type of the feature, which must extend [Feature](../../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of the representation, which must extend [Representation](../../../cl.ravenhill.keen.repr/-representation/index.md). |
| S | The type of the evolutionary state, which must extend [EvolutionState](../../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md). |
