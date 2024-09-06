//[keen-core](../../../index.md)/[cl.ravenhill.keen.repr](../index.md)/[Feature](index.md)/[map](map.md)

# map

[common]\
open override fun [map](map.md)(transform: ([T](index.md)) -&gt; [T](index.md)): [F](index.md)

Applies a transformation function to the value held by the feature and returns a new feature with the transformed value.

The `map` function is an essential part of the `Feature` interface, allowing for a transformation of the feature's value without altering the original feature. It is commonly used in functional programming contexts where operations are applied to the values within a container (in this case, the feature) without mutating the container itself. The `map` function returns a new instance of the feature with the transformed value.

#### Return

A new feature instance with the transformed value.

#### Parameters

common

| | |
|---|---|
| transform | A function that takes the current value of the feature and returns a new transformed value. |
