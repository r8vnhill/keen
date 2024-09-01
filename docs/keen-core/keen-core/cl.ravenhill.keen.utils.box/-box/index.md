//[keen-core](../../../index.md)/[cl.ravenhill.keen.utils.box](../index.md)/[Box](index.md)

# Box

sealed interface [Box](index.md)&lt;[T](index.md)&gt;

A sealed interface representing a container for a value.

The `Box` interface provides a way to encapsulate a value that can be either present or absent. It supports common functional operations such as `map`, `flatMap`, and `fold`, and can be converted between mutable and immutable forms.

#### Parameters

common

| | |
|---|---|
| T | The type of the value held by the box. |

#### Inheritors

| |
|---|
| [ImmutableBox](../-immutable-box/index.md) |
| [MutableBox](../-mutable-box/index.md) |

## Properties

| Name | Summary |
|---|---|
| [value](value.md) | [common]<br>abstract val [value](value.md): [T](index.md)?<br>The value contained in the box, or `null` if no value is present. |

## Functions

| Name | Summary |
|---|---|
| [flatMap](flat-map.md) | [common]<br>abstract fun &lt;[U](flat-map.md)&gt; [flatMap](flat-map.md)(transform: ([T](index.md)) -&gt; [Box](index.md)&lt;[U](flat-map.md)&gt;): [Box](index.md)&lt;[U](flat-map.md)&gt;<br>Applies the given transformation function to the value if it is present and returns a new `Box` produced by the function. |
| [fold](fold.md) | [common]<br>open fun &lt;[U](fold.md)&gt; [fold](fold.md)(transform: ([T](index.md)) -&gt; [U](fold.md)): [U](fold.md)?<br>Applies the given transformation function to the value if it is present. |
| [map](map.md) | [common]<br>abstract fun &lt;[U](map.md)&gt; [map](map.md)(transform: ([T](index.md)) -&gt; [U](map.md)): [Box](index.md)&lt;[U](map.md)&gt;<br>Applies the given transformation function to the value if it is present and returns a new `Box` with the result. |
| [toImmutable](to-immutable.md) | [common]<br>open fun [toImmutable](to-immutable.md)(): [ImmutableBox](../-immutable-box/index.md)&lt;[T](index.md)&gt;<br>Converts this `Box` to an immutable box. |
| [toMutable](to-mutable.md) | [common]<br>open fun [toMutable](to-mutable.md)(): [MutableBox](../-mutable-box/index.md)&lt;[T](index.md)&gt;<br>Converts this `Box` to a mutable box. |
