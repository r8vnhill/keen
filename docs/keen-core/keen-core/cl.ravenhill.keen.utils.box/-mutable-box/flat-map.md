//[keen-core](../../../index.md)/[cl.ravenhill.keen.utils.box](../index.md)/[MutableBox](index.md)/[flatMap](flat-map.md)

# flatMap

[common]\
open override fun &lt;[U](flat-map.md)&gt; [flatMap](flat-map.md)(transform: ([T](index.md)) -&gt; [Box](../-box/index.md)&lt;[U](flat-map.md)&gt;): [MutableBox](index.md)&lt;[U](flat-map.md)&gt;

Applies the given transformation function to the value if it is present and returns a new `MutableBox` produced by the function.

#### Return

A new `MutableBox` containing the result of applying the function to the value, or an empty `MutableBox` if no value is present.

#### Parameters

common

| | |
|---|---|
| U | The type of the value held by the new `MutableBox`. |
| transform | The function to apply to the value. |
