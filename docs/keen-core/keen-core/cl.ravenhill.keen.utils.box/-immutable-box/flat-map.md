//[keen-core](../../../index.md)/[cl.ravenhill.keen.utils.box](../index.md)/[ImmutableBox](index.md)/[flatMap](flat-map.md)

# flatMap

[common]\
open override fun &lt;[U](flat-map.md)&gt; [flatMap](flat-map.md)(transform: ([T](index.md)) -&gt; [Box](../-box/index.md)&lt;[U](flat-map.md)&gt;): [ImmutableBox](index.md)&lt;[U](flat-map.md)&gt;

Applies the given transformation function to the value if it is present and returns a new `ImmutableBox` produced by the function.

#### Return

A new `ImmutableBox` containing the result of applying the function to the value, or an empty `ImmutableBox` if no value is present.

#### Parameters

common

| | |
|---|---|
| U | The type of the value held by the new `ImmutableBox`. |
| transform | The function to apply to the value. |
