//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners.records](../index.md)/[EvolutionRecord](index.md)

# EvolutionRecord

[common]\
data class [EvolutionRecord](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;&gt;(val generations: [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[GenerationRecord](../-generation-record/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt; = mutableListOf()) : [AbstractTimedRecord](../-abstract-timed-record/index.md)

## Constructors

| | |
|---|---|
| [EvolutionRecord](-evolution-record.md) | [common]<br>constructor(generations: [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[GenerationRecord](../-generation-record/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt; = mutableListOf()) |

## Types

| Name | Summary |
|---|---|
| [InitializationRecord](-initialization-record/index.md) | [common]<br>class [InitializationRecord](-initialization-record/index.md) : [AbstractTimedRecord](../-abstract-timed-record/index.md) |

## Properties

| Name | Summary |
|---|---|
| [duration](../-abstract-timed-record/duration.md) | [common]<br>var [duration](../-abstract-timed-record/duration.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>The duration of the event in nanoseconds. |
| [generations](generations.md) | [common]<br>val [generations](generations.md): [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[GenerationRecord](../-generation-record/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt; |
| [initialization](initialization.md) | [common]<br>val [initialization](initialization.md): [EvolutionRecord.InitializationRecord](-initialization-record/index.md) |
| [startTime](../-abstract-timed-record/start-time.md) | [common]<br>lateinit var [startTime](../-abstract-timed-record/start-time.md): [TimeMark](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-time-mark/index.html)<br>The time mark indicating when the event started. |
