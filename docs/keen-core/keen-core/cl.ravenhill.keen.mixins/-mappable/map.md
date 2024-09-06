//[keen-core](../../../index.md)/[cl.ravenhill.keen.mixins](../index.md)/[Mappable](index.md)/[map](map.md)

# map

[common]\
abstract fun [map](map.md)(transform: ([T](index.md)) -&gt; [T](index.md)): [Mappable](index.md)&lt;[T](index.md)&gt;

Applies the given transformation function to the value contained within the mappable structure.

The `map` function takes a transformation function [transform](map.md), applies it to the internal value, and returns a new `Mappable` instance containing the transformed value. Implementers must ensure that the original instance is not modified and that a new instance is returned with the transformed value.

#### Return

A new `Mappable` instance containing the transformed value.

#### Parameters

common

| | |
|---|---|
| transform | A function that takes the current value of type [T](index.md) and returns a transformed value of type [T](index.md). |
