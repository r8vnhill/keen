//[keen-core](../../../index.md)/[cl.ravenhill.keen.utils.box](../index.md)/[MutableBox](index.md)

# MutableBox

data class [MutableBox](index.md)&lt;[T](index.md)&gt;(var value: [T](index.md)?) : [Box](../-box/index.md)&lt;[T](index.md)&gt; 

A mutable implementation of the `Box` interface.

The `MutableBox` class represents a container that holds a value which can be changed after it is created. It provides methods to apply transformations to the contained value and to convert it to other types of `Box`.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the box. |

## Constructors

| | |
|---|---|
| [MutableBox](-mutable-box.md) | [common]<br>constructor(value: [T](index.md)?)<br>Creates a `MutableBox` containing the given value. |

## Properties

| Name | Summary |
|---|---|
| [value](value.md) | [common]<br>open override var [value](value.md): [T](index.md)?<br>The value contained in the box, or `null` if no value is present. |

## Functions

| Name | Summary |
|---|---|
| [flatMap](flat-map.md) | [common]<br>open override fun &lt;[U](flat-map.md)&gt; [flatMap](flat-map.md)(transform: ([T](index.md)) -&gt; [Box](../-box/index.md)&lt;[U](flat-map.md)&gt;): [MutableBox](index.md)&lt;[U](flat-map.md)&gt;<br>Applies the given transformation function to the value if it is present and returns a new `MutableBox` produced by the function. |
| [fold](../-box/fold.md) | [common]<br>open fun &lt;[U](../-box/fold.md)&gt; [fold](../-box/fold.md)(transform: ([T](index.md)) -&gt; [U](../-box/fold.md)): [U](../-box/fold.md)?<br>Applies the given transformation function to the value if it is present. |
| [map](map.md) | [common]<br>open override fun &lt;[U](map.md)&gt; [map](map.md)(transform: ([T](index.md)) -&gt; [U](map.md)): [MutableBox](index.md)&lt;[U](map.md)&gt;<br>Applies the given transformation function to the value if it is present and returns a new `MutableBox` with the result. |
| [toImmutable](../-box/to-immutable.md) | [common]<br>open fun [toImmutable](../-box/to-immutable.md)(): [ImmutableBox](../-immutable-box/index.md)&lt;[T](index.md)&gt;<br>Converts this `Box` to an immutable box. |
| [toMutable](../-box/to-mutable.md) | [common]<br>open fun [toMutable](../-box/to-mutable.md)(): [MutableBox](index.md)&lt;[T](index.md)&gt;<br>Converts this `Box` to a mutable box. |
