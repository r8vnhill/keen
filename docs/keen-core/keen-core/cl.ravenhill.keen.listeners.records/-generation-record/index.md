//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners.records](../index.md)/[GenerationRecord](index.md)

# GenerationRecord

data class [GenerationRecord](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;&gt;(val generation: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) : [AbstractTimedRecord](../-abstract-timed-record/index.md)

Represents a record of a single generation in an evolutionary algorithm.

The `GenerationRecord` class captures detailed information about a specific generation within an evolutionary algorithm. This includes the generation number, the steady counter (indicating how many consecutive generations have maintained the same fittest individual), and various timed records related to key evolutionary operations such as alteration, evaluation, and selection.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features. |
| F | The type of feature, which must extend [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of representation, which must extend [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |

#### Throws

| | |
|---|---|
| CompositeException | if any of the constraints are violated. |

## Constructors

| | |
|---|---|
| [GenerationRecord](-generation-record.md) | [common]<br>constructor(generation: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |

## Types

| Name | Summary |
|---|---|
| [AlterationRecord](-alteration-record/index.md) | [common]<br>class [AlterationRecord](-alteration-record/index.md) : [AbstractTimedRecord](../-abstract-timed-record/index.md)<br>Tracks timing information related to the alteration phase. |
| [EvaluationRecord](-evaluation-record/index.md) | [common]<br>class [EvaluationRecord](-evaluation-record/index.md) : [AbstractTimedRecord](../-abstract-timed-record/index.md)<br>Tracks timing information related to the evaluation phase. |
| [PopulationRecord](-population-record/index.md) | [common]<br>data class [PopulationRecord](-population-record/index.md)&lt;[T](-population-record/index.md), [F](-population-record/index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](-population-record/index.md), [F](-population-record/index.md)&gt;, [R](-population-record/index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](-population-record/index.md), [F](-population-record/index.md)&gt;&gt;(var parents: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[IndividualRecord](../-individual-record/index.md)&lt;[T](-population-record/index.md), [F](-population-record/index.md), [R](-population-record/index.md)&gt;&gt; = emptyList(), var offspring: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[IndividualRecord](../-individual-record/index.md)&lt;[T](-population-record/index.md), [F](-population-record/index.md), [R](-population-record/index.md)&gt;&gt; = emptyList()) : [AbstractTimedRecord](../-abstract-timed-record/index.md)<br>Holds the population details for a specific generation, including the parents and offspring. |
| [SelectionRecord](-selection-record/index.md) | [common]<br>class [SelectionRecord](-selection-record/index.md) : [AbstractTimedRecord](../-abstract-timed-record/index.md)<br>Tracks timing information related to the selection phases (both parent and survivor selection). |

## Properties

| Name | Summary |
|---|---|
| [alteration](alteration.md) | [common]<br>val [alteration](alteration.md): [GenerationRecord.AlterationRecord](-alteration-record/index.md)<br>Tracks the timing information related to the alteration process in the evolutionary algorithm. |
| [duration](../-abstract-timed-record/duration.md) | [common]<br>var [duration](../-abstract-timed-record/duration.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>The duration of the event in nanoseconds. |
| [evaluation](evaluation.md) | [common]<br>val [evaluation](evaluation.md): [GenerationRecord.EvaluationRecord](-evaluation-record/index.md)<br>Tracks the timing information related to the evaluation process. |
| [generation](generation.md) | [common]<br>val [generation](generation.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The generation number, which must not be negative. |
| [parentSelection](parent-selection.md) | [common]<br>val [parentSelection](parent-selection.md): [GenerationRecord.SelectionRecord](-selection-record/index.md)<br>Tracks the timing information related to the parent selection process. |
| [population](population.md) | [common]<br>val [population](population.md): [GenerationRecord.PopulationRecord](-population-record/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;<br>Holds the record of the population, including the parents and offspring for this generation. |
| [startTime](../-abstract-timed-record/start-time.md) | [common]<br>lateinit var [startTime](../-abstract-timed-record/start-time.md): [TimeMark](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-time-mark/index.html)<br>The time mark indicating when the event started. |
| [steady](steady.md) | [common]<br>var [steady](steady.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>The counter for steady generations. This tracks how many consecutive generations have remained steady, meaning no significant changes in fitness values.  It must not be negative. |
| [survivorSelection](survivor-selection.md) | [common]<br>val [survivorSelection](survivor-selection.md): [GenerationRecord.SelectionRecord](-selection-record/index.md)<br>Tracks the timing information related to the survivor selection process. |
