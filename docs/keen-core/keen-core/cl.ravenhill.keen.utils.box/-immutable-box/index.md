//[keen-core](../../../index.md)/[cl.ravenhill.keen.utils.box](../index.md)/[ImmutableBox](index.md)

# ImmutableBox

data class [ImmutableBox](index.md)&lt;[T](index.md)&gt;(val value: [T](index.md)?) : [Box](../-box/index.md)&lt;[T](index.md)&gt; 

An immutable implementation of the `Box` interface.

The `ImmutableBox` class represents a container that holds a value which cannot be changed after it is created. It provides methods to apply transformations to the contained value and to convert it to other types of `Box`.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the box. |

## Constructors

| | |
|---|---|
| [ImmutableBox](-immutable-box.md) | [common]<br>constructor(value: [T](index.md)?)<br>Creates an `ImmutableBox` containing the given value. |

## Properties

| Name | Summary |
|---|---|
| [value](value.md) | [common]<br>open override val [value](value.md): [T](index.md)?<br>The value contained in the box, or `null` if no value is present. |

## Functions

| Name | Summary |
|---|---|
| [flatMap](flat-map.md) | [common]<br>open override fun &lt;[U](flat-map.md)&gt; [flatMap](flat-map.md)(transform: ([T](index.md)) -&gt; [Box](../-box/index.md)&lt;[U](flat-map.md)&gt;): [ImmutableBox](index.md)&lt;[U](flat-map.md)&gt;<br>Applies the given transformation function to the value if it is present and returns a new `ImmutableBox` produced by the function. |
| [fold](../-box/fold.md) | [common]<br>open fun &lt;[U](../-box/fold.md)&gt; [fold](../-box/fold.md)(transform: ([T](index.md)) -&gt; [U](../-box/fold.md)): [U](../-box/fold.md)?<br>Applies the given transformation function to the value if it is present. |
| [map](map.md) | [common]<br>open override fun &lt;[U](map.md)&gt; [map](map.md)(transform: ([T](index.md)) -&gt; [U](map.md)): [ImmutableBox](index.md)&lt;[U](map.md)&gt;<br>Applies the given transformation function to the value if it is present and returns a new `ImmutableBox` with the result. |
| [toImmutable](../-box/to-immutable.md) | [common]<br>open fun [toImmutable](../-box/to-immutable.md)(): [ImmutableBox](index.md)&lt;[T](index.md)&gt;<br>Converts this `Box` to an immutable box. |
| [toMutable](../-box/to-mutable.md) | [common]<br>open fun [toMutable](../-box/to-mutable.md)(): [MutableBox](../-mutable-box/index.md)&lt;[T](index.md)&gt;<br>Converts this `Box` to a mutable box. |
