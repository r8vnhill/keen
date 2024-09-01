//[keen-core](../../../../index.md)/[cl.ravenhill.keen.listeners.records](../../index.md)/[IndividualRecord](../index.md)/[Companion](index.md)/[fromIndividual](from-individual.md)

# fromIndividual

[common]\
fun &lt;[T](from-individual.md), [F](from-individual.md) : [Feature](../../../cl.ravenhill.keen.repr/-feature/index.md)&lt;[T](from-individual.md), [F](from-individual.md)&gt;, [R](from-individual.md) : [Representation](../../../cl.ravenhill.keen.repr/-representation/index.md)&lt;[T](from-individual.md), [F](from-individual.md)&gt;&gt; [fromIndividual](from-individual.md)(individual: [Individual](../../../cl.ravenhill.keen/-individual/index.md)&lt;[T](from-individual.md), [F](from-individual.md), [R](from-individual.md)&gt;): [IndividualRecord](../index.md)&lt;[T](from-individual.md), [F](from-individual.md), [R](from-individual.md)&gt;

Creates an `IndividualRecord` from an `Individual` object.

#### Return

An `IndividualRecord` with the same representation and fitness.

#### Parameters

common

| | |
|---|---|
| individual | The individual to convert to a record. |
