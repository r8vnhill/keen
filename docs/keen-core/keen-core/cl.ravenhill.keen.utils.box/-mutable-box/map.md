//[keen-core](../../../index.md)/[cl.ravenhill.keen.utils.box](../index.md)/[MutableBox](index.md)/[map](map.md)

# map

[common]\
open override fun &lt;[U](map.md)&gt; [map](map.md)(transform: ([T](index.md)) -&gt; [U](map.md)): [MutableBox](index.md)&lt;[U](map.md)&gt;

Applies the given transformation function to the value if it is present and returns a new `MutableBox` with the result.

#### Return

A new `MutableBox` containing the result of applying the function to the value, or an empty `MutableBox` if no value is present.

#### Parameters

common

| | |
|---|---|
| U | The type of the value produced by the transformation function. |
| transform | The function to apply to the value. |
