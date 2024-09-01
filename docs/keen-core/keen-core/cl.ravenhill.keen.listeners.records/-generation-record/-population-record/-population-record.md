//[keen-core](../../../../index.md)/[cl.ravenhill.keen.listeners.records](../../index.md)/[GenerationRecord](../index.md)/[PopulationRecord](index.md)/[PopulationRecord](-population-record.md)

# PopulationRecord

[common]\
constructor(parents: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[IndividualRecord](../../-individual-record/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt; = emptyList(), offspring: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[IndividualRecord](../../-individual-record/index.md)&lt;[T](index.md), [F](index.md), [R](index.md)&gt;&gt; = emptyList())

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the features. |
| F | The type of feature, which must extend [Feature](../../../cl.ravenhill.keen.repr/-feature/index.md). |
| R | The type of representation, which must extend [Representation](../../../cl.ravenhill.keen.repr/-representation/index.md). |
| parents | A list of parent individuals. |
| offspring | A list of offspring individuals. |
