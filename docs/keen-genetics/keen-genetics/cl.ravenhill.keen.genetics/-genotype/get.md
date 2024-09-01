//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics](../index.md)/[Genotype](index.md)/[get](get.md)

# get

[common]\
operator fun [get](get.md)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): Either&lt;CompositeException, [Chromosome](../../cl.ravenhill.keen.genetics.chromosomes/-chromosome/index.md)&lt;[T](index.md), [G](index.md)&gt;&gt;

Retrieves the chromosome at the specified index.

#### Return

The chromosome at the specified index.

#### Parameters

common

| | |
|---|---|
| index | The index of the chromosome to retrieve. |

#### Throws

| | |
|---|---|
| [InvalidIndexException](../../../../keen-core/keen-core/cl.ravenhill.keen.exceptions/-invalid-index-exception/index.md) | If the index is out of range. |
