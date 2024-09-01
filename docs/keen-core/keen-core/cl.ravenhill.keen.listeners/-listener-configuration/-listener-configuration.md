//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners](../index.md)/[ListenerConfiguration](index.md)/[ListenerConfiguration](-listener-configuration.md)

# ListenerConfiguration

[common]\
constructor(ranker: [IndividualRanker](../../cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; = FitnessMaxRanker(), evolution: [EvolutionRecord](../../cl.ravenhill.keen.listeners.records/-evolution-record/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; = EvolutionRecord(), timeSource: [TimeSource](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-time-source/index.html) = TimeSource.Monotonic, precision: [TimePrecision](../../cl.ravenhill.keen.listeners.precision/-time-precision/index.md) = WholeMilliseconds)

#### Parameters

common

| | |
|---|---|
| T | The type of value stored by the feature. |
| F | The kind of feature stored in a representation, which must implement [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of representation used by the individual, which must implement [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |
