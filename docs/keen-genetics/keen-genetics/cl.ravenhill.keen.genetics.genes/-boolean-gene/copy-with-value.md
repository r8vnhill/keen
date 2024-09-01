//[keen-genetics](../../../index.md)/[cl.ravenhill.keen.genetics.genes](../index.md)/[BooleanGene](index.md)/[copyWithValue](copy-with-value.md)

# copyWithValue

[common]\
open override fun [copyWithValue](copy-with-value.md)(value: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [BooleanGene](index.md)

Duplicates the gene with a new specified boolean value.

This method returns a new instance of the gene with the provided value. It uses an `if-else` structure to determine whether to return [True](-true/index.md) or [False](-false/index.md), providing an alternative to pattern matching.

#### Return

A new `BooleanGene` instance with the specified value.

#### Parameters

common

| | |
|---|---|
| value | The new boolean value for the gene. |
