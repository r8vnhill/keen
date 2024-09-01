//[keen-core](../../../../index.md)/[cl.ravenhill.keen.listeners.printer](../../index.md)/[EvolutionPrinter](../index.md)/[Companion](index.md)/[invoke](invoke.md)

# invoke

[common]\
operator fun &lt;[T](invoke.md), [F](invoke.md) : [Feature](../../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](invoke.md), [F](invoke.md)&gt;, [R](invoke.md) : [Representation](../../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](invoke.md), [F](invoke.md)&gt;&gt; [invoke](invoke.md)(every: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): ([ListenerConfiguration](../../../cl.ravenhill.keen.listeners/-listener-configuration/index.md)&lt;[T](invoke.md), [F](invoke.md), [R](invoke.md)&gt;) -&gt; [EvolutionPrinter](../index.md)&lt;[T](invoke.md), [F](invoke.md), [R](invoke.md), out [EvolutionState](../../../cl.ravenhill.keen.evolution.states/-evolution-state/index.md)&lt;[T](invoke.md), [F](invoke.md), [R](invoke.md), *&gt;&gt;

Factory function for creating an `EvolutionPrinter` with the specified interval.

#### Return

A function that takes a [ListenerConfiguration](../../../cl.ravenhill.keen.listeners/-listener-configuration/index.md) and returns an [EvolutionPrinter](../index.md) instance.

#### Parameters

common

| | |
|---|---|
| every | The interval (in generations) at which the evolution details should be printed. |
