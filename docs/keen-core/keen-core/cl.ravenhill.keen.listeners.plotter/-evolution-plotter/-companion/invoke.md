//[keen-core](../../../../index.md)/[cl.ravenhill.keen.listeners.plotter](../../index.md)/[EvolutionPlotter](../index.md)/[Companion](index.md)/[invoke](invoke.md)

# invoke

[common]\
operator fun &lt;[T](invoke.md), [F](invoke.md) : [Feature](../../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](invoke.md), [F](invoke.md)&gt;, [R](invoke.md) : [Representation](../../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](invoke.md), [F](invoke.md)&gt;&gt; [invoke](invoke.md)(): ([ListenerConfiguration](../../../cl.ravenhill.keen.listeners/-listener-configuration/index.md)&lt;[T](invoke.md), [F](invoke.md), [R](invoke.md)&gt;) -&gt; [EvolutionPlotter](../index.md)&lt;[T](invoke.md), [F](invoke.md), [R](invoke.md), out [EvolutionState](../../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](invoke.md), [F](invoke.md), [R](invoke.md), *&gt;&gt;

Factory method for creating an `EvolutionPlotter` instance.

This method returns a function that, when invoked with a `ListenerConfiguration`, creates an instance of `EvolutionPlotter`.

#### Return

A function that creates an `EvolutionPlotter` instance with the provided configuration.
