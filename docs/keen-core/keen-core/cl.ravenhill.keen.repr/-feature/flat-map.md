//[keen-core](../../../index.md)/[cl.ravenhill.keen.repr](../index.md)/[Feature](index.md)/[flatMap](flat-map.md)

# flatMap

[common]\
open fun [flatMap](flat-map.md)(f: ([T](index.md)) -&gt; [F](index.md)): [F](index.md)

Applies a function to the feature's value and returns a new feature instance with the transformed value.

The `flatMap` method allows for chaining operations that transform the feature's value and return a new feature. This method is a key part of the monadic structure, enabling complex transformations while maintaining the integrity of the feature's structure.

#### Return

A new feature instance with the transformed value.

#### Parameters

common

| | |
|---|---|
| f | The function to apply to the feature's value. |
