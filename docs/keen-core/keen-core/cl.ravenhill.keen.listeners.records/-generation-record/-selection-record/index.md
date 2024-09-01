//[keen-core](../../../../index.md)/[cl.ravenhill.keen.listeners.records](../../index.md)/[GenerationRecord](../index.md)/[SelectionRecord](index.md)

# SelectionRecord

[common]\
class [SelectionRecord](index.md) : [AbstractTimedRecord](../../-abstract-timed-record/index.md)

Tracks timing information related to the selection phases (both parent and survivor selection).

## Constructors

| | |
|---|---|
| [SelectionRecord](-selection-record.md) | [common]<br>constructor() |

## Properties

| Name | Summary |
|---|---|
| [duration](../../-abstract-timed-record/duration.md) | [common]<br>var [duration](../../-abstract-timed-record/duration.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>The duration of the event in nanoseconds. |
| [startTime](../../-abstract-timed-record/start-time.md) | [common]<br>lateinit var [startTime](../../-abstract-timed-record/start-time.md): [TimeMark](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-time-mark/index.html)<br>The time mark indicating when the event started. |
