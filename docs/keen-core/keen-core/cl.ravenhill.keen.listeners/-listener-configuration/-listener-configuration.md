//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners](../index.md)/[ListenerConfiguration](index.md)/[ListenerConfiguration](-listener-configuration.md)

# ListenerConfiguration

[common]\
constructor(ranker: [IndividualRanker](../../cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; = defaultRanker(), evolution: [EvolutionRecord](../../cl.ravenhill.keen.listeners.records/-evolution-record/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; = defaultEvolutionRecord(), timeSource: [TimeSource](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-time-source/index.html) = defaultTimeSource, precision: [TimePrecision](../../cl.ravenhill.keen.listeners.precision/-time-precision/index.md) = defaultPrecision)

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the genes in the individuals. |
| F | The type of the feature used in the individual's representation. |
| R | The type of the representation used by the individual. |
