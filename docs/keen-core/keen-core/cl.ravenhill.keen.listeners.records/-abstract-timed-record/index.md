//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners.records](../index.md)/[AbstractTimedRecord](index.md)

# AbstractTimedRecord

abstract class [AbstractTimedRecord](index.md)

Abstract class representing a timed record in the Keen evolutionary computation framework.

The `AbstractTimedRecord` class provides a structure for recording the start time and duration of events within the evolutionary algorithm. It is designed to be extended by other classes that require timing information.

## Usage:

This class is intended to be used as a base class for other timed records in the framework. Subclasses can use the `startTime` and `duration` properties to record and manage timing information for various events.

### Example 1: Extending AbstractTimedRecord

```kotlin
class MyTimedRecord : AbstractTimedRecord() {
    fun start() {
        startTime = TimeSource.Monotonic.markNow()
    }

    fun stop() {
        duration = startTime.elapsedNow().toLong(DurationUnit.NANOSECONDS)
    }
}
```

#### Inheritors

| |
|---|
| [EvolutionRecord](../-evolution-record/index.md) |
| [InitializationRecord](../-evolution-record/-initialization-record/index.md) |
| [GenerationRecord](../-generation-record/index.md) |
| [AlterationRecord](../-generation-record/-alteration-record/index.md) |
| [EvaluationRecord](../-generation-record/-evaluation-record/index.md) |
| [SelectionRecord](../-generation-record/-selection-record/index.md) |
| [PopulationRecord](../-generation-record/-population-record/index.md) |

## Constructors

| | |
|---|---|
| [AbstractTimedRecord](-abstract-timed-record.md) | [common]<br>constructor() |

## Properties

| Name | Summary |
|---|---|
| [duration](duration.md) | [common]<br>var [duration](duration.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>The duration of the event in nanoseconds. |
| [startTime](start-time.md) | [common]<br>lateinit var [startTime](start-time.md): [TimeMark](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.time/-time-mark/index.html)<br>The time mark indicating when the event started. |
