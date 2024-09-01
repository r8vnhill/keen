//[keen-core](../../../index.md)/[cl.ravenhill.keen.repr](../index.md)/[Feature](index.md)/[copyWithValue](copy-with-value.md)

# copyWithValue

[common]\
abstract fun [copyWithValue](copy-with-value.md)(value: [T](index.md)): [F](index.md)

Creates a duplicate of the feature with a new specified value.

This method returns a new instance of the feature, identical in every way except for the new value. It is useful in genetic algorithms where a new feature needs to be generated based on an existing one, but with a modified value.

#### Return

A new feature instance with the specified value.

#### Parameters

common

| | |
|---|---|
| value | The new value for the feature. |
