//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners](../index.md)/[ListenerConfiguration](index.md)

# ListenerConfiguration

data class [ListenerConfiguration](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;&gt;(val ranker: [IndividualRanker](../../cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; = FitnessMaxRanker(), val evolution: [EvolutionRecord](../../cl.ravenhill.keen.listeners.records/-evolution-record/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; = EvolutionRecord(), val timeSource: [TimeSource](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-time-source/index.html) = TimeSource.Monotonic, val precision: [TimePrecision](../../cl.ravenhill.keen.listeners.precision/-time-precision/index.md) = WholeMilliseconds)

Configuration for a listener that manages evolution and timing within an evolutionary algorithm.

#### Parameters

common

| | |
|---|---|
| T | The type of value stored by the feature. |
| F | The kind of feature stored in a representation, which must implement [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of representation used by the individual, which must implement [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |

## Constructors

| | |
|---|---|
| [ListenerConfiguration](-listener-configuration.md) | [common]<br>constructor(ranker: [IndividualRanker](../../cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; = FitnessMaxRanker(), evolution: [EvolutionRecord](../../cl.ravenhill.keen.listeners.records/-evolution-record/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; = EvolutionRecord(), timeSource: [TimeSource](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-time-source/index.html) = TimeSource.Monotonic, precision: [TimePrecision](../../cl.ravenhill.keen.listeners.precision/-time-precision/index.md) = WholeMilliseconds) |

## Properties

| Name | Summary |
|---|---|
| [currentGeneration](current-generation.md) | [common]<br>val [currentGeneration](current-generation.md): [MutableBox](../../cl.ravenhill.keen.utils.box/-mutable-box/index.md)&lt;[GenerationRecord](../../cl.ravenhill.keen.listeners.records/-generation-record/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;?&gt;<br>A mutable box containing the current generation record. Defaults to `null`. |
| [evolution](evolution.md) | [common]<br>val [evolution](evolution.md): [EvolutionRecord](../../cl.ravenhill.keen.listeners.records/-evolution-record/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;<br>The [EvolutionRecord](../../cl.ravenhill.keen.listeners.records/-evolution-record/index.md) that tracks the evolution process. Defaults to a new instance of [EvolutionRecord](../../cl.ravenhill.keen.listeners.records/-evolution-record/index.md). |
| [precision](precision.md) | [common]<br>val [precision](precision.md): [TimePrecision](../../cl.ravenhill.keen.listeners.precision/-time-precision/index.md)<br>A lambda function that takes a [Duration](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-duration/index.html) and returns a [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) value representing the precision. Defaults to [Duration.inWholeMilliseconds](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-duration/in-whole-milliseconds.html). |
| [ranker](ranker.md) | [common]<br>val [ranker](ranker.md): [IndividualRanker](../../cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;<br>The [IndividualRanker](../../cl.ravenhill.keen.ranking/-individual-ranker/index.md) used to evaluate and rank individuals. Defaults to [FitnessMaxRanker](../../cl.ravenhill.keen.ranking/-fitness-max-ranker/index.md). |
| [timeSource](time-source.md) | [common]<br>val [timeSource](time-source.md): [TimeSource](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-time-source/index.html)<br>The [TimeSource](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-time-source/index.html) providing time-related functionalities. Defaults to [TimeSource.Monotonic](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-time-source/-monotonic/index.html). |
