//[keen-core](../../../index.md)/[cl.ravenhill.keen.utils.box](../index.md)/[Box](index.md)/[map](map.md)

# map

[common]\
abstract fun &lt;[U](map.md)&gt; [map](map.md)(transform: ([T](index.md)) -&gt; [U](map.md)): [Box](index.md)&lt;[U](map.md)&gt;

Applies the given transformation function to the value if it is present and returns a new `Box` with the result.

#### Return

A new `Box` containing the result of applying the function to the value.

#### Parameters

common

| | |
|---|---|
| U | The type of the value produced by the transformation function. |
| transform | The function to apply to the value. |
