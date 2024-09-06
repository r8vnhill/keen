//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners](../index.md)/[ListenerConfiguration](index.md)

# ListenerConfiguration

data class [ListenerConfiguration](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;&gt;(val ranker: [IndividualRanker](../../cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; = defaultRanker(), val evolution: [EvolutionRecord](../../cl.ravenhill.keen.listeners.records/-evolution-record/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; = defaultEvolutionRecord(), val timeSource: [TimeSource](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-time-source/index.html) = defaultTimeSource, val precision: [TimePrecision](../../cl.ravenhill.keen.listeners.precision/-time-precision/index.md) = defaultPrecision)

Configuration class for initializing and managing listeners in an evolutionary algorithm.

The `ListenerConfiguration` class encapsulates the necessary configurations for listeners used in an evolutionary algorithm. It provides defaults for various components, such as the ranker, evolution record, time source, and time precision, ensuring that listeners have the required context and resources to operate effectively.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the genes in the individuals. |
| F | The type of the feature used in the individual's representation. |
| R | The type of the representation used by the individual. |

## Constructors

| | |
|---|---|
| [ListenerConfiguration](-listener-configuration.md) | [common]<br>constructor(ranker: [IndividualRanker](../../cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; = defaultRanker(), evolution: [EvolutionRecord](../../cl.ravenhill.keen.listeners.records/-evolution-record/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt; = defaultEvolutionRecord(), timeSource: [TimeSource](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-time-source/index.html) = defaultTimeSource, precision: [TimePrecision](../../cl.ravenhill.keen.listeners.precision/-time-precision/index.md) = defaultPrecision) |

## Properties

| Name | Summary |
|---|---|
| [currentGeneration](current-generation.md) | [common]<br>val [currentGeneration](current-generation.md): [MutableBox](../../cl.ravenhill.keen.utils.box/-mutable-box/index.md)&lt;[GenerationRecord](../../cl.ravenhill.keen.listeners.records/-generation-record/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;?&gt;<br>A [MutableBox](../../cl.ravenhill.keen.utils.box/-mutable-box/index.md) that holds the current generation's record, allowing listeners to access and modify the generation data. |
| [evolution](evolution.md) | [common]<br>val [evolution](evolution.md): [EvolutionRecord](../../cl.ravenhill.keen.listeners.records/-evolution-record/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;<br>The [EvolutionRecord](../../cl.ravenhill.keen.listeners.records/-evolution-record/index.md) that tracks the state and progress of the evolutionary process. Defaults to [defaultEvolutionRecord](../../../../keen-core/cl.ravenhill.keen.listeners/-listener-configuration/-companion/default-evolution-record.md). |
| [precision](precision.md) | [common]<br>val [precision](precision.md): [TimePrecision](../../cl.ravenhill.keen.listeners.precision/-time-precision/index.md)<br>The [TimePrecision](../../cl.ravenhill.keen.listeners.precision/-time-precision/index.md) that defines the precision level for timing operations. Defaults to [defaultPrecision](../../../../keen-core/cl.ravenhill.keen.listeners/-listener-configuration/-companion/default-precision.md). |
| [ranker](ranker.md) | [common]<br>val [ranker](ranker.md): [IndividualRanker](../../cl.ravenhill.keen.ranking/-individual-ranker/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;<br>The [IndividualRanker](../../cl.ravenhill.keen.ranking/-individual-ranker/index.md) used to evaluate and compare individuals in the population. Defaults to [defaultRanker](../../../../keen-core/cl.ravenhill.keen.listeners/-listener-configuration/-companion/default-ranker.md). |
| [timeSource](time-source.md) | [common]<br>val [timeSource](time-source.md): [TimeSource](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-time-source/index.html)<br>The [TimeSource](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-time-source/index.html) used for timing operations within the evolutionary algorithm. Defaults to [defaultTimeSource](../../../../keen-core/cl.ravenhill.keen.listeners/-listener-configuration/-companion/default-time-source.md). |
