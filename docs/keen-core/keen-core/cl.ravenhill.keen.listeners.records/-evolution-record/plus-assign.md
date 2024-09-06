//[keen-core](../../../index.md)/[cl.ravenhill.keen.listeners.records](../index.md)/[EvolutionRecord](index.md)/[plusAssign](plus-assign.md)

# plusAssign

[common]\
operator fun [plusAssign](plus-assign.md)(generation: [GenerationRecord](../-generation-record/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;)

Adds a new generation to the evolution record, ensuring that the new generation is greater than the last one.

#### Parameters

common

| | |
|---|---|
| generation | The new [GenerationRecord](../-generation-record/index.md) to be added. |

#### Throws

| | |
|---|---|
| CompositeException | If the new generation does not follow the required ordering. |
