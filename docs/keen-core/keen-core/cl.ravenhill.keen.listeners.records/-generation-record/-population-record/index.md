//[keen-core](../../../../index.md)/[cl.ravenhill.keen.listeners.records](../../index.md)/[GenerationRecord](../index.md)/[PopulationRecord](index.md)

# PopulationRecord

data class [PopulationRecord](index.md)&lt;[T](index.md), [F](index.md) : [Feature](../../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](index.md), [F](index.md)&gt;, [R](index.md) : [Representation](../../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](index.md), [F](index.md)&gt;&gt;(var parents: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[IndividualRecord](../../-individual-record/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt; = emptyList(), var offspring: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[IndividualRecord](../../-individual-record/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt; = emptyList()) : [AbstractTimedRecord](../../-abstract-timed-record/index.md)

Holds the population details for a specific generation, including the parents and offspring.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features. |
| F | The type of feature, which must extend [Feature](../../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of representation, which must extend [Representation](../../../cl.ravenhill.keen.repr/-representation/index.md). |
| parents | A list of parent individuals. |
| offspring | A list of offspring individuals. |

## Constructors

| | |
|---|---|
| [PopulationRecord](-population-record.md) | [common]<br>constructor(parents: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[IndividualRecord](../../-individual-record/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt; = emptyList(), offspring: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[IndividualRecord](../../-individual-record/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt; = emptyList()) |

## Properties

| Name | Summary |
|---|---|
| [duration](../../-abstract-timed-record/duration.md) | [common]<br>var [duration](../../-abstract-timed-record/duration.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>The duration of the event in nanoseconds. |
| [offspring](offspring.md) | [common]<br>var [offspring](offspring.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[IndividualRecord](../../-individual-record/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt; |
| [parents](parents.md) | [common]<br>var [parents](parents.md): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[IndividualRecord](../../-individual-record/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt; |
| [startTime](../../-abstract-timed-record/start-time.md) | [common]<br>lateinit var [startTime](../../-abstract-timed-record/start-time.md): [TimeMark](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-time-mark/index.html)<br>The time mark indicating when the event started. |
