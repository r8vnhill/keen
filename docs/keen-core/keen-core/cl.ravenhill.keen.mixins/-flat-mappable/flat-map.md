//[keen-core](../../../index.md)/[cl.ravenhill.keen.mixins](../index.md)/[FlatMappable](index.md)/[flatMap](flat-map.md)

# flatMap

[common]\
open fun &lt;[R](flat-map.md)&gt; [flatMap](flat-map.md)(f: ([T](index.md)) -&gt; [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[R](flat-map.md)&gt;): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[R](flat-map.md)&gt;

Applies a transformation function to each flattened element and returns a list of the results.

This method provides a default implementation that first flattens the structure and then applies the transformation function to each element in the flattened list.

#### Return

A list of transformed elements.

#### Parameters

common

| | |
|---|---|
| R | The type of elements in the resulting list. |
| f | The transformation function to apply to each flattened element. |
