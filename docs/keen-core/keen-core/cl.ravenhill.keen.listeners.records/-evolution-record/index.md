//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners.records](../index.md)/[EvolutionRecord](index.md)

# EvolutionRecord

data class [EvolutionRecord](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;&gt;(_generations: [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[GenerationRecord](../-generation-record/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt; = mutableListOf()) : [AbstractTimedRecord](../-abstract-timed-record/index.md)

Represents a record of the evolution process in an evolutionary algorithm, tracking the generations that occur.

The `EvolutionRecord` stores a list of [GenerationRecord](../-generation-record/index.md) instances, ensuring that there are no duplicate generations and that the generations are ordered in a monotonically increasing manner. It also includes an [InitializationRecord](-initialization-record/index.md) to track initialization timing.

#### Parameters

common

| | |
|---|---|
| T | The type of value held by the features in the generations. |
| F | The type of feature used in the representation, which must implement [Feature](../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of representation, which must implement [Representation](../../cl.ravenhill.keen.repr/-representation/index.md). |

#### Throws

| | |
|---|---|
| CompositeException | If the constraints on the generations are violated, such as duplicate generations or unordered generations. |

## Constructors

| | |
|---|---|
| [EvolutionRecord](-evolution-record.md) | [common]<br>constructor(_generations: [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[GenerationRecord](../-generation-record/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt; = mutableListOf()) |

## Types

| Name | Summary |
|---|---|
| [InitializationRecord](-initialization-record/index.md) | [common]<br>class [InitializationRecord](-initialization-record/index.md) : [AbstractTimedRecord](../-abstract-timed-record/index.md)<br>A record that tracks the initialization timing of the evolutionary process. |

## Properties

| Name | Summary |
|---|---|
| [duration](../-abstract-timed-record/duration.md) | [common]<br>var [duration](../-abstract-timed-record/duration.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>The duration of the event in nanoseconds. |
| [generations](generations.md) | [common]<br>val [generations](generations.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[GenerationRecord](../-generation-record/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt;<br>The list of [GenerationRecord](../-generation-record/index.md) instances that track each generation in the evolution. |
| [initialization](initialization.md) | [common]<br>val [initialization](initialization.md): [EvolutionRecord.InitializationRecord](-initialization-record/index.md)<br>An [InitializationRecord](-initialization-record/index.md) instance that tracks the timing of the initialization process. |
| [startTime](../-abstract-timed-record/start-time.md) | [common]<br>lateinit var [startTime](../-abstract-timed-record/start-time.md): [TimeMark](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-time-mark/index.html)<br>The time mark indicating when the event started. |

## Functions

| Name | Summary |
|---|---|
| [plusAssign](plus-assign.md) | [common]<br>operator fun [plusAssign](plus-assign.md)(generation: [GenerationRecord](../-generation-record/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;)<br>Adds a new generation to the evolution record, ensuring that the new generation is greater than the last one. |
