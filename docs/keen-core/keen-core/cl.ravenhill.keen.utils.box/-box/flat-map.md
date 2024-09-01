//[keen-core](../../../index.md)/[cl.ravenhill.keen.utils.box](../index.md)/[Box](index.md)/[flatMap](flat-map.md)

# flatMap

[common]\
abstract fun &lt;[U](flat-map.md)&gt; [flatMap](flat-map.md)(transform: ([T](index.md)) -&gt; [Box](index.md)&lt;[U](flat-map.md)&gt;): [Box](index.md)&lt;[U](flat-map.md)&gt;

Applies the given transformation function to the value if it is present and returns a new `Box` produced by the function.

#### Return

A new `Box` produced by applying the function to the value.

#### Parameters

common

| | |
|---|---|
| U | The type of the value held by the new `Box`. |
| transform | The function to apply to the value. |
